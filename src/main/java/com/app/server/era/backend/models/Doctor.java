package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "surname")
    private String surName;

    @ToString.Exclude
    @OneToMany(mappedBy = "doctor")
    private List<Notification> notifications;

    //для привязки к врачу
    @ToString.Exclude
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patient;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;
}
