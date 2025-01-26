package com.example.bypass_server.shopping.facade;

import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.shopping.controller.port.ShoppingFacade;
import com.example.bypass_server.shopping.domain.Item;
import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.facade.port.ItemService;
import com.example.bypass_server.shopping.facade.port.ShoppingPointService;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

@Service("shoppingFacadeImpl")
@Slf4j
public class ShoppingFacadeImpl implements ShoppingFacade {
    private final ItemService itemService;
    private final ShoppingPointService shoppingPointService;
    private final DeferredServiceQueuingEventHolder<ShoppingResponseDto> deferredEventHolder;
    private final ServiceQueuingManager<ShoppingResponseDto> serviceQueuingManager;

    @Autowired
    public ShoppingFacadeImpl(ItemService itemService, ShoppingPointService shoppingPointService,
                              @Qualifier("shoppingResponseDeferredEventHolder") DeferredServiceQueuingEventHolder<ShoppingResponseDto> deferredEventHolder,
                              @Qualifier("shoppingServiceQueuingManager") ServiceQueuingManager<ShoppingResponseDto> serviceQueuingManager) {
        this.itemService = itemService;
        this.shoppingPointService = shoppingPointService;
        this.deferredEventHolder = deferredEventHolder;
        this.serviceQueuingManager = serviceQueuingManager;
    }

    @Override
    @Transactional
    public ShoppingResponseDto buy(Long memberId, Long itemId, int amount, boolean pessimisticLock) {
        try {
            Item item = null;
            ShoppingPoint remains = null;
            if(pessimisticLock) {
                item = itemService.decreaseItem(itemId, amount, LockModeType.PESSIMISTIC_WRITE);
                remains = shoppingPointService.decreasePoint(memberId, item.getPrice() * amount, LockModeType.PESSIMISTIC_WRITE);
            } else {
                item = itemService.decreaseItem(itemId, amount);
                remains = shoppingPointService.decreasePoint(memberId, item.getPrice() * amount);
            }
            return ShoppingResponseDto.builder()
                    .status("S")
                    .message(item.getItemName())
                    .totalPrice((long)amount * (long)item.getPrice())
                    .build();
        } catch (Exception e) {
            return ShoppingResponseDto.builder()
                    .status("F")
                    .message(e.getMessage())
                    .totalPrice(0L)
                    .build();
        }
    }

    @Override
    public DeferredResult<ShoppingResponseDto> buyEvent(Long memberId, Long itemId, int amount) {
        String partitionKey = memberId + "-" + itemId;
        return serviceQueuingManager.execute((response) -> {
            DeferredResult<ShoppingResponseDto> deferredResult = deferredEventHolder.get(response.getRequestId())
                    .orElseThrow(() -> new RuntimeException("Result Not Found"));
            deferredResult.setResult(response.getResponse());
        }, partitionKey, "shoppingFacadeImpl", "buy", memberId, itemId, amount, false);
    }
}
