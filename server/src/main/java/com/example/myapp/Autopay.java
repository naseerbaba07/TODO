package com.example.myapp;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Autopay {
    @Scheduled(fixedRate=5000)
    public void pay(){
        System.out.println("fixed rate");
    }
    @Scheduled(cron = "5 * * * * *")
    public void pay2(){
        System.out.println("cron");
    }
}
