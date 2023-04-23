package com.app.server.era.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "elbow")
public class Elbow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "left_right")
    private int leftRight;

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

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
