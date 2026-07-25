package com.chatapp.websocket;

import com.chatapp.message.MessageDtos;
import com.chatapp.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfflineMessageDeliveryService {

    private final MessageRepository messageRepository;
    private final WebSocketMessageSender sender;

    /**
     * Called when a user reconnects.
     * Loads all undelivered messages for this user, pushes them over WebSocket,
     * then bulk-marks them delivered in a single UPDATE.
     *
     * The @Transactional boundary ensures that if sending partially fails
     * (e.g. session closes mid-flush), the delivered flag won't be set
     * for messages that didn't actually go through.
     * We commit only after all sends succeed.
     */
    @Transactional
    public void deliverPendingMessages(UUID userId) {
        var undelivered = messageRepository.findUndeliveredForUser(userId);
        if (undelivered.isEmpty()) {
            log.debug("No pending messages for user {}", userId);
            return;
        }

        log.info("Delivering {} pending messages to user {}", undelivered.size(), userId);

        List<UUID> deliveredIds = undelivered.stream()
                .map(message -> {
                    WsEnvelope.OutboundMessage envelope = WsEnvelope.OutboundMessage
                            .newMessage(MessageDtos.MessageResponse.from(message));
                    boolean sent = sender.sendToUser(userId, envelope);
                    return sent ? message.getId() : null;
                })
                .filter(id -> id != null)
                .toList();

        if (!deliveredIds.isEmpty()) {
            messageRepository.markDelivered(deliveredIds);
            log.info("Marked {} messages as delivered for user {}", deliveredIds.size(), userId);
        }
    }
}
