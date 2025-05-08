package com.TaskManagement.SpringBoot.service.Tasks;

import com.TaskManagement.SpringBoot.model.Task;
import com.TaskManagement.SpringBoot.model.UserClient;
import com.TaskManagement.SpringBoot.model.UserEmployee;
import com.TaskManagement.SpringBoot.repository.TaskRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserClientRepository;
import com.TaskManagement.SpringBoot.repository.Users.UserEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserEmployeeRepository userEmployeeRepository;

    @Autowired
    private UserClientRepository userClientRepository;

    // Create a new task (used by Admin)
    public Task createTask(String title,
                           String description,
                           String status,
                           Long assignedToId,
                           LocalDateTime dueDate,
                           Long connect_to) {
        Optional<UserEmployee> user = userEmployeeRepository.findById(assignedToId);
        Optional<UserClient> userClient = userClientRepository.findById(connect_to);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        if (userClient.isEmpty()) {
            throw new RuntimeException("Client not found!");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setAssignedTo(user.get());
        task.setDueDate(dueDate);
        task.setConnect_to(userClient.get());
        return taskRepository.save(task);
    }

    // Get All Tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks");
        }
        return tasks;
    }


    // Get Tasks By User
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }


    // Update tasks in use (role admin only)
    public Task updateTask(Long taskId,
                           String title,
                           String description,
                           String status,
                           LocalDateTime dueDate) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }

        Task task = taskOptional.get();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setDueDate(dueDate);
        return taskRepository.save(task);
    }

    // Delete task (used by Admin)
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found!");
        }
        taskRepository.deleteById(taskId);
    }

    // Transfer a task to another employee (used by Employee)
    public Task transferTask(Long taskId, Long newAssignedToId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }
        Optional<UserEmployee> newUser = userEmployeeRepository.findById(newAssignedToId);
        if (newUser.isEmpty()) {
            throw new RuntimeException("Target user not found!");
        }
        Task task = taskOptional.get();
        task.setAssignedTo(newUser.get());
        return taskRepository.save(task);
    }

    // End Tasks by ADMIN or CLIENT (State to Completed)
    public Task completeTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found!");
        }
        Task task = taskOptional.get();
        task.setStatus("Completed");
        return taskRepository.save(task);
    }
}
