package com.app.server.era.backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloseTreatmentRequest {
    private int id;

    @Pattern(regexp = "(Локоть|Колено)", message = "поле можеть содержать лишь значение Локоть либо Колено")
    private String elbowKnee;

    @Pattern(regexp = "(Лево|Право)", message = "поле можеть содержать лишь значение Лево либо Право")
    private String leftRight;
}
