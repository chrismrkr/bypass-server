package com.example.bypass_server.duplicateCheck.infrastructure.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationUrlJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String validationType;

    @Builder
    public ValidationUrlJpaEntity(Long id, String url, String validationType) {
        this.id = id;
        this.url = url;
        this.validationType = validationType;
    }
}
