package com.example.bypass_server.queueService.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceQueuingDetails {
    private Long requestId;
    private Object target;
    private String method;
    private Object[] parameters;
    @Builder
    private ServiceQueuingDetails(Long requestId, Object target, String method, Object... parameters) {
        this.requestId = requestId;
        this.target = target;
        this.method = method;
        this.parameters = parameters;
    }
}
