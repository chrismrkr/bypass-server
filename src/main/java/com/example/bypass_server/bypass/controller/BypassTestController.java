package com.example.bypass_server.bypass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BypassTestController {
    @RequestMapping("/test-me")
    public Object bypassHandler() {
        return "ok";
    }
}
