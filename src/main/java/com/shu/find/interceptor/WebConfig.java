package com.shu.find.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author:ShiQi
 * Date:2019/12/9-16:59
 * 拦截器
 * https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-config-interceptors
 */
@Configuration
//@EnableWebMvc
/*使用 @EnableWebMvc 注解，需要以编程的方式指定视图文件相关配置；
使用 @EnableAutoConfiguration 注解，会读取 application.properties 或 application.yml 文件中的配置。
*这里不用EnableWebMvc，静态资源（css，js）不会被拦截
*/
public class WebConfig implements WebMvcConfigurer {

    @Autowired//让Spring自动初始化
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns：把这些地址拦截；excludePathPatterns：把这些地址略过
//        registry.addInterceptor(new ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        //所有地址都作拦截处理：
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }

/*@Override
加载静态资源：
public void addResourceHandlers(ResourceHandlerRegistry registry) {}
*/
}
