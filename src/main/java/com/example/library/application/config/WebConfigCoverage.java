package com.example.library.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigCoverage implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve JaCoCo report from filesystem
        registry.addResourceHandler("/coverage/**")
                .addResourceLocations("file:/app/static/jacoco/")
                .setCachePeriod(0);
    }
}
