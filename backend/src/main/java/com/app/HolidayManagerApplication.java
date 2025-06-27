package com.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class HolidayManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HolidayManagerApplication.class, args);
    }
}
