package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private Doctor doctor;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient patient;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Notification(Doctor doctor, Patient patient, LocalDateTime dateTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.dateTime = dateTime;
    }
}
