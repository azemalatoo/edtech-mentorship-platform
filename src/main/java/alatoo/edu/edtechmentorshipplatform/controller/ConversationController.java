package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.services.ConversationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
@Tag(name = "ConversationController", description = "APIs for managing mentor–mentee conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<Conversation> create(@RequestParam Long mentorId, @RequestParam Long menteeId) {
        return ResponseEntity.ok(conversationService.createConversation(mentorId, menteeId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<List<Conversation>> listForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
    }

    @PostMapping("/{conversationId}/close")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<Conversation> close(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.closeConversation(conversationId));
    }
}
