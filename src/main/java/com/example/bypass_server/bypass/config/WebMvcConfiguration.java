package com.example.bypass_server.bypass.config;

import com.example.bypass_server.bypass.interceptor.UrlValidationInterceptor;
import com.example.bypass_server.bypass.interceptor.RequestDistributedLockStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final Environment env;
    private final RequestDistributedLockStorage lockStorage;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UrlValidationInterceptor(lockStorage, env))
                .addPathPatterns("/**");

    }
}
