package com.example.bypass_server.queueService.factory;

import com.example.bypass_server.queueService.manager.ServiceQueuingManager;

public interface ServiceQueuingManagerFactory<T> {
    ServiceQueuingManager<T> createManager();
}
