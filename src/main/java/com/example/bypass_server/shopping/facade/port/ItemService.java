package com.example.bypass_server.shopping.facade.port;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.domain.Item;
import jakarta.persistence.LockModeType;

public interface ItemService {
    Item decreaseItem(Long itemId, int amount, LockModeType lockModeType);
    Item decreaseItem(Long itemId, int amount);
}
