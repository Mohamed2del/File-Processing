package com.example.orange.controllers;

import com.example.orange.common.dto.ResponseDTO;
import com.example.orange.entites.AuditTrail;
import com.example.orange.services.AuditTrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AuditTrailController {
    private final AuditTrailService auditTrailService;

    @Autowired
    public AuditTrailController(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AuditTrail>>> getAllLogs() {
        List<AuditTrail> logs = auditTrailService.getAllLogs();
        return ResponseEntity.ok(ResponseDTO.success("Logs retrieved successfully", logs, HttpStatus.OK));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<AuditTrail>>> searchLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String endpoint,
            @RequestParam(required = false) Boolean success
    ) {
        List<AuditTrail> logs = auditTrailService.searchLogs(username, endpoint, success);
        return ResponseEntity.ok(ResponseDTO.success("Logs retrieved successfully", logs, HttpStatus.OK));
    }
}
