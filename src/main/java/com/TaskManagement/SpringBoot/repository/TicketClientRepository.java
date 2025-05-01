package com.TaskManagement.SpringBoot.repository;

import com.TaskManagement.SpringBoot.model.TicketClient;
import com.TaskManagement.SpringBoot.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketClientRepository extends JpaRepository<TicketClient, Long> {

    boolean existsByClient(UserClient client);

    List<TicketClient> findByAssignedToId(Long id);

    List<TicketClient> findByClientId(Long clientId);

    // Check for tickets associated with a Clients
    boolean existsByAssignedToId(Long clientId);



}
