package com.TaskManagement.SpringBoot.repository;

import com.TaskManagement.SpringBoot.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long> {
    Optional<UserClient> findByEmail(String email);
    Optional<UserClient> findByMobileNumber(String mobileNumber);

}
