package com.app.server.era.backend.repositories;

import com.app.server.era.backend.models.Dimension;
import com.app.server.era.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Integer> {
    List<Dimension> findAllByOwner(Patient owner);

    Dimension findFirstByOwnerAndElbowKneeAndLeftRightOrderByDateTimeDesc(Patient p, String eK, String lR);

    Dimension findFirstByOwnerAndElbowKneeAndLeftRightAndStatusOrderByDateTimeDesc(
            Patient owner, String elbowKnee, String leftRight, String status);

    List<Dimension> findAllByOwnerAndElbowKneeAndLeftRight(Patient owner, String elbowKnee, String leftRight);

    List<Dimension> findAllByOwnerAndElbowKneeAndLeftRightAndDateTimeAfter(
            Patient owner, String elbowKnee, String leftRight, LocalDateTime dateTime);
}
