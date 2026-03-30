package com.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication//扫描所在包及子包下的所有组件，这个包含@ComponentScan
public class StudentmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentmanagementApplication.class, args);
    }

}
