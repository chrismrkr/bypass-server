package com.example.bypass_server.shopping.domain;

import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Item {
    private Long itemId;
    private String itemName;
    private int stock;
    private int price;

    @Builder
    private Item(Long itemId, String itemName, int stock, int price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.stock = stock;
        this.price = price;
    }

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .id(this.itemId)
                .itemName(this.itemName)
                .price(this.price)
                .stock(this.stock)
                .build();
    }

    public static Item from(ItemEntity entity) {
        return Item.builder()
                .itemId(entity.getId())
                .itemName(entity.getItemName())
                .stock(entity.getStock())
                .price(entity.getPrice())
                .build();
    }

    public void give(int amount) {
        this.stock -= amount;
    }
}
