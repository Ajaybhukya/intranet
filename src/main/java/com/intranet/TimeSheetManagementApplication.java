package com.intranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // Add this import

@SpringBootApplication
@EnableFeignClients // Add this annotation to enable Feign clients
public class TimeSheetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeSheetManagementApplication.class, args);
    }
}
