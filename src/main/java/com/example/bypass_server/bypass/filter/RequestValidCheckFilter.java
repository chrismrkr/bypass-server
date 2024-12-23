package com.example.bypass_server.bypass.filter;


import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.filter.port.ValidUrlReadService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class RequestValidCheckFilter implements Filter {
    private final ValidUrlReadService urlReadService;
    private final Environment env;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("URL VALIDATION CHECK");
        if(!(servletRequest instanceof HttpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String requestURI = ((HttpServletRequest) (servletRequest)).getRequestURI();
        Optional<ValidationUrl> validationUrl = urlReadService.readByUrl(requestURI);
        if(validationUrl.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String requestKey = env.getProperty("spring.application.request.type-name");
        String requestType = validationUrl.get().getValidationType().name();
        servletRequest.setAttribute(requestKey, requestType);
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
