package com.TaskManagement.SpringBoot.repository;

import com.TaskManagement.SpringBoot.model.Ticket;
import com.TaskManagement.SpringBoot.model.TicketStatus;
import com.TaskManagement.SpringBoot.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketClientRepository extends JpaRepository<Ticket, Long> {

    boolean existsByClient(UserClient client);

    List<Ticket> findByAssignedToAdmin_Id(Long id); // ✅ FIXED

    List<Ticket> findByClientId(Long clientId);

    boolean existsByAssignedToAdmin_Id(Long adminId);

    boolean existsByClientAndTicketStatus(UserClient client, TicketStatus status);
}