package com.example.bypass_server.bypass.filter.port;

import com.example.bypass_server.bypass.domain.ValidationUrl;

import java.util.Optional;
import java.util.Set;

public interface ValidUrlReadService {
    Optional<ValidationUrl> readByUrl(String url);
}
