package com.insight_pulse.tech.user.domain;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);    
    User findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
}
