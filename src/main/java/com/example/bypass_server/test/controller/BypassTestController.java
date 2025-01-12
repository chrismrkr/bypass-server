package com.example.bypass_server.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BypassTestController {
    @RequestMapping("/dup-check-test")
    public Object bypassHandler() {
        return "dup-check-test-ok";
    }


}
