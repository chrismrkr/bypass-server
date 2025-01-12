package com.example.bypass_server.duplicateCheck.service.port;

import com.example.bypass_server.duplicateCheck.domain.ValidationType;
import com.example.bypass_server.duplicateCheck.domain.ValidationUrl;

import java.util.List;
import java.util.Optional;

public interface ValidationUrlRepository {
    Optional<ValidationUrl> findByUrl(String url);
    List<ValidationUrl> findByValidationType(ValidationType type);
    ValidationUrl save(ValidationUrl validationUrl);
    void delete(ValidationUrl validationUrl);
    void delete(String url);
}
