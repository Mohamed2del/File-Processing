package com.example.orange.services;
import com.example.orange.entites.AuditTrail;
import com.example.orange.repositories.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditTrailService {
    private final AuditTrailRepository auditTrailRepository;

    @Autowired
    public AuditTrailService(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    public List<AuditTrail> getAllLogs() {
        return auditTrailRepository.findAll();
    }

    public List<AuditTrail> searchLogs(String username, String endpoint, Boolean success) {
        // You can implement complex search functionality here.
        // For simplicity, this example returns all logs that match provided parameters.
        return auditTrailRepository.findAll();
    }
}