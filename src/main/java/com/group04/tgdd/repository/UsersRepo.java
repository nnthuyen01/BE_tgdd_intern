package com.group04.tgdd.repository;

import com.group04.tgdd.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Users findUsersByEmail(String email);

    Users findUsersByPhone(String phone);
}
