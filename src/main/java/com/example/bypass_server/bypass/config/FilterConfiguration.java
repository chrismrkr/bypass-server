package com.example.bypass_server.bypass.config;

import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.filter.RequestValidCheckFilter;
import com.example.bypass_server.bypass.filter.port.ValidUrlReadService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {
    private final ValidUrlReadService validUrlReadService;
    @Bean
    public RequestValidCheckFilter requestValidCheckFilter() {
        RequestValidCheckFilter requestValidCheckFilter = new RequestValidCheckFilter(validUrlReadService);
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
