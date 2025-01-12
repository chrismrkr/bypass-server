package com.example.bypass_server.medium.duplicateCheck.repository;

import com.example.bypass_server.duplicateCheck.interceptor.RequestDistributedLockStorage;
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
            boolean b = requestDistributedLockStorage.holdUrl(id, url);
            // then
            Assertions.assertTrue(b);
        } finally {
            requestDistributedLockStorage.releaseUrl(id, url);
        }
    }

    @Test
    void 사용자_및_특정_URI별_분산_락_획득() {
        // given
        String id = "123abcabcabc456";
        String url = "/test/url";
        try {
            // when, then
            boolean b = requestDistributedLockStorage.holdUrl(id, url);
            Assertions.assertTrue(b);
            boolean b1 = requestDistributedLockStorage.holdUrl(id, url);
            Assertions.assertFalse(b1);
        } finally {
            requestDistributedLockStorage.releaseUrl(id, url);
        }
    }
}
