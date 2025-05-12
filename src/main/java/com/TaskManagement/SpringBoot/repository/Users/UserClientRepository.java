package com.TaskManagement.SpringBoot.repository.Users;

import com.TaskManagement.SpringBoot.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserClientRepository extends JpaRepository<UserClient, Long> {


    /* @Query("""
    SELECT uc
    FROM UserClient uc
    JOIN User u ON u.id = uc.id
    WHERE u.email = :email
""")
    Optional<UserClient> findByEmailUsingUserJoin(@Param("email") String email);
*/

    Optional<UserClient> findByEmail(@Param("email") String email);

    Optional<UserClient> findByMobileNumber (String mobileNumber);


}
