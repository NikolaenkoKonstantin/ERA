package com.app.server.era.backend.repositories;

import com.app.server.era.backend.models.Doctor;
import com.app.server.era.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByUser(User user);

    @Query("select c from Doctor c " +
            "where lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Doctor> search(@Param("searchTerm") String searchTerm);
}
