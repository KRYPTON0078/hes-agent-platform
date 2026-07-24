package com.hes.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HesServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HesServerApplication.class, args);
    }
}
