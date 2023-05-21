package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_era")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "active")
    private boolean active;

    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Patient patient;

    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Doctor Doctor;
}
