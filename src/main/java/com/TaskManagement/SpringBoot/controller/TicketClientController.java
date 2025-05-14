package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.SecurityUtils;
import com.TaskManagement.SpringBoot.dto.TicketRequest;
import com.TaskManagement.SpringBoot.model.*;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.service.Ticket.TicketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketClientController {

    @Autowired
    private TicketClientService ticketService;

    @Autowired
    private UserClientRepository userClientRepository;


    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest request,
                                               Authentication authentication) {

        String email = authentication.getName();
        Ticket ticket = ticketService.createTicket(
                request.getTitle(),
                request.getDescription(),
                request.getAssignedToAdmin(),
                email
        );
        return ResponseEntity.ok(ticket);
    }


    // Update Ticket close and open
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{ticketId}")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long ticketId,
                                                     @RequestParam TicketStatus status) {
        Ticket updated = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updated);
    }


    // Just ADMIN he Get All Ticket
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTicket() {
        List<Ticket> all = ticketService.getAllTicket();
        System.out.println("admin Admin fetched all tickets, count = " + all.size());
        return ResponseEntity.ok(all);
    }

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable("userId") Long userId,
                                                       Authentication authentication) {

        UserClient current = SecurityUtils.getCurrentClient(authentication);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Ticket> tickets = ticketService.getTicketsByUser(userId, current, isAdmin);

        System.out.println("User " + current.getId() +
                (isAdmin ? " (ADMIN)" : "") +
                " fetched tickets for user " + userId +
                ", count = " + tickets.size());

        return ResponseEntity.ok(tickets);
    }


    // Delete ticket
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId,
                                               Authentication authentication) {
        UserClient currentUser = (UserClient) authentication.getPrincipal();


        if (currentUser.getRole() == Role.CLIENT){
            // Client: Delete only his tickets
            ticketService.deleteTicket(ticketId, currentUser.getId());
        } else if (currentUser.getRole().name().equals("ADMIN")) {
            // Admin: Delete any ticket without needing user ID
            ticketService.deleteTicket(ticketId);
        }
        return ResponseEntity.ok("Ticket deleted successfully");
    }


}