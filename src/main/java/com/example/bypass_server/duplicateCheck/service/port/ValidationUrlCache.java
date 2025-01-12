package com.example.bypass_server.duplicateCheck.service.port;

import com.example.bypass_server.duplicateCheck.domain.ValidationUrl;

import java.util.Optional;

public interface ValidationUrlCache {
    Optional<ValidationUrl> readByUrl(String url);
    ValidationUrl write(ValidationUrl validationUrl);
    void delete(ValidationUrl validationUrl);
}
