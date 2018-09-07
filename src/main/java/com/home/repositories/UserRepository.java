package com.home.repositories;

import com.home.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByEmail(String email);
}
