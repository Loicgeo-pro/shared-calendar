package com.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SharedCalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedCalendarApplication.class, args);
    }

}