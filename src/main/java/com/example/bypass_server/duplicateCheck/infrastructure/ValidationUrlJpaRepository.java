package com.example.bypass_server.duplicateCheck.infrastructure;

import com.example.bypass_server.duplicateCheck.infrastructure.entity.ValidationUrlJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValidationUrlJpaRepository extends JpaRepository<ValidationUrlJpaEntity, Long> {
    List<ValidationUrlJpaEntity> findByValidationType(String validationType);
    Optional<ValidationUrlJpaEntity> findByUrl(String url);
    void deleteByUrl(String url);
}
