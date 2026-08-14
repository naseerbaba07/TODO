package com.example.myapp.service;

import com.example.myapp.dto.TodoContext;
import com.example.myapp.entity.TodoUser;
import com.example.myapp.repo.TodoRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChatService {

    private final TodoRepo todoRepo;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public ChatService(
            TodoRepo todoRepo,
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.todoRepo = todoRepo;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    public ChatResult process(String userMessage) {

        try {

            // ==========================================
            // 1. Get existing tasks
            // ==========================================

            List<TodoUser> todos = todoRepo.findAll();

            // ==========================================
            // 2. Convert entities to Gemini-safe DTOs
            // ==========================================

            List<TodoContext> context = todos.stream()
                    .map(todo -> new TodoContext(
                            todo.getId(),
                            todo.getWorkname(),
                            todo.getWork(),
                            todo.getDueDate() != null
                                    ? todo.getDueDate().toString()
                                    : null,
                            todo.getPriority()
                    ))
                    .toList();

            String todosJson =
                    objectMapper.writeValueAsString(context);

            System.out.println("================================");
            System.out.println("TODO CONTEXT SENT TO GEMINI:");
            System.out.println(todosJson);
            System.out.println("================================");

            // ==========================================
            // 3. Send request to Gemini
            // ==========================================

            String aiResponse =
                    geminiService.analyze(
                            userMessage,
                            todosJson
                    );

            System.out.println("================================");
            System.out.println("GEMINI RESPONSE:");
            System.out.println(aiResponse);
            System.out.println("================================");

            // ==========================================
            // 4. Clean Gemini JSON
            // ==========================================

            aiResponse = cleanJson(aiResponse);

            JsonNode json =
                    objectMapper.readTree(aiResponse);

            String action =
                    json.path("action")
                            .asText("CHAT")
                            .toUpperCase();

            String reply =
                    json.path("reply")
                            .asText(
                                    "I couldn't understand that request."
                            );

            boolean requiresConfirmation =
                    json.path("requiresConfirmation")
                            .asBoolean(false);

            Long todoId = null;

            if (json.has("todoId")
                    && !json.path("todoId").isNull()) {

                todoId =
                        json.path("todoId").asLong();
            }

            // ==========================================
            // 5. DELETE
            // ==========================================

            if ("DELETE".equals(action)) {

                if (todoId == null) {

                    return new ChatResult(
                            "Which task would you like me to delete?",
                            "CHAT",
                            null,
                            false
                    );
                }

                return new ChatResult(
                        reply,
                        "DELETE",
                        todoId,
                        true
                );
            }

            // ==========================================
            // 6. CLEAR ALL
            // ==========================================

            if ("CLEAR_ALL".equals(action)) {

                return new ChatResult(
                        reply,
                        "CLEAR_ALL",
                        null,
                        true
                );
            }

            // ==========================================
            // 7. CREATE
            // ==========================================

            if ("CREATE".equals(action)) {

                String workname =
                        json.path("workname")
                                .asText(null);

                String priority =
                        json.path("priority")
                                .asText("MEDIUM");

                String dueDateString =
                        json.path("dueDate")
                                .asText(null);

                if (workname == null
                        || workname.isBlank()) {

                    return new ChatResult(
                            "What task would you like me to create?",
                            "CHAT",
                            null,
                            false
                    );
                }

                TodoUser todo =
                        new TodoUser();

                todo.setWorkname(workname);

                todo.setWork(false);

                // ------------------------------------------
                // Due date
                // ------------------------------------------

                if (dueDateString != null
                        && !dueDateString.equals("null")
                        && !dueDateString.isBlank()) {

                    try {

                        todo.setDueDate(
                                LocalDate.parse(
                                        dueDateString
                                )
                        );

                    } catch (Exception e) {

                        return new ChatResult(
                                "I understood the task, but I couldn't understand the date. Please provide the date again.",
                                "CHAT",
                                null,
                                false
                        );
                    }
                }

                // ------------------------------------------
                // Priority
                // ------------------------------------------

                todo.setPriority(
                        normalizePriority(priority)
                );

                TodoUser saved =
                        todoRepo.save(todo);

                return new ChatResult(
                        "Task created successfully: "
                                + saved.getWorkname(),
                        "CREATE",
                        saved.getId(),
                        false
                );
            }

            // ==========================================
            // 8. UPDATE
            // ==========================================

            if ("UPDATE".equals(action)) {

                if (todoId == null) {

                    return new ChatResult(
                            "Which task would you like me to update?",
                            "CHAT",
                            null,
                            false
                    );
                }

                TodoUser todo =
                        todoRepo.findById(todoId)
                                .orElse(null);

                if (todo == null) {

                    return new ChatResult(
                            "I couldn't find that task.",
                            "CHAT",
                            null,
                            false
                    );
                }

                // ------------------------------------------
                // Work name
                // ------------------------------------------

                if (json.has("workname")
                        && !json.path("workname").isNull()) {

                    String workname =
                            json.path("workname")
                                    .asText();

                    if (!workname.isBlank()) {

                        todo.setWorkname(
                                workname
                        );
                    }
                }

                // ------------------------------------------
                // Priority
                // ------------------------------------------

                if (json.has("priority")
                        && !json.path("priority").isNull()) {

                    todo.setPriority(
                            normalizePriority(
                                    json.path("priority")
                                            .asText()
                            )
                    );
                }

                // ------------------------------------------
                // Due date
                // ------------------------------------------

                if (json.has("dueDate")
                        && !json.path("dueDate").isNull()) {

                    String date =
                            json.path("dueDate")
                                    .asText();

                    if (!date.isBlank()) {

                        try {

                            todo.setDueDate(
                                    LocalDate.parse(date)
                            );

                        } catch (Exception e) {

                            return new ChatResult(
                                    "The task was found, but I couldn't understand the new date.",
                                    "CHAT",
                                    null,
                                    false
                            );
                        }
                    }
                }

                todoRepo.save(todo);

                return new ChatResult(
                        "Task updated successfully.",
                        "UPDATE",
                        todo.getId(),
                        false
                );
            }

            // ==========================================
            // 9. COMPLETE
            // ==========================================

            if ("COMPLETE".equals(action)) {

                if (todoId == null) {

                    return new ChatResult(
                            "Which task should I mark as completed?",
                            "CHAT",
                            null,
                            false
                    );
                }

                TodoUser todo =
                        todoRepo.findById(todoId)
                                .orElse(null);

                if (todo == null) {

                    return new ChatResult(
                            "I couldn't find that task.",
                            "CHAT",
                            null,
                            false
                    );
                }

                todo.setWork(true);

                todoRepo.save(todo);

                return new ChatResult(
                        "Task marked as completed.",
                        "COMPLETE",
                        todo.getId(),
                        false
                );
            }

            // ==========================================
            // 10. READ
            // ==========================================

            if ("READ".equals(action)) {

                return new ChatResult(
                        reply,
                        "READ",
                        null,
                        false
                );
            }

            // ==========================================
            // 11. Normal conversation
            // ==========================================

            return new ChatResult(
                    reply,
                    "CHAT",
                    null,
                    requiresConfirmation
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new ChatResult(
                    "Server error: "
                            + e.getClass().getSimpleName()
                            + " - "
                            + e.getMessage(),
                    "ERROR",
                    null,
                    false
            );
        }
    }

    // =====================================================
    // Remove Gemini markdown code fences
    // =====================================================

    private String cleanJson(String response) {

        if (response == null) {
            return "{}";
        }

        response = response.trim();

        if (response.startsWith("```json")) {

            response =
                    response.substring(7);
        }

        if (response.startsWith("```")) {

            response =
                    response.substring(3);
        }

        if (response.endsWith("```")) {

            response =
                    response.substring(
                            0,
                            response.length() - 3
                    );
        }

        return response.trim();
    }

    // =====================================================
    // Normalize priority
    // =====================================================

    private String normalizePriority(
            String priority) {

        if (priority == null) {
            return "MEDIUM";
        }

        String value =
                priority
                        .trim()
                        .toUpperCase();

        if (value.equals("LOW")
                || value.equals("MEDIUM")
                || value.equals("HIGH")) {

            return value;
        }

        return "MEDIUM";
    }

    // =====================================================
    // Response object
    // =====================================================

    public record ChatResult(
            String message,
            String action,
            Long todoId,
            boolean requiresConfirmation
    ) {
    }
}