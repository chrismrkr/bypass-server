package com.example.bypass_server.bypassTest.service;

import org.springframework.stereotype.Service;

@Service("queuedServiceTest")
public class QueuedServiceTest {
    public String testMethod(String s) {
        return "[TEST METHOD] " + s;
    }
}
