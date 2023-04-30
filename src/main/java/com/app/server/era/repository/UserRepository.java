package com.app.server.era.repository;

import com.app.server.era.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLoginAndPassword(String login, String password);
}
