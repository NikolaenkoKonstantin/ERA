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
    //id
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Фамилия
    @Column(name = "lastname")
    private String lastName;

    //Имя
    @Column(name = "firstname")
    private String firstName;

    //Отчество
    @Column(name = "surname")
    private String surName;

    //Привязка к уведомлениям
    @ToString.Exclude
    @OneToMany(mappedBy = "doctor")
    private List<Notification> notifications;

    //для привязки к пациенту
    @ToString.Exclude
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patient;

    //привязка к пользователю
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;
}
