package com.chatapp.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, ConversationMemberId> {

    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);
}
