package com.example.bypass_server.shopping.facade.port;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.domain.Item;

public interface ItemService {
    Item decreaseItem(Long itemId, int amount);
}
