package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.service.User.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private UserServiceClient clientService;


    //Get all Client - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping
    public ResponseEntity<List<UserClient>> getAllClients() {
        List<UserClient> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Get Client By ID - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserClient> getClientById(@PathVariable Long id) {
        Optional<UserClient> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete Account By CLIENT and ADMIN He Delete Any Users
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteUserClient(@PathVariable Long clientId,
                                                   Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserClient userClient) {
            boolean isAdminClient = userClient.getRole() == Role.ADMIN;

            if (!isAdminClient && !userClient.getId().equals(clientId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to delete another CLIENT.");
            }
        }


        try {
            clientService.deleteClient(clientId);
            return ResponseEntity.ok("The client has been successfully deleted.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceLockedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("You cannot delete the account because you have ticket.");
        }
    }
}