package com.taskService.task.service;

import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.exception.TaskDeletionException;
import com.taskService.task.exception.TaskRetrievalException;
import com.taskService.task.exception.TaskSaveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is a service class consist business logic related to task service
 *
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskDatabaseHelper taskDatabaseHelper;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task.
     *
     * @param request task request object
     * @return the created task response
     */
    public TaskResponse createTask(TaskRequest request) throws TaskSaveException {
        log.info("Creating task with given request: {}", request);
        return taskMapper.toResponse(taskDatabaseHelper.saveTask(taskMapper.toEntity(request)));
    }

    /**
     * Retrieves all tasks.
     *
     * @return list of task responses
     */
    public List<TaskResponse> getAllTasks() throws TaskRetrievalException {
        log.info("Fetching all tasks");
        return taskMapper.toResponseList(taskDatabaseHelper.findAllTasks());
    }

    /**
     * Retrieves task by ID.
     *
     * @param id task ID
     * @return task response
     */
    public TaskResponse getTaskById(Long id) throws TaskRetrievalException {
        return taskMapper.toResponse(taskDatabaseHelper.findTaskById(id));
    }

    /**
     * Updates an existing task.
     *
     * @param id task ID
     * @param request updated task request
     * @return updated task response
     */
    public TaskResponse updateTask(Long id, TaskRequest request) throws TaskSaveException {
        log.info("Updating task with ID: {}, new data: {}", id, request);
         TaskResponse taskResponse = taskMapper.toResponse(taskDatabaseHelper.updateTask(id, taskMapper.toEntity(request)));
         log.info("Task updated successfully with ID: {}", taskResponse.getId());
         return taskResponse;
    }

    /**
     * Deletes a task by ID.
     *
     * @param id task ID
     */
    public void deleteTask(Long id) throws TaskDeletionException {
        taskDatabaseHelper.deleteTaskById(id);
    }
}
