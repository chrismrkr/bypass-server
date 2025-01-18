package com.example.bypass_server.queueService.subscriber.port;

import java.lang.reflect.InvocationTargetException;

public interface ApplicationServiceExecutor {
    Object execute(Object target, String methodName, Object... parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    Object execute(String beanName, String methodName, Object... parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
