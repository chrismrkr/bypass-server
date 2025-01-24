package com.example.bypass_server.shopping.controller;

import com.example.bypass_server.shopping.controller.dto.ShoppingRequestDto;
import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.controller.port.ShoppingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ShoppingController {
    private final ShoppingFacade shoppingFacade;
    @PatchMapping("/item")
    public ShoppingResponseDto handleShopping(@RequestBody ShoppingRequestDto dto) {
        return shoppingFacade.buy(dto.getMemberId(), dto.getItemId(), dto.getAmount());
    }
}
