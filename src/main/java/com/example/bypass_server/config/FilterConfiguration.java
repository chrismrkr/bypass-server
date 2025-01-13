package com.example.bypass_server.config;

import com.example.bypass_server.duplicateCheck.filter.RequestValidCheckFilter;
import com.example.bypass_server.duplicateCheck.filter.port.ValidUrlReadService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {
    private final ValidUrlReadService validUrlReadService;
    private final Environment env;
    @Bean
    public RequestValidCheckFilter requestValidCheckFilter() {
        RequestValidCheckFilter requestValidCheckFilter = new RequestValidCheckFilter(validUrlReadService, env);
        return requestValidCheckFilter;
    }
    @Bean
    public FilterRegistrationBean requestValidCheckFilterRegistration() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(requestValidCheckFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
