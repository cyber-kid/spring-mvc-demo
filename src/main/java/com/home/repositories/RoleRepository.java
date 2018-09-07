package com.home.repositories;

import com.home.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findRoleByRoleName(String roleName);
}
