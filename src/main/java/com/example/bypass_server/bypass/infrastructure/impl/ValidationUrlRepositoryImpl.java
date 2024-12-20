package com.example.bypass_server.bypass.infrastructure.impl;


import com.example.bypass_server.bypass.domain.ValidationType;
import com.example.bypass_server.bypass.domain.ValidationUrl;
import com.example.bypass_server.bypass.infrastructure.ValidationUrlJpaRepository;
import com.example.bypass_server.bypass.infrastructure.entity.ValidationUrlJpaEntity;
import com.example.bypass_server.bypass.service.port.ValidationUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ValidationUrlRepositoryImpl implements ValidationUrlRepository {
    private final ValidationUrlJpaRepository jpaRepository;

    @Override
    public Optional<ValidationUrl> findByUrl(String url) {
        Optional<ValidationUrlJpaEntity> byUrl = jpaRepository.findByUrl(url);
        return byUrl.map(ValidationUrl::fromJpaEntity);
    }

    @Override
    public List<ValidationUrl> findByValidationType(ValidationType type) {
        List<ValidationUrlJpaEntity> byValidationType = jpaRepository.findByValidationType(type.name());
        return byValidationType.stream().map(ValidationUrl::fromJpaEntity)
                .toList();
    }

    @Override
    public ValidationUrl save(ValidationUrl validationUrl) {
        ValidationUrlJpaEntity entity = validationUrl.toJpaEntity();
        ValidationUrlJpaEntity save = jpaRepository.save(entity);
        return ValidationUrl.fromJpaEntity(save);
    }

    @Override
    public void delete(ValidationUrl validationUrl) {
        ValidationUrlJpaEntity entity = validationUrl.toJpaEntity();
        jpaRepository.delete(entity);
    }

    @Override
    public void delete(String url) {
        jpaRepository.deleteByUrl(url);
    }
}
