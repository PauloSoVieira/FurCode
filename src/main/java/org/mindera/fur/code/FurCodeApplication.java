package org.mindera.fur.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class FurCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurCodeApplication.class, args);
    }
}
