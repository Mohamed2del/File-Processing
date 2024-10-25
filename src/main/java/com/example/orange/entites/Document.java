package com.example.orange.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String contentType;
    @Lob
    private byte[] encryptedContent;
    private String owner;
    private LocalDateTime uploadedAt;
    @Column(name = "encrypted_aes_key", length = 512) // Increase the length to handle RSA-encrypted key
    private byte[] encryptedAesKey;
    public void setEncryptedAesKey(byte[] encryptedAesKey) {
        this.encryptedAesKey = encryptedAesKey;
    }

    public byte[] getEncryptedAesKey() {
        return encryptedAesKey;
    }
}