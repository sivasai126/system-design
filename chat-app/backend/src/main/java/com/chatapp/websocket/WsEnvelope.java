package com.chatapp.websocket;

import com.chatapp.message.MessageDtos;

import java.time.Instant;
import java.util.UUID;

/**
 * Envelope for all messages pushed from server → client over WebSocket.
 *
 * type:
 *   "NEW_MESSAGE"   - a new message in a conversation the user belongs to
 *   "USER_ONLINE"   - a contact came online
 *   "USER_OFFLINE"  - a contact went offline
 */
public class WsEnvelope {

    public record OutboundMessage(
            String type,
            Object payload,
            Instant timestamp
    ) {
        public static OutboundMessage newMessage(MessageDtos.MessageResponse message) {
            return new OutboundMessage("NEW_MESSAGE", message, Instant.now());
        }

        public static OutboundMessage userOnline(UUID userId) {
            return new OutboundMessage("USER_ONLINE", new UserStatusPayload(userId.toString()), Instant.now());
        }

        public static OutboundMessage userOffline(UUID userId) {
            return new OutboundMessage("USER_OFFLINE", new UserStatusPayload(userId.toString()), Instant.now());
        }
    }

    public record UserStatusPayload(String userId) {}
}
