package com.chatapp.websocket;

import com.chatapp.conversation.ConversationMemberRepository;
import com.chatapp.conversation.ConversationMemberId;
import com.chatapp.message.Message;
import com.chatapp.message.MessageDtos;
import com.chatapp.message.MessageRepository;
import com.chatapp.conversation.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketDeliveryService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final WebSocketMessageSender sender;

    /**
     * Pushes a newly persisted message to all online members of its conversation.
     * Marks the message as delivered if at least one recipient received it.
     *
     * The sender receives the message too (needed for multi-device sync,
     * and so the sender's own chat window updates in real-time).
     *
     * Called AFTER the message is committed to the DB — so we never push
     * something that failed to persist.
     */
    @Transactional
    public void deliverNewMessage(Message message) {
        UUID conversationId = message.getConversation().getId();
        UUID senderId = message.getSender().getId();

        // Load all member user IDs for this conversation
        List<UUID> memberIds = conversationRepository.findById(conversationId)
                .map(c -> c.getMembers().stream()
                        .map(m -> m.getUser().getId())
                        .toList())
                .orElse(List.of());

        if (memberIds.isEmpty()) {
            log.warn("No members found for conversation {}", conversationId);
            return;
        }

        WsEnvelope.OutboundMessage envelope = WsEnvelope.OutboundMessage
                .newMessage(MessageDtos.MessageResponse.from(message));

        boolean anyDelivered = false;
        for (UUID memberId : memberIds) {
            // Skip the sender — they already have the message from the REST response
            if (memberId.equals(senderId)) continue;

            boolean sent = sender.sendToUser(memberId, envelope);
            if (sent) {
                anyDelivered = true;
                log.debug("Message {} delivered to user {} via WebSocket", message.getId(), memberId);
            }
        }

        if (anyDelivered) {
            messageRepository.markDelivered(List.of(message.getId()));
            log.debug("Message {} marked as delivered", message.getId());
        }
    }
}
