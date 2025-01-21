package com.example.bypass_server.queueService.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestUtils {
    private String s;
    public TestUtils(String s) {
        this.s = s;
    }
    public String testMethod(String s) {
        return "[This String] " + this.s + " [Param String] " + s;
    }
}
