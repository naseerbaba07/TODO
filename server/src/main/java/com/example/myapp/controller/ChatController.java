package com.example.myapp.controller;

import com.example.myapp.dto.ChatRequest;
import com.example.myapp.dto.ChatResponse;
import com.example.myapp.service.ChatService;
import com.example.myapp.repo.TodoRepo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final TodoRepo todoRepo;

    public ChatController(
            ChatService chatService,
            TodoRepo todoRepo) {

        this.chatService = chatService;
        this.todoRepo = todoRepo;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request) {

        if (request.getMessage() == null
                || request.getMessage().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(
                            new ChatResponse(
                                    "Please enter a message.",
                                    "ERROR",
                                    null,
                                    false
                            )
                    );
        }

        ChatService.ChatResult result =
                chatService.process(
                        request.getMessage()
                );

        return ResponseEntity.ok(
                new ChatResponse(
                        result.message(),
                        result.action(),
                        result.todoId(),
                        result.requiresConfirmation()
                )
        );
    }

    /*
     * Confirm DELETE.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ChatResponse> delete(
            @PathVariable Long id) {

        if (!todoRepo.existsById(id)) {

            return ResponseEntity.notFound().build();
        }

        todoRepo.deleteById(id);

        return ResponseEntity.ok(
                new ChatResponse(
                        "Task deleted successfully.",
                        "DELETE",
                        id,
                        false
                )
        );
    }

    /*
     * Confirm CLEAR_ALL.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ChatResponse> clearAll() {

        todoRepo.deleteAll();

        return ResponseEntity.ok(
                new ChatResponse(
                        "All tasks have been deleted.",
                        "CLEAR_ALL",
                        null,
                        false
                )
        );
    }
}