package com.example.bypass_server.shopping.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingResponseDto {
    private String status;
    private String message;
    private Long totalPrice;

    @Builder
    private ShoppingResponseDto(String status, String message, Long totalPrice) {
        this.status = status;
        this.message = message;
        this.totalPrice = totalPrice;
    }
}
