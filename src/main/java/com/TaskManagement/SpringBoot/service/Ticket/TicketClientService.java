package com.TaskManagement.SpringBoot.service.Ticket;

import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.TicketClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserAdminRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import com.TaskManagement.SpringBoot.service.User.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketClientService {

    @Autowired
    private final TicketClientRepository ticketRepository;

    @Autowired
    private UserEmployeeRepository userEmployeeRepository;

    @Autowired
    private final UserClientRepository userClientRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private UserAdminRepository userAdminRepository;



    public Ticket createTicket(String title,
                               String description,
                               Long ignoredAssignedToAdmin, // لن تعود بحاجة له
                               String clientEmail) {

        // 1. استخرج عميل الـ Client من الإيميل
        UserClient client = userClientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // 2. تأكد أنه ليس لديه تذكرة مفتوحة
        if (ticketRepository.existsByClient(client)) {
            throw new RuntimeException("You have an existing ticket.");
        }

        // 3. ابحث دائماً عن المشرف ID=1
        UserAdmin admin = userAdminRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default admin (ID=1) not found"));

        // 4. انشئ التذكرة وعيّن المشرف الثابت
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setAssignedToAdmin(admin);
        ticket.setClient(client);

        return ticketRepository.save(ticket);
    }


    /*
    // Create Ticket
    public Ticket createTicket(String title, String desc, Long assignedToAdminId, String clientEmail) {
        if (assignedToAdminId == null) {
            throw new IllegalArgumentException("Assigned Admin ID must not be null");
        }

        UserAdmin admin = userAdminRepository.findById(assignedToAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        UserClient client = userClientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Ticket ticket = Ticket.builder()
                .title(title)
                .description(desc)
                .assignedToAdmin(admin)
                .client(client)
                .createdAt(LocalDateTime.now())
                .build();

        return ticketRepository.save(ticket);
    } */



    // Delete Ticket
    public void deleteTicket(Long ticketId, Long clientId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // check that the ticket holder is the authenticated customer
        if (!ticket.getClient().getId().equals(clientId)) {
            throw new AccessDeniedException("You can only delete your own ticket");
        }
        ticketRepository.delete(ticket);
    }
    // Deleted by admin: No ownership verification
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        ticketRepository.delete(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setTicketStatus(newStatus);
        return ticketRepository.save(ticket);
    }




    // Just Admin He Get All Ticket
    public List<Ticket> getAllTicket() {
        List<Ticket> all = ticketRepository.findAll();
        if (all.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tickets Found");
        }
        return all;
    }


    public List<Ticket> getTicketsByUser(Long userId, UserClient currentClient, boolean isAdmin) {

        // 1 You must check with the client first
        UserClient userclient = userClientRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2 If you are not an ADMIN and you want to view tickets other than your ticket => forbidden
        if (!isAdmin && !currentClient.getId().equals(userclient.getId())) {
            throw new AccessDeniedException("You can only view your own tickets");
        }

        // 3 Get Ticket
        List<Ticket> tickets = ticketRepository.findByClientId(userId);
        if (tickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets for this user");
        }
        return tickets;
    }
}