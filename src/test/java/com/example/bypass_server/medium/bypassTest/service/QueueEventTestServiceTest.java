package com.example.bypass_server.medium.bypassTest.service;

import com.example.bypass_server.bypassTest.controller.BypassTestController;
import com.example.bypass_server.bypassTest.service.QueuedEventTestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
public class QueueEventTestServiceTest {
    @Autowired
    QueuedEventTestService queuedEventTestService;

    @Test
    void 서비스_호출_테스트() throws InterruptedException {
        // given
        // when
        DeferredResult<BypassTestController.BypassTestResponseDTO> result = queuedEventTestService.doTest("123sfabbaadf1312aafdsaf", "aaaa");
        // then
        Thread.sleep(1500L);
        Assertions.assertTrue(result.hasResult());
    }
}
