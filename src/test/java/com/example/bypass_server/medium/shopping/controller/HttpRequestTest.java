package com.example.bypass_server.medium.shopping.controller;


import com.example.bypass_server.shopping.controller.dto.ShoppingRequestDto;
import com.example.bypass_server.shopping.infrastructure.ItemJpaRepository;
import com.example.bypass_server.shopping.infrastructure.ShoppingPointJpaRepository;
import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HttpRequestTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    ShoppingPointJpaRepository shoppingPointJpaRepository;
    @Autowired
    ObjectMapper objectMapper;


    @AfterEach
    void clearAll() {
        itemJpaRepository.deleteAll();
        shoppingPointJpaRepository.deleteAll();
    }



    @Test
    void 아이템_구매_요청() throws Exception {
        // given

        ItemEntity savedItem = itemJpaRepository.save(
                ItemEntity.builder()
                        .itemName("TestItem")
                        .price(1)
                        .stock(10000)
                        .build()
        );
        ShoppingPointEntity savedShoppingPoints = shoppingPointJpaRepository.save(
                ShoppingPointEntity.builder()
                        .memberId(1L)
                        .point(10000L)
                        .build()
        );
        // when
        int buyAmt = 100;
        ShoppingRequestDto reqDto = ShoppingRequestDto
                .builder()
                .memberId(savedShoppingPoints.getMemberId())
                .itemId(savedItem.getId()).amount(buyAmt).build();
        MvcResult mvcResult = mockMvc.perform(patch("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        ItemEntity itemEntity = itemJpaRepository.findById(savedItem.getId()).get();
        Assertions.assertEquals(10000-100, itemEntity.getStock());
    }

    @Test
    void 아이템_구매_이벤트_요청() throws Exception {
        // given
        ItemEntity savedItem = itemJpaRepository.save(
                ItemEntity.builder()
                        .itemName("TestItem")
                        .price(1)
                        .stock(10000)
                        .build()
        );
        ShoppingPointEntity savedShoppingPoints = shoppingPointJpaRepository.save(
                ShoppingPointEntity.builder()
                        .memberId(1L)
                        .point(10000L)
                        .build()
        );
        // when
        int buyAmt = 100;
        ShoppingRequestDto reqDto = ShoppingRequestDto
                .builder()
                .memberId(savedShoppingPoints.getMemberId())
                .itemId(savedItem.getId()).amount(buyAmt).build();
        MvcResult mvcResult = mockMvc.perform(patch("/event/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        Thread.sleep(1000L);
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        ItemEntity itemEntity = itemJpaRepository.findById(savedItem.getId()).get();
        Assertions.assertEquals(10000-100, itemEntity.getStock());
    }

    @Test
    void 아이템구매_요청_동시10번() throws Exception {
        // given
        ItemEntity savedItem = itemJpaRepository.save(
                ItemEntity.builder()
                        .itemName("TestItem")
                        .price(1)
                        .stock(10000)
                        .build()
        );
        ShoppingPointEntity savedShoppingPoints = shoppingPointJpaRepository.save(
                ShoppingPointEntity.builder()
                        .memberId(1L)
                        .point(10000L)
                        .build()
        );
        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i=0; i<threadCount; i++) {
            executorService.execute(() -> {
                int buyAmt = 100;
                ShoppingRequestDto reqDto = ShoppingRequestDto
                        .builder()
                        .memberId(savedShoppingPoints.getMemberId())
                        .itemId(savedItem.getId()).amount(buyAmt).build();
                try {
                    MvcResult mvcResult = mockMvc.perform(patch("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(reqDto)))
                            .andExpect(status().isOk())
                            .andReturn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        // then
        Thread.sleep(1000L);
        ItemEntity itemEntity = itemJpaRepository.findById(savedItem.getId()).get();
        ShoppingPointEntity shoppingPointEntity = shoppingPointJpaRepository.findById(savedShoppingPoints.getMemberId()).get();
        Assertions.assertEquals(10000-100*10, itemEntity.getStock());
        Assertions.assertEquals(10000-100*10, shoppingPointEntity.getPoint());
    }

    @Test
    void 아이템구매_요청_이벤트_동시10번() throws Exception {
        // given
        ItemEntity savedItem = itemJpaRepository.save(
                ItemEntity.builder()
                        .itemName("TestItem")
                        .price(1)
                        .stock(10000)
                        .build()
        );
        ShoppingPointEntity savedShoppingPoints = shoppingPointJpaRepository.save(
                ShoppingPointEntity.builder()
                        .memberId(1L)
                        .point(10000L)
                        .build()
        );
        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i=0; i<threadCount; i++) {
            executorService.execute(() -> {
                int buyAmt = 100;
                ShoppingRequestDto reqDto = ShoppingRequestDto
                        .builder()
                        .memberId(savedShoppingPoints.getMemberId())
                        .itemId(savedItem.getId()).amount(buyAmt).build();
                try {
                    MvcResult mvcResult = mockMvc.perform(patch("/event/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(reqDto)))
                            .andExpect(status().isOk())
                            .andReturn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        // then
        Thread.sleep(1000L);
        ItemEntity itemEntity = itemJpaRepository.findById(savedItem.getId()).get();
        ShoppingPointEntity shoppingPointEntity = shoppingPointJpaRepository.findById(savedShoppingPoints.getMemberId()).get();
        Assertions.assertEquals(10000-100*10, itemEntity.getStock());
        Assertions.assertEquals(10000-100*10, shoppingPointEntity.getPoint());
    }

    @Test
    void 아이템구매_요청_동시_10명이_2번씩() throws Exception {
        // given
        ItemEntity savedItem = itemJpaRepository.save(
                ItemEntity.builder()
                        .itemName("TestItem")
                        .price(1)
                        .stock(100000)
                        .build()
        );
        for(int i=0; i<10; i++) {
            ShoppingPointEntity savedShoppingPoints = shoppingPointJpaRepository.save(
                    ShoppingPointEntity.builder()
                            .memberId((long)i)
                            .point(10000L)
                            .build()
            );
        }
        // when
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for(int i=0; i<threadCount; i++) {
            int finalI = i;
            executorService.execute(() -> {
                int buyAmt = 100;
                ShoppingRequestDto reqDto = ShoppingRequestDto
                        .builder()
                        .memberId((long)(finalI)%10)
                        .itemId(savedItem.getId()).amount(buyAmt).build();
                try {
                    MvcResult mvcResult = mockMvc.perform(patch("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(reqDto)))
                            .andExpect(status().isOk())
                            .andReturn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        // then
        Thread.sleep(1000L);
        ItemEntity itemEntity = itemJpaRepository.findById(savedItem.getId()).get();
        Assertions.assertEquals(100000-100*10*2, itemEntity.getStock());
        for(int i=0; i<10; i++) {
            ShoppingPointEntity shoppingPointEntity = shoppingPointJpaRepository.findById((long) i).get();
            Assertions.assertEquals(10000L - 100*2, shoppingPointEntity.getPoint());
        }
    }
}
