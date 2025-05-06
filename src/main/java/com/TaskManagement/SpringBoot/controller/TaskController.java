package com.TaskManagement.SpringBoot.controller;

import com.TaskManagement.SpringBoot.model.AdminUser;
import com.TaskManagement.SpringBoot.model.Task;
import com.TaskManagement.SpringBoot.dto.TaskRequest;
import com.TaskManagement.SpringBoot.dto.TransferRequest;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.UserEmployeeRepository;
import com.TaskManagement.SpringBoot.service.Tasks.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;



    // create tasks by ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request,
                                           Authentication authentication) {

        System.out.println("🔐 Authorities: " + authentication.getAuthorities());
        System.out.println("🔐 Principal class: " + authentication.getPrincipal().getClass().getSimpleName());

        Task task = taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getAssignedToId(),
                request.getDueDate(),
                request.getConnect_to()
        );

        return ResponseEntity.ok(task);

    }

    // Delete Tasks by Admin
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    // Update Tasks by Admin
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE')")
    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        Task updatedTask = taskService.updateTask(
                taskId,
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getDueDate()
        );
        return ResponseEntity.ok(updatedTask);
    }


    // transfer Tasks to another Employee
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/transfer/{taskId}")
    public ResponseEntity<Task> transferTask(@PathVariable Long taskId, @RequestBody TransferRequest request) {
        Task task = taskService.transferTask(taskId, request.getNewAssignedToId());
        return ResponseEntity.ok(task);
    }


    // Get All Tasks - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE','SUPERVISOR')")
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }


    // Get Employee by ID
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE','EMPLOYEE','SUPERVISOR')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }


    // Close Task by Client - only CLIENT
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/complete/{taskId}")
    public ResponseEntity<Task> completeTask(@PathVariable Long taskId) {
        Task completedTask = taskService.completeTask(taskId);
        return ResponseEntity.ok(completedTask);
    }

    // END Task by ADMIN - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_EMPLOYEE')")
    @PutMapping("/admin/complete/{taskId}")
    public ResponseEntity<Task> completeTaskByAdmin(@PathVariable Long taskId) {
        Task completedTask = taskService.completeTask(taskId); // تم توحيد طريقة انهاء المهمة لاختلاف التسمية
        return ResponseEntity.ok(completedTask);
    }
}
