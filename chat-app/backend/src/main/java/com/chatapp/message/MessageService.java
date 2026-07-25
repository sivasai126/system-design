package com.chatapp.message;

import com.chatapp.conversation.Conversation;
import com.chatapp.conversation.ConversationService;
import com.chatapp.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    /**
     * Persists a message. Caller must be a member of the conversation.
     * Returns the saved message (WebSocket delivery happens in the controller layer).
     */
    @Transactional
    public Message sendMessage(User sender, MessageDtos.SendMessageRequest request) {
        conversationService.assertMember(request.conversationId(), sender.getId());

        Conversation conversation = conversationService.getOrThrow(request.conversationId());

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.content())
                .delivered(false)
                .build();

        message = messageRepository.save(message);
        log.debug("Message {} saved to conversation {}", message.getId(), conversation.getId());
        return message;
    }

    /**
     * Returns full conversation history. Caller must be a member.
     */
    @Transactional(readOnly = true)
    public List<MessageDtos.MessageResponse> getHistory(User caller, UUID conversationId) {
        conversationService.assertMember(conversationId, caller.getId());

        return messageRepository.findByConversationId(conversationId)
                .stream()
                .map(MessageDtos.MessageResponse::from)
                .toList();
    }
}
