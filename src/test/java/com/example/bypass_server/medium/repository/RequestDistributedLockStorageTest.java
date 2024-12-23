package com.example.bypass_server.medium.repository;

import com.example.bypass_server.interceptor.port.RequestDistributedLockStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RequestDistributedLockStorageTest {
    @Autowired
    RequestDistributedLockStorage requestDistributedLockStorage;

    @Test
    void URL_분산락_획득() {
        // given
        String id = "123abcabcabc123";
        String url = "/test/url";

        try {
            // when
            boolean b = requestDistributedLockStorage.setLock(id, url);
            // then
            Assertions.assertTrue(b);
        } finally {
            requestDistributedLockStorage.releaseLock(id, url);
        }
    }

    @Test
    void 사용자_및_특정_URI별_분산_락_획득() {
        // given
        String id = "123abcabcabc456";
        String url = "/test/url";
        try {
            // when, then
            boolean b = requestDistributedLockStorage.setLock(id, url);
            Assertions.assertTrue(b);
            boolean b1 = requestDistributedLockStorage.setLock(id, url);
            Assertions.assertFalse(b1);
        } finally {
            requestDistributedLockStorage.releaseLock(id, url);
        }
    }
}
