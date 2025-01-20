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

    @RequestMapping("/fcfs-check-test")
    public Object fcfsTestHandler() {
        return "fcfs-check-test";
    }

    @PostMapping("/queued-event-test")
    public DeferredResult<BypassTestResponseDTO> queuedEventHandler(@RequestBody BypassTestRequestDto requestDto) {
        queuedEventTestService.doTest(requestDto.getKey(), requestDto.getMsg());
        return null;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BypassTestResponseDTO {
        private String status;
        private String message;
    }


    @AllArgsConstructor
    @Data
    private static class BypassTestRequestDto {
        private String key;
        private String msg;
    }
}
