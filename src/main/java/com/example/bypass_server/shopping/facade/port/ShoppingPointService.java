package com.example.bypass_server.shopping.facade.port;

import com.example.bypass_server.shopping.domain.ShoppingPoint;

public interface ShoppingPointService {
    ShoppingPoint decreasePoint(Long memberId, int amount);
}
