package com.app.server.era.repository;

import com.app.server.era.model.Dimension;
import com.app.server.era.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
    Dimension findFirstByOwnerAndElbowKneeOrderByDateTimeDesc(Patient p, int eK);
    //для закрытия реабилитации пациента врачом (такому то пациенту по правой ноге закрыть лечение)
    Dimension findFirstByOwnerAndElbowKneeAndLeftRightOrderByDateTimeDesc(Patient p, int eK, int lR);
}
