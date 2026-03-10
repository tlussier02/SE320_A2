package com.dta.repository;

import com.dta.entity.User;
import java.util.Optional;

public interface UserRepository {
    // TODO [Timmy]: Implement with JPA and add unique constraints/indexes on username/email.
    Optional<User> findByUsername(String username);
    // TODO [Timmy]: Add save/update behavior with soft-delete policy and audit fields.
    User save(User user);
}
