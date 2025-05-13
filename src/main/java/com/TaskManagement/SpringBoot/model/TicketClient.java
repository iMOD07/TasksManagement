package com.TaskManagement.SpringBoot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserAdmin assignedTo;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus = TicketStatus.IN_PROGRESS;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private UserClient client;

}
