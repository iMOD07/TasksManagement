package com.TaskManagement.SpringBoot.service.User;

import com.TaskManagement.SpringBoot.exception.EmailAlreadyExistsException;
import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.repository.TicketClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

@Service
public class UserServiceClient {

    @Autowired
    private UserClientRepository userClientRepository;


    @Autowired
    private TicketClientRepository ticketClientRepository;

    // Register Client
    public UserClient registerClient(String fullName,
                                     String email,
                                     String passwordHash,
                                     String mobileNumber,
                                     String companyName,
                                     String address) {

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (userClientRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("This email is already registered.");
        }

        if (mobileNumber.length() != 10 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must enter only 10 numbers.");
        }

        if (userClientRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This mobile number is already registered.");
        }


        UserClient client = new UserClient();
        client.setFullName(fullName);
        client.setEmail(email);
        client.setPasswordHash(passwordHash);
        client.setMobileNumber(mobileNumber);
        client.setCompanyName(companyName);
        client.setAddress(address);
        client.setRole(Role.CLIENT);
        return userClientRepository.save(client);
    }

    public Optional<UserClient> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    public Optional<UserClient> findById(Long id) {
        return userClientRepository.findById(id);
    }


    // Get all Client
    public List<UserClient> getAllClients() {
        return userClientRepository.findAll();
    }

    // Get a client by ID
    public Optional<UserClient> getClientById(Long id) {
        return userClientRepository.findById(id);
    }


    public boolean existsById(Long clientId) {
        return userClientRepository.existsById(clientId);
    }


    // delete Client
    @Transactional
    public void deleteClient(Long clientId) {
        UserClient client = userClientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (ticketClientRepository.existsByClient(client)) {
            throw new ResourceLockedException("Cannot delete Client because there are tickets assigned.");
        }

        userClientRepository.delete(client);
    }
}
