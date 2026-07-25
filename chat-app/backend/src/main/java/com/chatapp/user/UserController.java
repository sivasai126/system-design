package com.chatapp.user;

import com.chatapp.common.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    /**
     * Search users by username prefix (case-insensitive).
     * Used by the frontend when starting a new direct conversation.
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> search(@RequestParam String q) {
        var caller = securityUtils.currentUser();
        List<UserResponse> results = userRepository
                .findByUsernameContainingIgnoreCase(q)
                .stream()
                .filter(u -> !u.getId().equals(caller.getId())) // exclude self
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(UserResponse.from(securityUtils.currentUser()));
    }
}
