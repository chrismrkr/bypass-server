package com.example.bypass_server.shopping.controller.dto;

import lombok.*;

@NoArgsConstructor
@Getter
public class ShoppingRequestDto {
    private Long memberId;
    private Long itemId;
    private int amount;

    @Builder
    public ShoppingRequestDto(Long memberId, Long itemId, int amount) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.amount = amount;
    }
}
