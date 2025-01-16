package com.example.bypass_server.queueService.subscriber.executor;

import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultApplicationServiceExecutor implements ApplicationServiceExecutor {
    private final ApplicationContext applicationContext;

    @Override
    public Object execute(Object target, String methodName, Object... parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(parameters == null || parameters.length == 0) {
            return this.execute(target, methodName);
        }

        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for(int i=0; i<parameters.length; i++) {
            if (parameters[i] == null) {
                throw new IllegalArgumentException("Cannot infer type for null parameter at index " + i);
            }
            parameterTypes[i] = parameters[i].getClass();
        }

        Method targetMethod = null;
        try {
            targetMethod = target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Method[] methods = target.getClass().getMethods();
            for(Method method : methods) {
                if(method.getName().equals(methodName) && parameters.length == method.getParameterTypes().length) {
                    // TODO. primitive - Wrapper
                }
            }
        }
        return targetMethod.invoke(target, parameters);
    }

    private Object execute(Object target, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method targetMethod = null;
        targetMethod = target.getClass().getMethod(methodName);
        return targetMethod.invoke(target);
    }

    @Override
    public Object execute(String beanName, String method, Object... parameter) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return null;
    }

    private Class<?> resolvePrimitiveType(Class<?> clazz) {
        return WRAPPER_PRIMITIVE_MAP.getOrDefault(clazz, clazz);
    }
    private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = Map.of(
            Integer.class, int.class,
            Double.class, double.class,
            Boolean.class, boolean.class,
            Character.class, char.class,
            Long.class, long.class,
            Float.class, float.class,
            Short.class, short.class,
            Byte.class, byte.class
    );
}
