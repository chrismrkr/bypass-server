package com.example.bypass_server.shopping.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingPointEntity {
    @Id
    private Long memberId;
    private Long point;

    @Builder
    private ShoppingPointEntity(Long memberId, Long point) {
        this.memberId = memberId;
        this.point = point;
    }
}
