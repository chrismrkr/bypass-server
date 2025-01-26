package com.example.bypass_server.shopping.controller.port;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import org.springframework.web.context.request.async.DeferredResult;

public interface ShoppingFacade {
    ShoppingResponseDto buy(Long memberId, Long itemId, int amount, boolean pessimisticLock);
    DeferredResult<ShoppingResponseDto> buyEvent(Long memberId, Long itemId, int amount);
}
