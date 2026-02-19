package com.example.myapp.dto;



import java.time.LocalDate;

import lombok.Data;

@Data
public class Usertodo {

    private String workname;
    private Boolean work;
    private LocalDate dueDate;
    private String priority;
}
