package com.example.bypass_server.shopping.service;

import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.facade.port.ItemService;
import com.example.bypass_server.shopping.service.port.ItemRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Item decreaseItem(Long itemId, int amount) {
        Item item = itemRepository.findById(itemId, LockModeType.PESSIMISTIC_WRITE);
        if(item.getStock() < amount) {
            throw new IllegalArgumentException("Not Enough Stock");
        }
        item.give(amount);
        return itemRepository.save(item);
    }
}
