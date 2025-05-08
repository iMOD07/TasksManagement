package com.TaskManagement.SpringBoot.service.Ticket;

import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.TicketClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class TicketClientService {

    @Autowired
    private TicketClientRepository ticketRepository;

    @Autowired
    private UserEmployeeRepository employeeRepository;

    @Autowired
    private UserClientRepository clientRepository;


    public TicketClient createTicket(String title,
                                     String description,
                                     String clientEmail) {

        // Extract the Client from the Email
        UserClient client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // 🟡 تأكد فقط من عدم وجود تذكرة مفتوحة
        if (ticketRepository.existsByClientAndStatus(client, "OPEN")) {
            throw new RuntimeException("You already have an open ticket.");
        }

        // 🔵 جلب أول مسؤول مباشرة بدون تحميل الكل
        UserEmployee admin = employeeRepository.findFirstByRole(Role.ADMIN)
                .orElseThrow(() -> new RuntimeException("No ADMIN user found"));

        // 🟢 إنشاء التذكرة
        TicketClient ticket = new TicketClient();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setAssignedTo(admin);
        ticket.setClient(client);
        ticket.setStatus(TicketStatus.OPEN); // عند الإنشاء// ⚠️ تأكد من وجود هذا الحقل في `TicketClient`

        return ticketRepository.save(ticket);
    }


    // Delete Ticket
    public void deleteTicket(Long ticketId, Long clientId) {
        TicketClient ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // check that the ticket holder is the authenticated customer
        if (!ticket.getClient().getId().equals(clientId)) {
            throw new AccessDeniedException("You can only delete your own ticket");
        }
        ticketRepository.delete(ticket);
    }
    // Deleted by admin: No ownership verification
    public void deleteTicket(Long ticketId) {
        TicketClient ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        ticketRepository.delete(ticket);
    }



    // Just Admin He Get All Ticket
    public List<TicketClient> getAllTicket() {
        List<TicketClient> all = ticketRepository.findAll();
        if (all.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tickets Found");
        }
        return all;
    }


    public List<TicketClient> getTicketsByUser(Long userId, UserClient currentClient, boolean isAdmin) {

        // 1 You must check with the client first
        UserClient userclient = clientRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2 If you are not an ADMIN and you want to view tickets other than your ticket => forbidden
        if (!isAdmin && !currentClient.getId().equals(userclient.getId())) {
            throw new AccessDeniedException("You can only view your own tickets");
        }

        // 3 Get Ticket
        List<TicketClient> tickets = ticketRepository.findByClientId(userId);
        if (tickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets for this user");
        }
        return tickets;
    }
}