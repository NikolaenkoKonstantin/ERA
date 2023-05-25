package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

//Модель user
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_era")
public class User {
    //id пользователя
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Логин
    @Column(name = "login")
    private String login;

    //Пароль
    @Column(name = "password")
    private String password;

    //Роль
    @Column(name = "role")
    private String role;

    //Активность
    @Column(name = "active")
    private boolean active;

    //Связь с моделью Patient
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Patient patient;

    //Связь с моделью Doctor
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Doctor Doctor;
}
