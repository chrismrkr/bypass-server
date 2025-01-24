package com.example.bypass_server.shopping.controller.port;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;

public interface ShoppingFacade {
    ShoppingResponseDto buy(Long memberId, Long itemId, int amount);
}
