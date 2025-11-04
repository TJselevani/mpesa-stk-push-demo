package com.example.mpesa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MpesaStkPushDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpesaStkPushDemoApplication.class, args);
	}

}
