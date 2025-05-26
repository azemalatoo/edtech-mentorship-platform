package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.services.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Create a new conversation",
            description = "Starts a conversation between a mentor and a mentee."
    )
    @ApiResponse(responseCode = "200", description = "Conversation created")
    @PostMapping
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<Conversation> create(@RequestParam Long mentorId, @RequestParam Long menteeId) {
        return ResponseEntity.ok(conversationService.createConversation(mentorId, menteeId));
    }

    @Operation(
            summary = "List conversations for a user",
            description = "Retrieves all active conversations for the given mentor or mentee."
    )
    @ApiResponse(responseCode = "200", description = "List retrieved")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<List<Conversation>> listForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
    }

    @Operation(
            summary = "Close a conversation",
            description = "Marks a conversation as closed/inactive."
    )
    @ApiResponse(responseCode = "200", description = "Conversation closed")
    @PostMapping("/{conversationId}/close")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseEntity<Conversation> close(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.closeConversation(conversationId));
    }
}
