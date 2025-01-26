package com.example.bypass_server.shopping.service;

import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.facade.port.ShoppingPointService;
import com.example.bypass_server.shopping.service.port.ShoppingPointRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingPointServiceImpl implements ShoppingPointService {
    private final ShoppingPointRepository shoppingPointRepository;
    @Override
    @Transactional
    public ShoppingPoint decreasePoint(Long memberId, int amount) {
        ShoppingPoint shoppingPoint = shoppingPointRepository.findById(memberId);
        if(shoppingPoint.getPoint() < amount) {
            throw new IllegalArgumentException("Not Enough Points");
        }
        shoppingPoint.spend(amount);
        return shoppingPointRepository.save(shoppingPoint);
    }

    @Override
    @Transactional
    public ShoppingPoint decreasePoint(Long memberId, int amount, LockModeType lockModeType) {
        ShoppingPoint shoppingPoint = shoppingPointRepository.findById(memberId, lockModeType);
        if(shoppingPoint.getPoint() < amount) {
            throw new IllegalArgumentException("Not Enough Points");
        }
        shoppingPoint.spend(amount);
        return shoppingPointRepository.save(shoppingPoint);
    }
}
