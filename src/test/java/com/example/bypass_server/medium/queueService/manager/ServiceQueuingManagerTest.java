package com.example.bypass_server.medium.queueService.manager;

import com.example.bypass_server.bypassTest.service.QueuedEventTestService;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import com.example.bypass_server.queueService.utils.TestUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.Serializable;
import java.util.Optional;
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
        TestUtils testUtils = new TestUtils();
        // when
        DeferredResult<Object> execute = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<Object>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    flag[0] = true;
                }, clintUniqueKey, new TestUtils("Hello"), method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertTrue(flag[0]);
    }

    @Test
    void 스프링_빈_메소드_단일_이벤트_처리() throws InterruptedException {
        // given
        final boolean[] flag = {false};
        String clintUniqueKey = "aabbccddeeffz123fvdfqbb124a";
        String method = "doService";
        String param1 = "param1";
        // when
        DeferredResult<Object> execute = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<Object>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    flag[0] = true;
                }, clintUniqueKey, "queuedEventTestService", method, param1);
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
        DeferredResult<Object> execute1 = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<Object>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, new ManagerTestClass(), method, param);
        DeferredResult<Object> execute2 = serviceQueuingManager.execute(
                result -> {
                    Long requestId = result.getRequestId();
                    Optional<DeferredResult<Object>> serviceQueuingDetailsDeferredResult =
                            deferredServiceQueuingEventHolder.get(requestId);
                    Assertions.assertFalse(serviceQueuingDetailsDeferredResult.isEmpty());
                    count[0].incrementAndGet();
                }, clintUniqueKey, new ManagerTestClass(), method, param);
        // then
        Thread.sleep(1000L);
        Assertions.assertEquals(count[0].get(), 2);
    }

    @Getter
    @JsonSerialize
    public static class ManagerTestClass implements Serializable {
        public ManagerTestClass() {
        }
        public String testMethod(String a) {
            return "[TEST] " + a;
        }
    }
}
