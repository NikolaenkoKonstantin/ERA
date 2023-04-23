package com.app.server.era.repository;

import com.app.server.era.model.Elbow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElbowRepository extends JpaRepository<Elbow, Integer> {
}
