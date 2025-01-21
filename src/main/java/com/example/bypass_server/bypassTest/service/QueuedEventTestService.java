package com.example.bypass_server.bypassTest.service;

import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service("queuedEventTestService")
@RequiredArgsConstructor
@Slf4j
public class QueuedEventTestService {
    private final ServiceQueuingManager serviceQueuingManager;
    private final DeferredServiceQueuingEventHolder deferredServiceQueuingEventHolder;

    public DeferredResult<Object> doTest(String clientUniqueKey, String s) {
        DeferredResult<Object> execute = serviceQueuingManager.execute(result -> {
            DeferredResult<Object> objectDeferredResult = deferredServiceQueuingEventHolder.get(result.getRequestId())
                    .get();
            objectDeferredResult.setResult(result.getResponse());
        }, clientUniqueKey, "queuedEventTestService", "doService", s);
        return execute;
    }

    public String doService(String s) {
        return "[doService Method] " + s;
    }

}
