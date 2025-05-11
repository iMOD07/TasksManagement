package com.TaskManagement.SpringBoot.repository.Users;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Collection<Object> findByRole(Role role);
    Optional<User> findFirstByRole(Role role);

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);


}
