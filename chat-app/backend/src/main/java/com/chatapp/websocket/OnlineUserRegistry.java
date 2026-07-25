package com.chatapp.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory registry of online users.
 * Keyed by userId → WebSocketSession.
 *
 * Thread-safe via ConcurrentHashMap. Single-node only (fine for MVP).
 * For multi-node, replace with Redis pub/sub — the interface stays the same.
 */
@Component
@Slf4j
public class OnlineUserRegistry {

    private final ConcurrentHashMap<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(UUID userId, WebSocketSession session) {
        WebSocketSession previous = sessions.put(userId, session);
        if (previous != null && previous.isOpen()) {
            log.warn("User {} connected from a new session; old session {} replaced", userId, previous.getId());
        }
        log.info("User {} connected (session {}). Online users: {}", userId, session.getId(), sessions.size());
    }

    public void remove(UUID userId) {
        sessions.remove(userId);
        log.info("User {} disconnected. Online users: {}", userId, sessions.size());
    }

    public Optional<WebSocketSession> getSession(UUID userId) {
        return Optional.ofNullable(sessions.get(userId))
                .filter(WebSocketSession::isOpen);
    }

    public boolean isOnline(UUID userId) {
        return getSession(userId).isPresent();
    }

    public Collection<UUID> onlineUserIds() {
        return sessions.keySet();
    }
}
