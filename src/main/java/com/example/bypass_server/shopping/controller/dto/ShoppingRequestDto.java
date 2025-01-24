package com.example.bypass_server.shopping.controller.dto;

import lombok.Data;

@Data
public class ShoppingRequestDto {
    private Long memberId;
    private Long itemId;
    private int amount;
}
