package com.example.bypass_server.medium.shopping.Facade;

import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.controller.port.ShoppingFacade;
import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.infrastructure.ItemJpaRepository;
import com.example.bypass_server.shopping.infrastructure.ShoppingPointJpaRepository;
import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class ShoppingFacadeTest {
    @Autowired
    ShoppingFacade shoppingFacade;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    ShoppingPointJpaRepository shoppingPointJpaRepository;

    @AfterEach
    void clear() {
        itemJpaRepository.deleteAll();
        shoppingPointJpaRepository.deleteAll();
        log.info("====================[TEST END]==================");
    }

    @Test
    void 상품구매_포인트차감() {
        // given
        Long memberId = 1L;
        ItemEntity itemEntity = itemJpaRepository.save(
                Item.builder()
                .itemName("TEST_ITEM")
                .stock(1000)
                .price(1)
                .build().toEntity());
        ShoppingPointEntity shoppingPoint = shoppingPointJpaRepository.save(
                ShoppingPoint.builder()
                        .memberId(memberId)
                        .point(10000L)
                        .build().toEntity()
        );
        // when
        ShoppingResponseDto buy = shoppingFacade.buy(memberId, itemEntity.getId(), 10);
        // then
        Assertions.assertEquals(990, itemJpaRepository.findById(itemEntity.getId()).get().getStock());
        Assertions.assertEquals(9990L, shoppingPointJpaRepository.findById(memberId).get().getPoint());
        Assertions.assertNotNull(buy);
    }

    @Test
    void 상품구매_포인트차감_동시100번요청() {
        // given
        Long memberId = 1L;
        ItemEntity itemEntity = itemJpaRepository.save(
                Item.builder()
                        .itemName("TEST_ITEM")
                        .stock(1000)
                        .price(1)
                        .build().toEntity());
        ShoppingPointEntity shoppingPoint = shoppingPointJpaRepository.save(
                ShoppingPoint.builder()
                        .memberId(memberId)
                        .point(10000L)
                        .build().toEntity()
        );
        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(long i=0; i<=threadCount; i++) {
            executorService.execute(() -> {
                shoppingFacade.buy(memberId, itemEntity.getId(), 5);
                countDownLatch.countDown();
            });
        }
        // then
        ItemEntity resultItemEntity = itemJpaRepository.findById(itemEntity.getId()).get();
        ShoppingPointEntity shoppingPointEntity = shoppingPointJpaRepository.findById(memberId).get();
        Assertions.assertEquals(500, resultItemEntity.getStock());
        Assertions.assertEquals(10000L - 500L, shoppingPointEntity.getPoint());
    }
}
