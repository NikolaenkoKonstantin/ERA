package com.app.server.era.backend.repositories;

import com.app.server.era.backend.models.Doctor;
import com.app.server.era.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findAllByDoctor(Doctor doctor);

    Patient findPatientByCardNumber(String cardNumber);

    @Query("select c from Patient c " +
            "where c.doctor = :doctor and " +
            "(lower(c.lastName) like lower(concat('%', :searchTerm, '%')))")
    List<Patient> search(@Param("searchTerm") String searchTerm, @Param("doctor") Doctor doctor);

}
