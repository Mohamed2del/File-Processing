package com.example.orange.montoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {
    private final MeterRegistry registry;

    public CustomMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementLoginCount() {
        registry.counter("user.login.count").increment();
    }

    public void incrementUploadCount() {
        registry.counter("document.upload.count").increment();
    }

    public void incrementEditCount() {
        registry.counter("document.edit.count").increment();
    }

    public void incrementDeleteCount() {
        registry.counter("document.delete.count").increment();
    }
}
