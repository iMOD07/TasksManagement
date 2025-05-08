package com.TaskManagement.SpringBoot.repository.Users;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserEmployeeRepository extends JpaRepository<UserEmployee, Long> {
    Optional<UserEmployee> findByEmail(String email);

    Collection<Object> findByRole(Role role);
    Optional<UserEmployee> findFirstByRole(Role role);

}