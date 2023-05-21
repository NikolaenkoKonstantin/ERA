package com.app.server.era.backend.dto;

import com.app.server.era.backend.models.Doctor;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientEditDoctorRequest {
    private int id;

    private Doctor doctor;

    public PatientEditDoctorRequest(int id) {
        this.id = id;
    }
}
