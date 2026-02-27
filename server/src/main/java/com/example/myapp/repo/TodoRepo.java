package com.example.myapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myapp.entity.TodoUser;

public interface TodoRepo extends JpaRepository<TodoUser, Long> {
}