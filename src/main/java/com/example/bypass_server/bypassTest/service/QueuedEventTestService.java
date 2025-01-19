package com.example.bypass_server.bypassTest.service;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service("queuedEventTestService")
@RequiredArgsConstructor
public class QueuedEventTestService {
    private final ServiceQueuingManager serviceQueuingManager;
    private final DeferredServiceQueuingEventHolder deferredServiceQueuingEventHolder;
    public DeferredResult<String> testMethod(String clientUniqueKey, String s) {
        DeferredResult<ServiceQueuingDetails> execute = serviceQueuingManager.execute(result -> {

        }, clientUniqueKey, "queuedEventTestService", "testMethod", s);
        return null;
    }

    public String doService(String s) {
        return null;
    }

}
