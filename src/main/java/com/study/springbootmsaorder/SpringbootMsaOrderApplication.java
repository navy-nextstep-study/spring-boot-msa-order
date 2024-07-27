package com.study.springbootmsaorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootMsaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMsaOrderApplication.class, args);
    }

}
