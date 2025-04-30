package com.TaskManagement.SpringBoot.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private Long assignedToId;
    private LocalDateTime dueDate;
    private Long connect_to;

}