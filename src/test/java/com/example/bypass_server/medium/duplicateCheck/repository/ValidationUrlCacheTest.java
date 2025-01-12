package com.example.bypass_server.medium.duplicateCheck.repository;

import com.example.bypass_server.duplicateCheck.domain.ValidationType;
import com.example.bypass_server.duplicateCheck.domain.ValidationUrl;
import com.example.bypass_server.duplicateCheck.service.port.ValidationUrlCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValidationUrlCacheTest {
    @Autowired
    ValidationUrlCache validationUrlCache;

    @Test
    void ValidationUrl_캐시_저장_및_조회() {
        // given
        ValidationUrl validationUrl = ValidationUrl.builder()
                .id(1L)
                .url("/test/me")
                .validationType(ValidationType.DUPLICATE_CHECK)
                .build();
        try {
            // when
            ValidationUrl write = validationUrlCache.write(validationUrl);
            // then
            ValidationUrl read = validationUrlCache.readByUrl(validationUrl.getUrl())
                    .get();
            Assertions.assertEquals(write.getId(), read.getId());
            Assertions.assertEquals(write.getUrl(), read.getUrl());
            Assertions.assertEquals(write.getValidationType(), read.getValidationType());
        }
        finally {
            validationUrlCache.delete(validationUrl);
        }
    }
}
