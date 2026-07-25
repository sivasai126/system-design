package com.chatapp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Fetch conversation history ordered oldest-first, with a sane cap.
     * Frontend can page backwards using a before-timestamp cursor later.
     */
    @Query("""
            SELECT m FROM Message m
            WHERE m.conversation.id = :conversationId
            ORDER BY m.createdAt ASC
            """)
    List<Message> findByConversationId(@Param("conversationId") UUID conversationId);

    /**
     * Undelivered messages sent to conversations the reconnecting user belongs to.
     * Used for offline delivery on reconnect.
     */
    @Query("""
            SELECT m FROM Message m
            JOIN ConversationMember cm
              ON cm.conversation = m.conversation AND cm.user.id = :userId
            WHERE m.delivered = false
              AND m.sender.id <> :userId
            ORDER BY m.createdAt ASC
            """)
    List<Message> findUndeliveredForUser(@Param("userId") UUID userId);

    /**
     * Bulk-mark messages as delivered.
     */
    @Modifying
    @Query("UPDATE Message m SET m.delivered = true WHERE m.id IN :ids")
    void markDelivered(@Param("ids") List<UUID> ids);
}
