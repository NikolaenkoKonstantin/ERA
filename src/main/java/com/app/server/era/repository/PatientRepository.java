package com.app.server.era.repository;

import com.app.server.era.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findPatientByCardNumber(String cardNumber);
}
