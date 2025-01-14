package com.example.bypass_server.bypassTest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessageHandler {
    public void handleMessage(String message) {
        log.info("[Message Received] {}", message);
    }
}
