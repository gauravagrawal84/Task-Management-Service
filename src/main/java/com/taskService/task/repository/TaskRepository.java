package com.taskService.task.repository;

import com.taskService.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}