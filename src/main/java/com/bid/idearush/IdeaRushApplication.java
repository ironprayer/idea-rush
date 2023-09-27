package com.bid.idearush;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableSchedulerLock(defaultLockAtMostFor = "50s")
public class IdeaRushApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeaRushApplication.class, args);
	}

}
