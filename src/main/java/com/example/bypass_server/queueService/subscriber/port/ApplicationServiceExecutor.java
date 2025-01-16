package com.example.bypass_server.queueService.subscriber.port;

import java.lang.reflect.InvocationTargetException;

public interface ApplicationServiceExecutor {
    Object execute(Object target, String method, Object... parameter) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    Object execute(String beanName, String method, Object... parameter) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
