package com.app.server.era.service;

import com.app.server.era.model.Dimension;
import com.app.server.era.model.Patient;
import com.app.server.era.repository.DimensionRepository;
import com.app.server.era.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DimensionService {
    private final DimensionRepository dimRepo;
    private final PatientRepository patRepo;

    public void addDimension(String cardNumber, Dimension dimension){
        EnrichDimension(dimension, patRepo.findPatientByCardNumber(cardNumber));
        dimRepo.save(dimension);
    }

    private void EnrichDimension(Dimension dimension, Patient patient){
        dimension.setOwner(patient);
        dimension.setDateTime(LocalDateTime.now());
    }
}
