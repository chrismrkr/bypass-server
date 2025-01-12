package com.example.bypass_server.duplicateCheck.interceptor;

public interface RequestDistributedLockStorage {
    boolean isUrlHeld(String id, String url);
    void releaseUrl(String id, String url);
    boolean holdUrl(String id, String url);
}
