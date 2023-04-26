package com.app.server.era.repository;

import com.app.server.era.model.Dimension;
import com.app.server.era.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
    Dimension findFirstByOwnerOrderByDateTimeDesc(Patient patient);
}
