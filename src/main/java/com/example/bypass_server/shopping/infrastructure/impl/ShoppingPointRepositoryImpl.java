package com.example.bypass_server.shopping.infrastructure.impl;

import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.infrastructure.ShoppingPointJpaRepository;
import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import com.example.bypass_server.shopping.service.port.ShoppingPointRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ShoppingPointRepositoryImpl implements ShoppingPointRepository {
    private final ShoppingPointJpaRepository jpaRepository;
    private final EntityManager entityManager;
    @Override
    public ShoppingPoint findById(Long memberId) {
        ShoppingPointEntity entity = jpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid MemberId"));
        return ShoppingPoint.from(entity);
    }

    @Override
    public ShoppingPoint save(ShoppingPoint shoppingPoint) {
        ShoppingPointEntity entity = shoppingPoint.toEntity();
        ShoppingPointEntity save = jpaRepository.save(entity);
        return ShoppingPoint.from(save);
    }

    @Override
    @Transactional
    public ShoppingPoint findById(Long memberId, LockModeType lockModeType) {
        ShoppingPointEntity entity = entityManager.find(ShoppingPointEntity.class, memberId, lockModeType);
        if(entity == null) {
            throw new IllegalArgumentException("Invalid Member Id");
        }
        return ShoppingPoint.from(entity);
    }

}
