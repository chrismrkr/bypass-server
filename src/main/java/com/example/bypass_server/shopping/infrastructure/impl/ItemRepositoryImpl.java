package com.example.bypass_server.shopping.infrastructure.impl;

import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.infrastructure.ItemJpaRepository;
import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import com.example.bypass_server.shopping.service.port.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemJpaRepository jpaRepository;
    private final EntityManager entityManager;
    @Override
    public Item findById(Long itemId) {
        ItemEntity entity = jpaRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item Not Found"));
        return Item.from(entity);
    }

    @Override
    @Transactional
    public Item findById(Long itemId, LockModeType lockModeType) {
        ItemEntity entity = entityManager.find(ItemEntity.class, itemId, lockModeType);
        if(entity == null) {
            throw new IllegalArgumentException("Item Not Found");
        }
        return Item.from(entity);
    }

    @Override
    public Item save(Item item) {
        ItemEntity entity = item.toEntity();
        ItemEntity save = jpaRepository.save(entity);
        return Item.from(save);
    }
}
