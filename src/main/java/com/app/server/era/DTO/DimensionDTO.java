package com.app.server.era.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDTO {
    @Pattern(regexp = "\\d{6}", message = "6 digits")
    private String cardNumber;

    @Min(value = 0, message = "the elbow or leg field must contain either 0 or 1")
    @Max(value = 1, message = "the elbow or leg field must contain either 0 or 1")
    private int elbowKnee;

    @Min(value = 0, message = "the left or right field must contain either 0 or 1")
    @Max(value = 1, message = "the left or right field must contain either 0 or 1")
    private int leftRight;

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

    @Min(value = 1, message = "the state field must contain either 1 or 5")
    @Max(value = 5, message = "the state field must contain either 1 or 5")
    private int state;

    @Positive(message = "distance field cannot be negative")
    private Integer distance;
}
