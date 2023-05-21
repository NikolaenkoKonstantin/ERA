package com.app.server.era.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dimension")
public class Dimension {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient owner;

    @Column(name = "elbow_knee")
    private String elbowKnee;

    @Column(name = "left_right")
    private String leftRight;

    @Column(name = "flexion_angle")
    private double flexionAngle;

    @Column(name = "extension_angle")
    private double extensionAngle;

    @Column(name = "count_bend")
    private int countBend;

    @Column(name = "dizziness")
    private boolean dizziness;

    @Column(name = "state")
    private int state;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "status")
    private String status;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
