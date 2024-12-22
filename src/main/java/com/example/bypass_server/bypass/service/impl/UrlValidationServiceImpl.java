package com.example.bypass_server.bypass.service.impl;

import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.filter.port.ValidUrlReadService;
import com.example.bypass_server.bypass.service.port.ValidationUrlCache;
import com.example.bypass_server.bypass.service.port.ValidationUrlRepository;
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
        return Optional.empty();
    }
}
