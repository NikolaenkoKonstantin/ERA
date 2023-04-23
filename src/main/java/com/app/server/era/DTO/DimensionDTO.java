package com.app.server.era.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDTO {
    private String cardNumber;

    private int elbowKnee;

    private int leftRight;

    private double flexionAngle;

    private double extensionAngle;

    private int countBend;

    private boolean dizziness;

    private int state;

    private Integer distance;
}
