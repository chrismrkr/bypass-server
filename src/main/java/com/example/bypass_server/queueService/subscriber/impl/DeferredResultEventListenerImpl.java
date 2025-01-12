package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.service.port.DeferredResultEventListener;
import com.example.bypass_server.queueService.subscriber.AbstractDeferredResultEventListener;
import com.example.bypass_server.queueService.subscriber.port.DeferredResultHolderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class DeferredResultEventListenerImpl extends AbstractDeferredResultEventListener implements DeferredResultEventListener {
    public DeferredResultEventListenerImpl(DeferredResultHolderReader<ServiceQueuingDetails> deferredResultHolderReader) {
        super(deferredResultHolderReader);
    }

    public void handle(DeferredResult<ServiceQueuingDetails> deferredResult) {

    }
}
