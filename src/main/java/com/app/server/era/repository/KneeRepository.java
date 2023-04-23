package com.app.server.era.repository;

import com.app.server.era.model.Knee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KneeRepository extends JpaRepository<Knee, Integer> {
}
