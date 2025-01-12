package com.example.bypass_server.duplicateCheck.filter;


import com.example.bypass_server.duplicateCheck.domain.ValidationUrl;
import com.example.bypass_server.duplicateCheck.filter.port.ValidUrlReadService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

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
        servletRequest.setAttribute(requestKey, validationUrl.get().getValidationType());
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
