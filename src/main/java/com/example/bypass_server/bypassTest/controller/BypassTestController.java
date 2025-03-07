package com.example.bypass_server.bypassTest.controller;

import com.example.bypass_server.bypassTest.service.QueuedEventTestService;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequiredArgsConstructor
public class BypassTestController {
    private final QueuedEventTestService queuedEventTestService;
    @RequestMapping("/dup-check-test")
    public Object bypassHandler() {
        return "dup-check-test-ok";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BypassTestResponseDTO {
        private String status;
        private String message;
    }


    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class BypassTestRequestDto {
        private String key;
        private String msg;
    }
}
