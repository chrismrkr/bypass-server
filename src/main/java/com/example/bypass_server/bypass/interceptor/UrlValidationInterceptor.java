package com.example.bypass_server.bypass.interceptor;

import com.example.bypass_server.bypass.domain.ValidationType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class UrlValidationInterceptor implements AsyncHandlerInterceptor {
    private final RequestDistributedLockStorage distributedLockStorage;
    private final Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestTypeKey = env.getProperty("spring.application.request.type-name");
        Object requestType = request.getAttribute(requestTypeKey);
        if (requestType != null && requestType instanceof ValidationType) {
            String tokenHeader = env.getProperty("spring.application.jwt.header-name");
            String token = request.getHeader(tokenHeader);
            String requestURI = request.getRequestURI();
            if (distributedLockStorage.isLocked(token, requestURI)) {
                return false;
            }
            distributedLockStorage.setLock(token, requestURI);
        }
        return true;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String requestTypeKey = env.getProperty("spring.application.request.type-name");
        Object requestType = request.getAttribute(requestTypeKey);
        if (requestType != null && requestType instanceof ValidationType) {
            String tokenHeader = env.getProperty("spring.application.jwt.header-name");
            String token = request.getHeader(tokenHeader);
            String requestURI = request.getRequestURI();
            distributedLockStorage.releaseLock(token, requestURI);
        }
        return;
    }
}
