package com.shu.find;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

@EnableAsync
@MapperScan("com.shu.find.mapper")
@SpringBootApplication(scanBasePackages = {"com.shu.find"})
@EnableScheduling   //热门话题
public class FindApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

 // 打包war需要这个启动类，发布到服务器上
 @Override
 protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
    return application.sources(FindApplication.class);
 }
    public static void main(String[] args) {
        SpringApplication.run(FindApplication.class, args);
    }

}
