package com.app.server.era.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionRequestDTO {
    @Pattern(regexp = "\\d{6}", message = "must contain 6 digits")
    private String cardNumber;

    @Pattern(regexp = "(elbow|knee)", message = "Part of the body may contain an elbow, or a knee")
    private String elbowKnee;

    @Pattern(regexp = "(left|right)", message = "can only contain left or right")
    private String leftRight;

    @Min(value = 0, message = "bending angle cannot be less than 0")
    @Max(value = 180, message = "bending angle cannot be more than 180")
    private double flexionAngle;

    @Min(value = 0, message = "extension angle cannot be less than 0")
    @Max(value = 180, message = "extension angle cannot be more than 180")
    private double extensionAngle;

    @Min(value = 0, message = "number of folds cannot be negative")
    private int countBend;

    @NotNull(message = "the dizziness field can contain either true or false")
    private boolean dizziness;

    @Min(value = 1, message = "the status field can have values from 1 to 5")
    @Max(value = 5, message = "the status field can have values from 1 to 5")
    private int state;

    @Positive(message = "distance field cannot be negative")
    private Integer distance;
}
