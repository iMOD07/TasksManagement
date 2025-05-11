package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.SecurityUtils;
import com.TaskManagement.SpringBoot.dto.TicketRequest;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.TicketClient;
import com.TaskManagement.SpringBoot.model.TicketStatus;
import com.TaskManagement.SpringBoot.model.UserClient;
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



    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<TicketClient> create(@RequestBody TicketRequest request) {
        TicketClient ticket = ticketService.createTicket(request);
        return ResponseEntity.ok(ticket);
    }


    /*
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest req,
                                          Authentication authentication) {

        // Authentication.principal Here is the UserClient
        UserClient current = (UserClient) authentication.getPrincipal();

        TicketClient created = ticketService.createTicket(
                req.getTitle(),
                req.getDescription(),
                current.getEmail()      // To Email
        );
        return ResponseEntity.ok(created);
    }


         @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public TicketClient create(@RequestBody TicketRequest request,
                               Long id) {
        return ticketService.createTicket(request);
    }


     */


    // Update Ticket close and open
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{ticketId}")
    public ResponseEntity<TicketClient> updateTicketStatus(@PathVariable Long ticketId,
                                                           @RequestParam TicketStatus status) {
        TicketClient updated = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updated);
    }


    // Just ADMIN he Get All Ticket
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TicketClient>> getAllTicket() {
        List<TicketClient> all = ticketService.getAllTicket();
        System.out.println("admin Admin fetched all tickets, count = " + all.size());
        return ResponseEntity.ok(all);
    }

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketClient>> getUserTickets(@PathVariable("userId") Long userId,
                                                             Authentication authentication) {

        UserClient current = SecurityUtils.getCurrentClient(authentication);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<TicketClient> tickets = ticketService.getTicketsByUser(userId, current, isAdmin);

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