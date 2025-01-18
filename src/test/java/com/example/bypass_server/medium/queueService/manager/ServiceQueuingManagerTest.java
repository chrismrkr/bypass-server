package com.example.bypass_server.medium.queueService.manager;

import com.example.bypass_server.bypassTest.service.BypassTestService;
import com.example.bypass_server.bypassTest.service.QueuedServiceTest;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.Serializable;
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
        String method = "testMethod";
        String param = "param1";
        // when
        DeferredResult<ServiceQueuingDetails> execute = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    flag[0] = true;
                }, clintUniqueKey, new QueuedServiceTest(), method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertTrue(flag[0]);
    }

    @Test
    void 스프링_빈_메소드_단일_이벤트_처리() throws InterruptedException {
        // given
        final boolean[] flag = {false};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "testMethod";
        String param = "param1";
        // when
        DeferredResult<ServiceQueuingDetails> execute = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    flag[0] = true;
                }, clintUniqueKey, "queuedServiceTest", method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertTrue(flag[0]);
    }

    @Test
    void 복수_이벤트_처리() throws InterruptedException {
        // given
        final AtomicInteger[] count = {new AtomicInteger(0)};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "testMethod";
        String param = "param1";
        // when
        DeferredResult<ServiceQueuingDetails> execute1 = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, new QueuedServiceTest(), method, param);
        DeferredResult<ServiceQueuingDetails> execute2 = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<ServiceQueuingDetails>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, new QueuedServiceTest(), method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertEquals(count[0].get(), 2);
    }

    @Getter
    public static class ManagerTestClass implements Serializable {
        public ManagerTestClass() {
        }
        public String testMethod(String a) {
            return "[TEST] " + a;
        }
    }
}
