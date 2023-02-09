package com.group04.tgdd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group04.tgdd.model.VerificationToken;

@Repository
public interface VerificationRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findVerificationTokenByTokenAndUser_Email(String token,String email);
    VerificationToken findVerificationTokenByUserEmail(String email);
    VerificationToken findVerificationTokenByUserPhone(String phone);

    VerificationToken findVerificationTokenByUserId(Long uid);
    VerificationToken findVerificationTokenByTokenAndUserPhone(String token, String phone);
}
