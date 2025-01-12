package com.example.bypass_server.queueService.subscriber;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.port.DeferredResultHolderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractDeferredResultEventListener implements MessageListener {
    private final DeferredResultHolderReader<ServiceQueuingDetails> deferredResultHolderReader;
    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        String requestId = new String(message.getChannel());
        String payload = new String(message.getBody());
        Optional<DeferredResult<ServiceQueuingDetails>> deferredResult = deferredResultHolderReader.get(Long.valueOf(requestId));
        deferredResult.ifPresent(this::handle);
    }
    public void subscribeToChannel(String requestId) {
        ChannelTopic channelTopic = new ChannelTopic(requestId);

    }

    protected void handle(DeferredResult<ServiceQueuingDetails> deferredResult) {}
}
