package com.example.bypass_server.shopping.domain;

import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShoppingPoint {
    private Long memberId;
    private Long point;

    public void spend(int amount) {
        this.point -= amount;
    }
    @Builder
    private ShoppingPoint(Long memberId, Long point) {
        this.memberId = memberId;
        this.point = point;
    }

    public static ShoppingPoint from(ShoppingPointEntity entity) {
        return ShoppingPoint.builder()
                .memberId(entity.getMemberId())
                .point(entity.getPoint())
                .build();
    }
    public ShoppingPointEntity toEntity() {
        return ShoppingPointEntity.builder()
                .memberId(this.memberId)
                .point(this.point)
                .build();
    }
}
