package com.example.bypass_server.shopping.infrastructure;

import com.example.bypass_server.shopping.infrastructure.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {
}
