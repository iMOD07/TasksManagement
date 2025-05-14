package com.TaskManagement.SpringBoot.repository.Users;

import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {
    Optional<UserAdmin> findByEmail(String email);
    List<UserAdmin> findByRole(Role role);
}
