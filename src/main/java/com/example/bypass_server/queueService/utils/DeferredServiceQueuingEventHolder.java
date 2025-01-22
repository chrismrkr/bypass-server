package com.example.bypass_server.queueService.utils;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DeferredServiceQueuingEventHolder<T> {
    private final ConcurrentHashMap<Long, DeferredResult<T>> deferredResultMap = new ConcurrentHashMap<>();

    public void save(Long requestId, DeferredResult<T> deferredResult) {
        deferredResultMap.put(requestId, deferredResult);
    }

    public void delete(Long requestId) {
        deferredResultMap.remove(requestId);
    }

    public Optional<DeferredResult<T>> get(Long requestId) {
        DeferredResult<T> deferredResult = deferredResultMap.get(requestId);
        if(deferredResult == null) {
            return Optional.empty();
        } else {
            return Optional.of(deferredResult);
        }
    }
}
