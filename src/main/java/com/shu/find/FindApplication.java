package com.shu.find;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shu.find.mapper")
@SpringBootApplication(scanBasePackages = {"com.shu.find"})
public class FindApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindApplication.class, args);
    }

}
