package com.chatapp.conversation;

import com.chatapp.user.UserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ConversationDtos {

    public record CreateDirectRequest(
            @NotNull(message = "recipientId is required")
            UUID recipientId
    ) {}

    public record CreateGroupRequest(
            @NotBlank(message = "Group name is required")
            @Size(min = 1, max = 100, message = "Group name must be 1-100 characters")
            String name,

            @NotEmpty(message = "At least one member is required")
            List<UUID> members
    ) {}

    public record ConversationResponse(
            UUID id,
            String type,
            String name,
            List<UserResponse> members,
            Instant createdAt
    ) {
        public static ConversationResponse from(Conversation conversation) {
            List<UserResponse> memberResponses = conversation.getMembers().stream()
                    .map(m -> UserResponse.from(m.getUser()))
                    .toList();

            return new ConversationResponse(
                    conversation.getId(),
                    conversation.getType().name(),
                    conversation.getName(),
                    memberResponses,
                    conversation.getCreatedAt()
            );
        }
    }
}
