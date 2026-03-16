package com.dta.repository;

import com.dta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA handles unique constraints and indexing based on @Entity annotations
    Optional<User> findByUsername(String username);
    
    // Added for login and registration lookups
    Optional<User> findByEmail(String email);
    
    // JpaRepository already includes save(), delete(), and audit support via Hibernate
}
