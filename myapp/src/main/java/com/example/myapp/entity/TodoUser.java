package com.example.myapp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "todos")
@Data
public class TodoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   

    @Column(nullable = false)
    private String workname;

    @Column(nullable = false)
    private Boolean work=false;
    
    @Column(name = "work_date")
    private LocalDate dueDate;

    @Column(nullable = false)
   private String priority = "MEDIUM";
}