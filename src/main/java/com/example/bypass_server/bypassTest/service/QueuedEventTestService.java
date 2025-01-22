package com.example.bypass_server.bypassTest.service;

import com.example.bypass_server.bypassTest.controller.BypassTestController;
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
    private final ServiceQueuingManager<BypassTestController.BypassTestResponseDTO> serviceQueuingManager;
    private final DeferredServiceQueuingEventHolder<BypassTestController.BypassTestResponseDTO> deferredServiceQueuingEventHolder;

    public DeferredResult<BypassTestController.BypassTestResponseDTO> doTest(String clientUniqueKey, String s) {
        DeferredResult<BypassTestController.BypassTestResponseDTO> execute = serviceQueuingManager.execute(response -> {
            Long requestId = response.getRequestId();
            BypassTestController.BypassTestResponseDTO responseDTO = response.getResponse();
            DeferredResult<BypassTestController.BypassTestResponseDTO> deferredResult = deferredServiceQueuingEventHolder.get(requestId)
                    .get();
            deferredResult.setResult(responseDTO);
        }, clientUniqueKey, "queuedEventTestService", "doService", s);
        return execute;
    }

    public BypassTestController.BypassTestResponseDTO doService(String s) {
        return new BypassTestController.BypassTestResponseDTO("success", "[doService Method] " + s);
    }

}
