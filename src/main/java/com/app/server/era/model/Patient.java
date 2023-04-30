package com.app.server.era.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private List<Dimension> dimensions;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "surname")
    private String surName;

    @Column(name = "age")
    private int age;

    @Column(name = "card_number")
    private String cardNumber;
}
