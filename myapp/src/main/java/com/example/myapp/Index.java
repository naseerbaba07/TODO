package com.example.myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dto.D;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class Index {
    @Autowired 
    public D d;
    
    @GetMapping("/")
    public D m() {
        return d;
    }

    @GetMapping("/pay")
    public ResponseEntity<D> m2(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(d);
    }
    }
    
    

