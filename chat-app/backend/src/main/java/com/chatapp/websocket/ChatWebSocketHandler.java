package com.chatapp.websocket;

import com.chatapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

/**
 * Handles the WebSocket lifecycle:
 *
 * 1. afterConnectionEstablished — validate JWT from query param, register session,
 *    then flush any undelivered offline messages.
 * 2. afterConnectionClosed — remove from registry, notify contacts (future).
 * 3. handleTextMessage — currently unused from client→server (messages go via REST POST /messages).
 *    Kept for extensibility (e.g. typing indicators).
 *
 * JWT is passed as a query parameter:
 *   ws://localhost:8080/ws/chat?token=<jwt>
 *
 * Why query param and not Authorization header?
 * Browser WebSocket API does not support custom headers during the handshake.
 * Query param is the standard approach. The token is short-lived (24h), so
 * exposure in server logs is acceptable for MVP. For production, use a
 * short-lived ticket exchanged via a REST endpoint.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String TOKEN_PARAM = "token";
    private static final String USER_ID_ATTR = "userId";

    private final JwtService jwtService;
    private final OnlineUserRegistry registry;
    private final OfflineMessageDeliveryService offlineDeliveryService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        UUID userId = authenticate(session);
        if (userId == null) {
            closeUnauthorized(session);
            return;
        }

        session.getAttributes().put(USER_ID_ATTR, userId);
        registry.register(userId, session);

        // Deliver any messages that arrived while the user was offline
        offlineDeliveryService.deliverPendingMessages(userId);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus status) {
        UUID userId = (UUID) session.getAttributes().get(USER_ID_ATTR);
        if (userId != null) {
            registry.remove(userId);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session,
                                     @NonNull TextMessage message) {
        // Client→server messages are not used in the current design.
        // Messages are sent via POST /api/messages (REST), which then
        // pushes to recipients over WebSocket.
        // This handler exists for future features like typing indicators.
        log.debug("Received WS message from session {}: {}", session.getId(), message.getPayload());
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session,
                                     @NonNull Throwable exception) {
        UUID userId = (UUID) session.getAttributes().get(USER_ID_ATTR);
        log.warn("Transport error for user {} (session {}): {}", userId, session.getId(), exception.getMessage());
        // afterConnectionClosed will be called next and handles registry cleanup
    }

    // ---- private helpers ----

    private UUID authenticate(WebSocketSession session) {
        try {
            String uri = session.getUri() != null ? session.getUri().toString() : "";
            String token = extractTokenFromUri(uri);
            if (token == null || !jwtService.validateToken(token)) {
                log.warn("WebSocket connection rejected — invalid or missing token. Session: {}", session.getId());
                return null;
            }
            return jwtService.extractUserId(token);
        } catch (Exception e) {
            log.warn("WebSocket auth failed: {}", e.getMessage());
            return null;
        }
    }

    private String extractTokenFromUri(String uri) {
        // Parse ?token=<value> from URI string
        int idx = uri.indexOf("token=");
        if (idx == -1) return null;
        String rest = uri.substring(idx + 6);
        int ampersand = rest.indexOf('&');
        return ampersand == -1 ? rest : rest.substring(0, ampersand);
    }

    private void closeUnauthorized(WebSocketSession session) {
        try {
            session.close(CloseStatus.POLICY_VIOLATION);
        } catch (Exception e) {
            log.warn("Failed to close unauthorized session: {}", e.getMessage());
        }
    }
}
