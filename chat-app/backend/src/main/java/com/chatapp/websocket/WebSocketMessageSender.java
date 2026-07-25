package com.chatapp.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageSender {

    private final OnlineUserRegistry registry;
    private final ObjectMapper objectMapper;

    /**
     * Sends an envelope to a user if they are online.
     * Silently skips if the user is offline or the session has closed.
     *
     * @return true if the message was sent successfully
     */
    public boolean sendToUser(UUID userId, WsEnvelope.OutboundMessage envelope) {
        return registry.getSession(userId).map(session -> {
            try {
                String json = objectMapper.writeValueAsString(envelope);
                synchronized (session) {
                    // WebSocketSession.sendMessage is not thread-safe; synchronize on session
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(json));
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to send WebSocket message to user {}: {}", userId, e.getMessage());
                registry.remove(userId);
            }
            return false;
        }).orElse(false);
    }
}
