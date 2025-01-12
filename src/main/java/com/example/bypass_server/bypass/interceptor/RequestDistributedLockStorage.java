package com.example.bypass_server.bypass.interceptor;

public interface RequestDistributedLockStorage {
    boolean isLocked(String id, String url);
    void releaseLock(String id, String url);
    boolean setLock(String id, String url);
}
