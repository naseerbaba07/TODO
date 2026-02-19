package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.myapp.dto.Usertodo;
import com.example.myapp.entity.TodoUser;
import com.example.myapp.repo.TodoRepo;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoRepo todoRepo;

   
   @PostMapping("/save")
public ResponseEntity<TodoUser> save(@RequestBody Usertodo dto) {

    TodoUser todo = new TodoUser();
    todo.setWorkname(dto.getWorkname());
    todo.setWork(false);
    todo.setDueDate(dto.getDueDate()); 
    todo.setPriority(
    dto.getPriority() != null ? dto.getPriority() : "MEDIUM"
);

    TodoUser saved = todoRepo.save(todo);
    return ResponseEntity.ok(saved);
}

   
    @GetMapping
    public List<TodoUser> getAllTodos() {
        return todoRepo.findAll();
    }

    // UPDATE (Toggle complete)
    @PutMapping("/{id}")
public ResponseEntity<TodoUser> updateTodo(
        @PathVariable Long id,
        @RequestBody Usertodo dto) {

    return todoRepo.findById(id).map(existing -> {

        if(dto.getWorkname() != null)
            existing.setWorkname(dto.getWorkname());

        if(dto.getWork() != null)
            existing.setWork(dto.getWork());

        if(dto.getDueDate() != null)
            existing.setDueDate(dto.getDueDate());

        if(dto.getPriority() != null)
       existing.setPriority(dto.getPriority());

        return ResponseEntity.ok(todoRepo.save(existing));

    }).orElse(ResponseEntity.notFound().build());
}

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        todoRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}