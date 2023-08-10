package com.bid.idearush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class IdeaRushApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeaRushApplication.class, args);
	}

}
