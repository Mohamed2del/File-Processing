package com.example.orange.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class ExternalApiMockController {

    // Mock for Fetch Document Metadata
    @GetMapping("/documents/{document_id}/metadata")
    public ResponseEntity<Map<String, Object>> fetchDocumentMetadata(@PathVariable("document_id") String documentId) {
        Map<String, Object> response = new HashMap<>();
        response.put("document_id", documentId);
        response.put("title", "Sample Document Title");
        response.put("author", "John Doe");
        response.put("date_created", "2024-10-25");
        response.put("keywords", new String[]{"example", "document", "metadata"});
        return ResponseEntity.ok(response);
    }

    // Mock for Extract Document Metadata
    @PostMapping("/documents/metadata/extract")
    public ResponseEntity<Map<String, Object>> extractDocumentMetadata(@RequestBody Map<String, String> requestBody) {
        String fileUrl = requestBody.get("file_url");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Metadata extracted successfully for " + fileUrl);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Extracted Document Title");
        metadata.put("author", "Jane Doe");
        metadata.put("date_created", "2024-10-20");
        metadata.put("keywords", new String[]{"extracted", "metadata", "sample"});
        response.put("extracted_metadata", metadata);
        return ResponseEntity.ok(response);
    }

    // Mock for Document Classification
    @PostMapping("/documents/classify")
    public ResponseEntity<Map<String, Object>> classifyDocument(@RequestBody Map<String, String> requestBody) {
        String fileUrl = requestBody.get("file_url");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("classification", "Legal Document");
        response.put("confidence", 0.95);
        return ResponseEntity.ok(response);
    }

    // Mock for Document Status
    @GetMapping("/documents/{document_id}/status")
    public ResponseEntity<Map<String, Object>> getDocumentStatus(@PathVariable("document_id") String documentId) {
        Map<String, Object> response = new HashMap<>();
        response.put("document_id", documentId);
        response.put("status", "processed");
        response.put("last_updated", "2024-10-25T10:15:30");
        return ResponseEntity.ok(response);
    }
}
