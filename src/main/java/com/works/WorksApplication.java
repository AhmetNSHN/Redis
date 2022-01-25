package com.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan("com.works.restcontrollers")
@EnableJpaAuditing
@SpringBootApplication
public class WorksApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorksApplication.class, args);
    }

}
