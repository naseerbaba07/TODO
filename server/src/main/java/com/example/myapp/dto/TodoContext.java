package com.example.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoContext {

    private Long id;
    private String workname;
    private Boolean completed;
    private String dueDate;
    private String priority;
}