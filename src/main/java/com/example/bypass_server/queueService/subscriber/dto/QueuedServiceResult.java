package com.example.bypass_server.queueService.subscriber.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueuedServiceResult {
    private Long requestId;
    private Object response;
    @Builder
    private QueuedServiceResult(Long requestId, Object response) {
        this.requestId = requestId;
        this.response = response;
    }
}
