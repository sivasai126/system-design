package com.chatapp.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    /**
     * Find all conversations that a given user is a member of,
     * ordered by most recently created first.
     */
    @Query("""
            SELECT c FROM Conversation c
            JOIN c.members m
            WHERE m.user.id = :userId
            ORDER BY c.createdAt DESC
            """)
    List<Conversation> findAllByMemberUserId(@Param("userId") UUID userId);

    /**
     * Find an existing DIRECT conversation shared between exactly two users.
     * This prevents duplicate direct conversations being created.
     */
    @Query("""
            SELECT c FROM Conversation c
            WHERE c.type = 'DIRECT'
            AND EXISTS (
                SELECT 1 FROM ConversationMember m1
                WHERE m1.conversation = c AND m1.user.id = :userAId
            )
            AND EXISTS (
                SELECT 1 FROM ConversationMember m2
                WHERE m2.conversation = c AND m2.user.id = :userBId
            )
            """)
    Optional<Conversation> findDirectConversation(
            @Param("userAId") UUID userAId,
            @Param("userBId") UUID userBId
    );
}
