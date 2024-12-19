package com.example.bypass_server.bypass.infrastructure;

import com.example.bypass_server.bypass.infrastructure.entity.ValidationUrlJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationUrlJpaRepository extends JpaRepository<Long, ValidationUrlJpaEntity> {
}
