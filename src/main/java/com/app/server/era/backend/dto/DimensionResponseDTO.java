package com.app.server.era.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionResponseDTO {
    private int id;

    private String elbowKnee;

    private String leftRight;

    private double flexionAngle;

    private double extensionAngle;

    private int countBend;

    private String dizziness;

    private int state;

    private String distance;

    private String status;

    private LocalDateTime dateTime;
}
