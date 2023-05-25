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
    //id
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //привязка к пациенту
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient owner;

    //Сустав
    @Column(name = "elbow_knee")
    private String elbowKnee;

    //Ориентация
    @Column(name = "left_right")
    private String leftRight;

    //Угол сгибания
    @Column(name = "flexion_angle")
    private double flexionAngle;

    //Угол разгибания
    @Column(name = "extension_angle")
    private double extensionAngle;

    //Количетсво сгибаний-разгибаний
    @Column(name = "count_bend")
    private int countBend;

    //Головокружение (УБРАТЬ)
    @Column(name = "dizziness")
    private boolean dizziness;

    //Боль в суставе
    @Column(name = "state")
    private int state;

    //Расстояние (актуально для коленного сустава)
    @Column(name = "distance")
    private Integer distance;

    //Статус (здоров/реабилитация)
    @Column(name = "status")
    private String status;

    //Дата и время
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
