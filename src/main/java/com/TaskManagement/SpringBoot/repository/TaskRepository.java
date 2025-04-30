package com.TaskManagement.SpringBoot.repository;

import com.TaskManagement.SpringBoot.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    boolean existsByAssignedToId(Long employeeId);

}
