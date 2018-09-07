package com.home.repositories;

import com.home.entities.User;
import com.home.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    VerificationToken findByTokenAndExpiryDateAfter(String token, LocalDateTime now);
}
