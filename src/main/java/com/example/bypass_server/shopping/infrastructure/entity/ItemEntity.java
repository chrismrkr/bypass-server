package com.example.bypass_server.shopping.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private int price;
    private int stock;

    @Builder
    private ItemEntity(Long id, String itemName, int price, int stock) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
    }
}
