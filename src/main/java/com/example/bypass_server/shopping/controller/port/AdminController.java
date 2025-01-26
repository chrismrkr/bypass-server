package com.example.bypass_server.shopping.controller.port;

import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.service.port.ItemRepository;
import com.example.bypass_server.shopping.service.port.ShoppingPointRepository;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final ItemRepository itemRepository;
    private final ShoppingPointRepository shoppingPointRepository;

    @PostMapping("/item")
    public Item handleCreatingItem(@RequestBody ItemCreateDto dto) {
        Item save = itemRepository.save(
                Item.builder()
                        .itemName(dto.getItemName())
                        .price(dto.getPrice())
                        .stock(dto.getStock())
                        .build()
        );
        return save;
    }

    @PostMapping("/shoppingpoint")
    public ShoppingPoint handleCreateingShoppingPoint(@RequestBody ShoppingPointCreateDto dto) {
        ShoppingPoint save = shoppingPointRepository.save(
                ShoppingPoint.builder()
                        .memberId(dto.getMemberId())
                        .point(dto.getPoint())
                        .build()
        );
        return save;
    }

    @NoArgsConstructor
    @Getter
    @ToString
    private static class ItemCreateDto {
        private String itemName;
        private int price;
        private int stock;
        @Builder
        public ItemCreateDto(String itemName, int price, int stock) {
            this.itemName = itemName;
            this.price = price;
            this.stock = stock;
        }
    }

    @NoArgsConstructor
    @Getter
    @ToString
    private static class ShoppingPointCreateDto {
        private Long memberId;
        private Long point;
        @Builder
        public ShoppingPointCreateDto(Long memberId, Long point) {
            this.memberId = memberId;
            this.point = point;
        }
    }
}
