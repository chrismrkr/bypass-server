package com.example.bypass_server.queueService.subscriber.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueuedServiceResult<T> {
    private Long requestId;
    private T response;
    private QueuedServiceResult(Builder<T> builder) {
        this.requestId = builder.requestId;
        this.response = builder.response;
    }

    public static class Builder<T> {
        private Long requestId;
        private T response;
        public Builder<T> requestId(Long requestId) {
            this.requestId = requestId;
            return this;
        }
        public Builder<T> response(T response) {
            this.response = response;
            return this;
        }
        public QueuedServiceResult<T> build() {
            return new QueuedServiceResult<T>(this);
        }
    }
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}