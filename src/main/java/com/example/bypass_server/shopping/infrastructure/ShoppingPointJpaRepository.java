package com.example.bypass_server.shopping.infrastructure;

import com.example.bypass_server.shopping.domain.ShoppingPoint;
import com.example.bypass_server.shopping.infrastructure.entity.ShoppingPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingPointJpaRepository extends JpaRepository<ShoppingPointEntity, Long> {
}
