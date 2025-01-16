package com.example.bypass_server.medium.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ServiceQueuingManagerTest {
    @Autowired
    ServiceQueuingManager serviceQueuingManager;
    @Autowired
    DeferredServiceQueuingEventHolder deferredServiceQueuingEventHolder;

    @Test
    void 단일_이벤트_처리() throws InterruptedException {
        // given
        final boolean[] flag = {false};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "serviceMethod1";
        String param = "param1";
        // when
        DeferredResult<ServiceQueuingDetails> execute = serviceQueuingManager.execute(
                requestId -> {
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(Long.parseLong(requestId));
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    flag[0] = true;
                }, clintUniqueKey, null, method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertTrue(flag[0]);
    }

    @Test
    void 복수_이벤트_처리() throws InterruptedException {
        // given
        final AtomicInteger[] count = {new AtomicInteger(0)};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "serviceMethod1";
        String param = "param1";
        // when
        DeferredResult<ServiceQueuingDetails> execute1 = serviceQueuingManager.execute(
                requestId -> {
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(Long.parseLong(requestId));
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, null, method, param);
        DeferredResult<ServiceQueuingDetails> execute2 = serviceQueuingManager.execute(
                requestId -> {
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(Long.parseLong(requestId));
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, null, method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertEquals(count[0].get(), 2);
    }

    @Test
    void 다수_이벤트_동시_처리_시_순차_처리됨() throws InterruptedException {
        // given
        final int[] count = {0};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "serviceMethod";
        String param = "param1";

        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i=0; i<threadCount; i++) {
            int finalI = i;
            DeferredResult<ServiceQueuingDetails> execute1 = serviceQueuingManager.execute(
                    requestId -> {
                        Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                                deferredServiceQueuingEventHolder.get(Long.parseLong(requestId));
                        Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                        count[0] += 1;
                    }, clintUniqueKey, null, method, param);
        }

        // then
        Thread.sleep(1000L);
    }
}
