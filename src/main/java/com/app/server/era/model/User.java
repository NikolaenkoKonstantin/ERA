package com.app.server.era.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
//@EqualsAndHashCode
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

    @Column(name = "status")
    private int status;

    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Patient patient;

    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Doctor Doctor;
}
