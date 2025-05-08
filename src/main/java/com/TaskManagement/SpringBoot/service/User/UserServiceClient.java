package com.TaskManagement.SpringBoot.service.User;

import com.TaskManagement.SpringBoot.exception.ResourceLockedException;
import com.TaskManagement.SpringBoot.exception.ResourceNotFoundException;
import com.TaskManagement.SpringBoot.repository.TicketClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TaskManagement.SpringBoot.model.Role;
import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;

import java.util.Optional;
import java.util.List;

@Service
public class UserServiceClient {

    @Autowired
    private UserClientRepository clientRepository;

    @Autowired
    private TicketClientRepository ticketRepository;

    // Register Client
    public UserClient registerClient(String fullName,
                                     String email,
                                     String passwordHash,
                                     String mobileNumber,
                                     String companyName,
                                     String address) {
        UserClient client = new UserClient();
        client.setFullName(fullName);
        client.setEmail(email);
        client.setPasswordHash(passwordHash);
        client.setMobileNumber(mobileNumber);
        client.setCompanyName(companyName);
        client.setAddress(address);
        client.setRole(Role.CLIENT);
        return clientRepository.save(client);
    }

    public Optional<UserClient> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<UserClient> findById(Long id) {
        return clientRepository.findById(id);
    }


    // Get all Client
    public List<UserClient> getAllClients() {
        return clientRepository.findAll();
    }

    // Get a client by ID
    public Optional<UserClient> getClientById(Long id) {
        return clientRepository.findById(id);
    }


    public boolean existsById(Long clientId) {
        return clientRepository.existsById(clientId);
    }


    // delete Client
    @Transactional
    public void deleteClient(Long clientId) {
        UserClient client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (ticketRepository.countByClientId(clientId) > 0) {
            throw new ResourceLockedException("Cannot delete Client because there are tickets assigned.");
        }

        clientRepository.delete(client);
    }

}
