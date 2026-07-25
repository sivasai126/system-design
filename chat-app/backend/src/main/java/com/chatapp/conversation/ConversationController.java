package com.chatapp.conversation;

import com.chatapp.common.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final SecurityUtils securityUtils;

    @PostMapping("/direct")
    public ResponseEntity<ConversationDtos.ConversationResponse> createDirect(
            @Valid @RequestBody ConversationDtos.CreateDirectRequest request) {
        var caller = securityUtils.currentUser();
        var response = conversationService.getOrCreateDirect(caller, request.recipientId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/group")
    public ResponseEntity<ConversationDtos.ConversationResponse> createGroup(
            @Valid @RequestBody ConversationDtos.CreateGroupRequest request) {
        var caller = securityUtils.currentUser();
        var response = conversationService.createGroup(caller, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConversationDtos.ConversationResponse>> list() {
        var caller = securityUtils.currentUser();
        return ResponseEntity.ok(conversationService.listForUser(caller));
    }
}
