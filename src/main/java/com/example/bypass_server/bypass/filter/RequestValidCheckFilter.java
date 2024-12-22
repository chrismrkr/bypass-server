package com.example.bypass_server.bypass.filter;


import com.example.bypass_server.bypass.filter.port.ValidUrlReadService;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class RequestValidCheckFilter implements Filter {
    private final ValidUrlReadService urlReadService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("URL VALIDATION CHECK");
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
