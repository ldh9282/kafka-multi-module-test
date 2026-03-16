package com.kafka01.notifications.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.kafka01.notifications.email",
                "com.kafka01.common"
        }
)
public class NotificationsEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsEmailApplication.class, args);
    }

}
