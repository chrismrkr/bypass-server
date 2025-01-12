package com.example.bypass_server.duplicateCheck.filter.port;

import com.example.bypass_server.duplicateCheck.domain.ValidationUrl;

import java.util.Optional;

public interface ValidUrlReadService {
    Optional<ValidationUrl> readByUrl(String url);
}
