package com.chatapp.message;

import com.chatapp.user.UserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public class MessageDtos {

    public record SendMessageRequest(
            @NotNull(message = "conversationId is required")
            UUID conversationId,

            @NotBlank(message = "Message content is required")
            @Size(max = 10000, message = "Message too long")
            String content
    ) {}

    public record MessageResponse(
            UUID id,
            UUID conversationId,
            UserResponse sender,
            String content,
            Instant createdAt,
            boolean delivered
    ) {
        public static MessageResponse from(Message message) {
            return new MessageResponse(
                    message.getId(),
                    message.getConversation().getId(),
                    UserResponse.from(message.getSender()),
                    message.getContent(),
                    message.getCreatedAt(),
                    message.isDelivered()
            );
        }
    }
}
