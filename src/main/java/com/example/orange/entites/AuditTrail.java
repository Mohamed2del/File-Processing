package com.example.orange.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail")
@Getter
@Setter
public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String actionType;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private LocalDateTime actionTimestamp;

    @Column
    private String endpointUri;

    @Column
    private String requestData;

    @Column(nullable = false)
    private String responseStatus;
}