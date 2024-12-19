package com.example.bypass_server.bypass.domain;

import com.example.bypass_server.bypass.infrastructure.entity.ValidationUrlJpaEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationUrl {
    private Long id;
    private ValidationType validationType;
    private String url;

    @Builder
    public ValidationUrl(Long id, ValidationType validationType, String url) {
        this.id = id;
        this.validationType = validationType;
        this.url = url;
    }

    public ValidationUrlJpaEntity toJpaEntity() {
        return ValidationUrlJpaEntity
                .builder()
                .id(this.id)
                .validationType(this.validationType.name())
                .url(this.url)
                .build();
    }

    public static ValidationUrl fromJpaEntity(ValidationUrlJpaEntity entity) {
        return ValidationUrl.builder()
                .id(entity.getId())
                .validationType(ValidationType.valueOf(entity.getValidationType()))
                .url(entity.getUrl())
                .build();
    }

}
