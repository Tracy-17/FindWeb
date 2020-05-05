package com.shu.find;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.shu.find.mapper")
@SpringBootApplication(scanBasePackages = {"com.shu.find"})
@EnableScheduling   //热门话题
public class FindApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindApplication.class, args);
    }

}
