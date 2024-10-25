package com.example.orange.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

// Role.java
@Entity(name = "role")
@Setter
@Getter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    public Role(String authority) {
        this.name = authority;
    }

    public Role() {

    }

    @Override
    public String getAuthority() {
        return name;
    }

}