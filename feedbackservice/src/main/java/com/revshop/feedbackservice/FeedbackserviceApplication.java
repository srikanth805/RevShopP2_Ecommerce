package com.revshop.feedbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FeedbackserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackserviceApplication.class, args);
	}

}
