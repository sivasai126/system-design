package com.chatapp.conversation;

import com.chatapp.exception.ForbiddenException;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.user.User;
import com.chatapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;

    /**
     * Returns an existing DIRECT conversation between caller and recipient,
     * or creates a new one. Idempotent.
     */
    @Transactional
    public ConversationDtos.ConversationResponse getOrCreateDirect(
            User caller, UUID recipientId) {

        if (caller.getId().equals(recipientId)) {
            throw new ForbiddenException("Cannot create a conversation with yourself");
        }

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + recipientId));

        // Check for existing direct conversation
        return conversationRepository
                .findDirectConversation(caller.getId(), recipientId)
                .map(existing -> {
                    log.debug("Reusing existing direct conversation {} between {} and {}",
                            existing.getId(), caller.getUsername(), recipient.getUsername());
                    return ConversationDtos.ConversationResponse.from(existing);
                })
                .orElseGet(() -> {
                    Conversation conversation = Conversation.builder()
                            .type(ConversationType.DIRECT)
                            .createdBy(caller)
                            .build();
                    conversation = conversationRepository.saveAndFlush(conversation);

                    conversation.getMembers().add(ConversationMember.of(conversation, caller));
                    conversation.getMembers().add(ConversationMember.of(conversation, recipient));
                    conversationRepository.saveAndFlush(conversation);

                    // Re-fetch to ensure all DB-generated fields (created_at) are populated
                    Conversation saved = conversationRepository.findById(conversation.getId()).orElseThrow();
                    log.info("Created direct conversation {} between {} and {}",
                            saved.getId(), caller.getUsername(), recipient.getUsername());
                    return ConversationDtos.ConversationResponse.from(saved);
                });
    }

    /**
     * Creates a new group conversation. Caller is automatically added as a member.
     */
    @Transactional
    public ConversationDtos.ConversationResponse createGroup(
            User caller, ConversationDtos.CreateGroupRequest request) {

        List<UUID> memberIds = new ArrayList<>(request.members());
        // Ensure caller is always included
        if (!memberIds.contains(caller.getId())) {
            memberIds.add(caller.getId());
        }

        List<User> members = userRepository.findAllById(memberIds);
        if (members.size() != memberIds.size()) {
            throw new ResourceNotFoundException("One or more users not found");
        }

        Conversation conversation = Conversation.builder()
                .type(ConversationType.GROUP)
                .name(request.name())
                .createdBy(caller)
                .build();
        conversation = conversationRepository.saveAndFlush(conversation);

        for (User member : members) {
            conversation.getMembers().add(ConversationMember.of(conversation, member));
        }
        conversationRepository.saveAndFlush(conversation);

        // Re-fetch to get DB-generated timestamps
        Conversation saved = conversationRepository.findById(conversation.getId()).orElseThrow();
        log.info("Created group conversation '{}' ({}) with {} members",
                saved.getName(), saved.getId(), members.size());
        return ConversationDtos.ConversationResponse.from(saved);
    }

    /**
     * Lists all conversations the caller belongs to.
     */
    @Transactional(readOnly = true)
    public List<ConversationDtos.ConversationResponse> listForUser(User caller) {
        return conversationRepository.findAllByMemberUserId(caller.getId())
                .stream()
                .map(ConversationDtos.ConversationResponse::from)
                .toList();
    }

    /**
     * Validates that a user is a member of the given conversation.
     * Throws ForbiddenException if not.
     */
    public void assertMember(UUID conversationId, UUID userId) {
        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new ForbiddenException("You are not a member of this conversation");
        }
    }

    /**
     * Loads a conversation by ID or throws 404.
     */
    @Transactional(readOnly = true)
    public Conversation getOrThrow(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));
    }
}
