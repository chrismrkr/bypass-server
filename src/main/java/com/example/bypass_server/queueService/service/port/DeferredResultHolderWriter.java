package com.example.bypass_server.queueService.service.port;

import org.springframework.web.context.request.async.DeferredResult;

public interface DeferredResultHolderWriter<T> {
    void save(Long requestId, DeferredResult<T> deferredResult);
}
