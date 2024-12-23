package com.example.bypass_server.bypass.infrastructure.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationUrlCacheEntity {
    private Long id;
    private String url;
    private String validationType;
    @Builder
    private ValidationUrlCacheEntity(Long id, String url, String validationType) {
        this.id = id;
        this.url = url;
        this.validationType = validationType;
    }
}
