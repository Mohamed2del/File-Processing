package com.example.orange.services;

import com.example.orange.entites.Document;
import com.example.orange.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final KeyPair keyPair;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) throws NoSuchAlgorithmException {
        this.documentRepository = documentRepository;
        this.keyPair = generateKeyPair();
    }

    @Async
    public void processDocumentAsync(String filename, String contentType, byte[] content, String owner) throws Exception {
        Document document = new Document();
        document.setFilename(filename);
        document.setContentType(contentType);

        // Encrypt content using hybrid encryption
        EncryptedData encryptedData = encryptContent(content);
        document.setEncryptedContent(encryptedData.encryptedContent);
        document.setEncryptedAesKey(encryptedData.encryptedAesKey);

        document.setOwner(owner);
        document.setUploadedAt(LocalDateTime.now());
        documentRepository.save(document);
    }

    public Document storeDocument(String filename, String contentType, byte[] content, String owner) throws Exception {
        processDocumentAsync(filename, contentType, content, owner);
        return null; // Returning null as the processing is done asynchronously
    }

    public List<Document> searchDocuments() {
        // Implement search functionality for documents
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public byte[] getDecryptedAESKey(Long id) throws Exception {
        Optional<Document> documentOpt = documentRepository.findById(id);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            return rsaCipher.doFinal(document.getEncryptedAesKey());
        } else {
            throw new Exception("Document not found");
        }
    }

    public String getDecryptedDocumentContent(Long id) throws Exception {
        Optional<Document> documentOpt = documentRepository.findById(id);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            byte[] decryptedAESKey = getDecryptedAESKey(id);
            SecretKey aesKey = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decryptedContent = aesCipher.doFinal(document.getEncryptedContent());

            return new String(decryptedContent);
        } else {
            throw new Exception("Document not found");
        }
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private EncryptedData encryptContent(byte[] content) throws Exception {
        // Step 1: Generate AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES key size
        SecretKey aesKey = keyGen.generateKey();

        // Step 2: Encrypt the content using AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedContent = aesCipher.doFinal(content);

        // Step 3: Encrypt the AES key using RSA
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        return new EncryptedData(encryptedContent, encryptedAesKey);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public Document updateDocument(Long id, MultipartFile updatedDocument) throws Exception {
        Optional<Document> document = documentRepository.findById(id);

        Document documentObject= document.orElseThrow();
        processDocumentAsync(documentObject.getFilename(), documentObject.getContentType(), updatedDocument.getBytes(), documentObject.getOwner());
        return null;
    }

    // Helper class to hold encrypted data
    private static class EncryptedData {
        private final byte[] encryptedContent;
        private final byte[] encryptedAesKey;

        public EncryptedData(byte[] encryptedContent, byte[] encryptedAesKey) {
            this.encryptedContent = encryptedContent;
            this.encryptedAesKey = encryptedAesKey;
        }
    }
}
