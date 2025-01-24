package com.example.bypass_server.shopping.service.port;

import com.example.bypass_server.shopping.domain.Item;
import jakarta.persistence.LockModeType;

public interface ItemRepository {
    Item findById(Long itemId);
    Item findById(Long itemId, LockModeType lockModeType);
    Item save(Item item);
}
