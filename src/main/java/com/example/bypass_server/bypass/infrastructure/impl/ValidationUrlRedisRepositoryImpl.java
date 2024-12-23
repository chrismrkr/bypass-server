package com.example.bypass_server.bypass.infrastructure.impl;

import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.infrastructure.entity.ValidationUrlCacheEntity;
import com.example.bypass_server.bypass.service.port.ValidationUrlCache;
import com.example.bypass_server.interceptor.port.RequestDistributedLockStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class ValidationUrlRedisRepositoryImpl implements ValidationUrlCache, RequestDistributedLockStorage {
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, ValidationUrlCacheEntity> cacheEntityRedisTemplate;

    private final String URL_DISTRIBUTED_LOCK_PREFIX = "DIS_LOCK";
    private final String URL_DISTRIBUTED_LOCK_SEPERATOR = ":";
    private final Long URL_DISTRIBUTED_LOCK_TIMEOUT = 30000L; // 30sec

    public ValidationUrlRedisRepositoryImpl(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> stringRedisTemplate,
                                            @Qualifier("cacheEntityRedisTemplate") RedisTemplate<String, ValidationUrlCacheEntity> cacheEntityRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.cacheEntityRedisTemplate = cacheEntityRedisTemplate;
    }

    @Override
    public boolean isLocked(String id, String url) {
        String key = getUrlDistributedLockKey(id, url);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key) != null;
    }

    @Override
    public void releaseLock(String id, String url) {
        String key = getUrlDistributedLockKey(id, url);
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        String val = (String) valueOperations.get(key);
        if(!Objects.requireNonNull(val).isEmpty() && val.equals(id)) {
            valueOperations.getAndDelete(key);
        }
    }

    @Override
    public boolean setLock(String id, String url) {
        String key = getUrlDistributedLockKey(id, url);
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.setIfAbsent(key, id,
                    Duration.ofMillis(URL_DISTRIBUTED_LOCK_TIMEOUT));
    }

    @Override
    public Optional<ValidationUrl> readByUrl(String url) {
        ValidationUrlCacheEntity entity = cacheEntityRedisTemplate.opsForValue()
                .get(url);
        ValidationUrl validationUrl = ValidationUrl.fromCacheEntity(entity);
        return Optional.of(validationUrl);
    }

    @Override
    public ValidationUrl write(ValidationUrl validationUrl) {
        cacheEntityRedisTemplate.opsForValue()
                .set(validationUrl.getUrl(), validationUrl.toCacheEntity());
        return validationUrl;
    }

    @Override
    public void delete(ValidationUrl validationUrl) {
        cacheEntityRedisTemplate.opsForValue()
                .getAndDelete(validationUrl.getUrl());
    }

    private String getUrlDistributedLockKey(String id, String url) {
        return new StringBuilder()
                .append(URL_DISTRIBUTED_LOCK_PREFIX)
                .append(URL_DISTRIBUTED_LOCK_SEPERATOR)
                .append(id)
                .append(URL_DISTRIBUTED_LOCK_SEPERATOR)
                .append(url)
                .toString();
    }
}
