package com.springboot.websocket.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:tangh
 * jdk 1.8 版本后引入默认实现方法后，WebMvcConfigurer中有默认实现方法，所以可以直接实现WebMvcConfigurer 实现自定义配置
 * @since 1.0
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    /**
     * 自定义项目起始欢迎页面
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/websocket.html");
        registry.setOrder(Integer.MIN_VALUE);
    }
}
