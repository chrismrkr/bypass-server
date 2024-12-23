package com.example.bypass_server.bypass.service.impl;

import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.filter.port.ValidUrlReadService;
import com.example.bypass_server.bypass.service.port.ValidationUrlCache;
import com.example.bypass_server.bypass.service.port.ValidationUrlRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlValidationServiceImpl implements ValidUrlReadService {
    private final ValidationUrlCache cache;
    private final ValidationUrlRepository repository;
    @Override
    public Optional<ValidationUrl> readByUrl(String url) {
        Optional<ValidationUrl> cacheData = cache.readByUrl(url);
        if(cacheData.isPresent()) {
            return cacheData;
        }
        ValidationUrl repoData = repository.findByUrl(url)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("");
                });
        return Optional.of(repoData);
    }

    @PostConstruct
    void init() {

    }
}
