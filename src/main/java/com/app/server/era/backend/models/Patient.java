package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {
    //id
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //привезка к измерениям
    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private List<Dimension> dimensions;

    //привязка к уведомлениям
    @ToString.Exclude
    @OneToMany(mappedBy = "patient")
    private List<Notification> notifications;

    //для привязки к врачу
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private Doctor doctor;

    //Привязка у пользователю
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    //Фамилия
    @Column(name = "lastname")
    private String lastName;

    //Имя
    @Column(name = "firstname")
    private String firstName;

    //Отчество
    @Column(name = "surname")
    private String surName;

    //Возраст
    @Column(name = "age")
    private int age;

    //Номер карты
    @Column(name = "card_number")
    private String cardNumber;
}
