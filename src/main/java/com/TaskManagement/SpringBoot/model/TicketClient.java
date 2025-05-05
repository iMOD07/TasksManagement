package com.TaskManagement.SpringBoot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
@Getter
@Setter
public class TicketClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserEmployee assignedTo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private UserClient client;


}
