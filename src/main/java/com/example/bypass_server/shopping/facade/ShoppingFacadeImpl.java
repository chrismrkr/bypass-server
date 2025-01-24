package com.example.bypass_server.shopping.facade;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.controller.port.ShoppingFacade;
import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.facade.port.ItemService;
import com.example.bypass_server.shopping.facade.port.ShoppingPointService;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingFacadeImpl implements ShoppingFacade {
    private final ItemService itemService;
    private final ShoppingPointService shoppingPointService;

    @Override
    @Transactional
    public ShoppingResponseDto buy(Long memberId, Long itemId, int amount) {
        try {
            Item item = itemService.decreaseItem(itemId, amount);
            ShoppingPoint remains = shoppingPointService.decreasePoint(memberId, item.getPrice() * amount);
            return ShoppingResponseDto.builder()
                    .status("S")
                    .message(item.getItemName())
                    .totalPrice((long)amount * (long)item.getPrice())
                    .build();
        } catch (Exception e) {
            return ShoppingResponseDto.builder()
                    .status("F")
                    .message(e.getMessage())
                    .totalPrice(0L)
                    .build();
        }
    }
}
