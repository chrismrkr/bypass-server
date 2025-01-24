package com.example.bypass_server.shopping.service.port;

import com.example.bypass_server.shopping.domain.ShoppingPoint;
import jakarta.persistence.LockModeType;

public interface ShoppingPointRepository {
    ShoppingPoint findById(Long memberId);
    ShoppingPoint save(ShoppingPoint shoppingPoint);
    ShoppingPoint findById(Long memberId, LockModeType lockModeType);
}
