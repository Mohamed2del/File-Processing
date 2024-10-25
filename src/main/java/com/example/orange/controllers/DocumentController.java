package com.example.orange.controllers;

import com.example.orange.common.dto.ResponseDTO;
import com.example.orange.entites.Document;
import com.example.orange.montoring.CustomMetrics;
import com.example.orange.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final CustomMetrics customMetrics;

    @Autowired
    public DocumentController(DocumentService documentService, CustomMetrics customMetrics) {
        this.documentService = documentService;
        this.customMetrics = customMetrics;
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO<Void>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("owner") String owner
    )   {
        customMetrics.incrementUploadCount();
        try{
            documentService.storeDocument(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    owner
            );
            return ResponseEntity.ok(ResponseDTO.success("Document uploaded successfully", null, HttpStatus.OK));
        } catch (Exception e)   {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseDTO.failure("Document upload failed", HttpStatus.INTERNAL_SERVER_ERROR));

        }
        }


    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO<List<Document>>> searchDocuments(
    ) throws Exception {
        List<Document> documents = documentService.searchDocuments();
        return ResponseEntity.ok(ResponseDTO.success("Documents retrieved successfully", documents, HttpStatus.OK));
    }

    @GetMapping("/{id}/encrypted")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO> getEncryptedDocument(@PathVariable Long id) {
        Optional<Document> documentOpt = documentService.getDocumentById(id);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            ResponseDTO response = new ResponseDTO( "Encrypted document retrieved successfully", HttpStatus.OK,document);
            return ResponseEntity.ok(response);
        } else {
            ResponseDTO response = new ResponseDTO( "Document not found", HttpStatus.INTERNAL_SERVER_ERROR,"");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Endpoint to get decrypted AES key
    @GetMapping("/{id}/decrypt-key")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO> getDecryptedAESKey(@PathVariable Long id) {
        try {
            byte[] decryptedAESKey = documentService.getDecryptedAESKey(id);
            ResponseDTO response = new ResponseDTO("Decrypted AES key retrieved successfully",HttpStatus.OK, decryptedAESKey);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO response = new ResponseDTO("Failed to retrieve AES key: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR ,null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint to get the decrypted document content
    @GetMapping("/{id}/decrypted")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO> getDecryptedDocument(@PathVariable Long id) {
        try {
            String decryptedContent = documentService.getDecryptedDocumentContent(id);
            ResponseDTO response = new ResponseDTO( "Decrypted document content retrieved successfully", HttpStatus.OK,decryptedContent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO response = new ResponseDTO("Failed to retrieve decrypted document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint to edit a document (Admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> editDocument(@PathVariable Long id,@RequestParam("file") MultipartFile file) {
        customMetrics.incrementEditCount();
        try {
            Document document = documentService.updateDocument(id, file);
            ResponseDTO response = new ResponseDTO("Document updated successfully",HttpStatus.OK, document);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO response = new ResponseDTO( "Failed to update document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint to delete a document (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteDocument(@PathVariable Long id) {
        customMetrics.incrementDeleteCount();
        try {
            documentService.deleteDocument(id);
            ResponseDTO response = new ResponseDTO("Document deleted successfully",HttpStatus.OK ,null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO response = new ResponseDTO("Failed to delete document: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}