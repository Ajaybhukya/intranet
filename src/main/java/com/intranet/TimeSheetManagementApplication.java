package com.intranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages="com.intranet.client")
public class TimeSheetManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeSheetManagementApplication.class, args);
	}

}
