package com.taskService.task.controller;

import com.taskService.task.controller.specification.TaskApi;
import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.exception.TaskDeletionException;
import com.taskService.task.exception.TaskRetrievalException;
import com.taskService.task.exception.TaskSaveException;
import com.taskService.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a controller used for routes regarding task
 *
 */
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class TaskController implements TaskApi {
    private final TaskService taskService;

    /**
     * create task and save it to db.
     *
     * @return a TaskResponse
     */
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) throws TaskSaveException {
        log.info("Received request to create task###: {}", request);
        TaskResponse response = taskService.createTask(request);
        log.info("Task created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks
     */
    public List<TaskResponse> getAll() throws TaskRetrievalException {
        List<TaskResponse> tasks = taskService.getAllTasks();
        log.info("Total tasks found: {}", tasks.size());
        return tasks;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task
     * @return the task with the given ID
     */
    public TaskResponse getById(Long id) throws TaskRetrievalException {
        log.info("Fetching task with ID: {}", id);
        TaskResponse task = taskService.getTaskById(id);
        log.info("Successfully fetch task for id: {}", task.getId());
        return task;
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id the ID of the task to update
     * @param request the new task data
     * @return the updated task
     */
    public TaskResponse update(Long id,TaskRequest request) throws TaskSaveException {
        log.info("Received request to update task for id: {}", id);
        TaskResponse updatedTask = taskService.updateTask(id, request);
        log.info("Task updated: {}", updatedTask);
        return updatedTask;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return an empty response with HTTP 204 status
     */
    public ResponseEntity<Void> delete(Long id) throws TaskDeletionException {
        taskService.deleteTask(id);
        log.info("Task deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}