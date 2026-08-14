package com.example.myapp.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;

    private final String model;

    public GeminiService(
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.model:gemini-3.5-flash}") String model) {

        this.client = Client.builder()
                .apiKey(apiKey)
                .build();

        this.model = model;
    }

    public String analyze(String userMessage, String todosJson) {

        String prompt = """
        You are BabaList AI Assistant.

        You control a task management application.

        EXISTING TASKS:
        %s

        USER REQUEST:
        "%s"

        IMPORTANT:
        You MUST classify the user's request based on the user's
        actual command.

        ACTION RULES:

        CREATE:
        Use CREATE ONLY when the user explicitly wants to create,
        add, make, or schedule a NEW task.

        Examples:
        "Create a task to study Java"
        "Add a task called Buy groceries"
        "Remind me to study tomorrow"

        DELETE:
        Use DELETE when the user says:
        delete, remove, erase, get rid of, or permanently remove
        an EXISTING task.

        Examples:
        "Delete Study Java"
        "Remove my Java task"
        "Erase task 100"

        COMPLETE:
        Use COMPLETE when the user says:
        complete, finish, done, mark as completed, or mark done.

        Examples:
        "Complete Study Java"
        "Mark Study Java as done"

        UPDATE:
        Use UPDATE when the user wants to change an EXISTING task.

        Examples:
        "Change Study Java priority to high"
        "Rename Study Java to Learn Spring Boot"

        READ:
        Use READ when the user wants to see, list, find, search,
        or know about existing tasks.

        Examples:
        "Show my tasks"
        "What tasks do I have?"
        "Show pending tasks"

        CLEAR_ALL:
        Use CLEAR_ALL ONLY when the user explicitly asks to delete,
        remove, or clear ALL tasks.

        Examples:
        "Delete all tasks"
        "Clear all my tasks"

        CHAT:
        Use CHAT when the request is general conversation or cannot
        be mapped safely to a task operation.

        CRITICAL SAFETY RULES:

        1. NEVER classify "delete", "remove", or "erase" of an
           existing task as CREATE.

        2. NEVER create a new task when the user is asking to
           modify, complete, or delete an existing task.

        3. NEVER invent a todoId.

        4. For DELETE, find the matching existing task and return
           its actual ID.

        5. DELETE must always have:
           "requiresConfirmation": true

        6. CLEAR_ALL must always have:
           "requiresConfirmation": true

        7. CREATE must have:
           "requiresConfirmation": false

        8. If the user says "Delete Study Java" and an existing
           task named "Study Java" exists, the action MUST be DELETE.

        Return ONLY valid JSON.

        JSON FORMAT:

        {
          "action": "CHAT",
          "todoId": null,
          "workname": null,
          "dueDate": null,
          "priority": null,
          "requiresConfirmation": false,
          "reply": "short response"
        }

        Valid actions are ONLY:

        CREATE
        READ
        UPDATE
        COMPLETE
        DELETE
        CLEAR_ALL
        CHAT

        Current date:
        %s
        """.formatted(
        todosJson,
        userMessage,
        java.time.LocalDate.now()
);

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .temperature(0.2F)
                        .maxOutputTokens(1000)
                        .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        prompt,
                        config
                );

        return response.text();
    }
}