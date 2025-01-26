package com.example.bypass_server.medium.shopping.controller;


import com.example.bypass_server.shopping.controller.dto.ShoppingRequestDto;
import com.example.bypass_server.shopping.infrastructure.ItemJpaRepository;
import com.example.bypass_server.shopping.infrastructure.ShoppingPointJpaRepository;
import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import com.example.bypass_server.shopping.service.port.ItemRepository;
import com.example.bypass_server.shopping.service.port.ShoppingPointRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
}
