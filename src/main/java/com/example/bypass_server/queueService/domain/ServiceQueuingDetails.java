package com.example.bypass_server.queueService.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class ServiceQueuingDetails {
    private Long requestId;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private Object target;
    private String method;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private Object[] parameters;
    @Builder
    private ServiceQueuingDetails(Long requestId, Object target, String method, Object... parameters) {
        this.requestId = requestId;
        this.target = target;
        this.method = method;
        this.parameters = parameters;
    }
}
