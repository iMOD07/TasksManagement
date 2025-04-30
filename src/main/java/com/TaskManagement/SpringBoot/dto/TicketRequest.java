package com.TaskManagement.SpringBoot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    private String title;
    private String description;
}
