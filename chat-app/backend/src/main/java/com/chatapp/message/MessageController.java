package com.chatapp.message;

import com.chatapp.common.SecurityUtils;
import com.chatapp.websocket.WebSocketDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final WebSocketDeliveryService wsDeliveryService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<MessageDtos.MessageResponse> send(
            @Valid @RequestBody MessageDtos.SendMessageRequest request) {
        var sender = securityUtils.currentUser();

        // 1. Persist (own transaction)
        Message message = messageService.sendMessage(sender, request);

        // 2. Push to online recipients (own transaction, non-blocking)
        //    If delivery fails, the message is already persisted and will be
        //    delivered on next reconnect via OfflineMessageDeliveryService.
        wsDeliveryService.deliverNewMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageDtos.MessageResponse.from(message));
    }

    @GetMapping
    public ResponseEntity<List<MessageDtos.MessageResponse>> history(
            @RequestParam UUID conversationId) {
        var caller = securityUtils.currentUser();
        return ResponseEntity.ok(messageService.getHistory(caller, conversationId));
    }
}
