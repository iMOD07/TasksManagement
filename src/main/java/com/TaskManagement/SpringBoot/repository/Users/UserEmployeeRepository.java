package com.TaskManagement.SpringBoot.repository.Users;

import com.TaskManagement.SpringBoot.model.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmployeeRepository extends JpaRepository<UserEmployee, Long> {
    Optional<UserEmployee> findByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);

}

