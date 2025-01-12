package com.example.bypass_server.queueService.subscriber.port;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;

public interface DeferredResultHolderReader<T> {
    void delete(Long requestId);
    Optional<DeferredResult<T>> get(Long requestId);
}
