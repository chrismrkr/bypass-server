package com.example.bypass_server.shopping.facade.port;

import com.example.bypass_server.shopping.domain.ShoppingPoint;
import jakarta.persistence.LockModeType;

public interface ShoppingPointService {
    ShoppingPoint decreasePoint(Long memberId, int amount);
    ShoppingPoint decreasePoint(Long memberId, int amount, LockModeType lockModeType);
}
