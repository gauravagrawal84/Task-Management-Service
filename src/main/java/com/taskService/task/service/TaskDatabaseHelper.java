package com.taskService.task.service;

import com.taskService.task.entity.TaskEntity;
import com.taskService.task.exception.TaskDeletionException;
import com.taskService.task.exception.TaskNotFoundException;
import com.taskService.task.exception.TaskRetrievalException;
import com.taskService.task.exception.TaskSaveException;
import com.taskService.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is a helper class for db operations for task service class
 *
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskDatabaseHelper {

    private final TaskRepository taskRepository;

    /**
     * Saves a task entity to the database.
     *
     * @param task the task to save
     * @return the saved task
     */
    public TaskEntity saveTask(TaskEntity task) throws TaskSaveException {
        try {
            log.info("Saving task to database: {}", task);
            TaskEntity savedTask = taskRepository.save(task);
            log.info("Task successfully saved with ID: {}", savedTask.getId());
            return savedTask;
        } catch (Exception e) {
            log.error("Error occurred while saving task: {}", task, e);  // Log error with exception details
            throw new TaskSaveException("Failed to save task", e);  // Throw custom exception
              }
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return list of tasks
     * @throws TaskRetrievalException if there is an error during retrieval
     */
    public List<TaskEntity> findAllTasks() throws TaskRetrievalException {
        try {
            log.info("Fetching all tasks from database");
            List<TaskEntity> tasks = taskRepository.findAll();
            log.info("Successfully retrieved {} tasks from database", tasks.size());
            return tasks;
        } catch (Exception e) {
            log.error("Error occurred while fetching tasks", e);
            throw new TaskRetrievalException("Failed to retrieve tasks", e);
        }
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the task ID
     * @return the found task
     * @throws TaskNotFoundException if task is not found
     * @throws TaskRetrievalException if any unexpected error occurs
     */
    public TaskEntity findTaskById(Long id) throws TaskRetrievalException {
        try {
            log.info("Fetching task by ID: {}", id);
            return taskRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Task not found with ID: {}", id);
                        return new TaskNotFoundException(id);
                    });
        } catch (TaskNotFoundException e) {
            throw e;  // propagate as is
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching task by ID: {}", id, e);
            throw new TaskRetrievalException("Failed to fetch task with ID: " + id, e);
        }
    }




    /**
     * Updates an existing task.
     *
     * @param id task ID
     * @param updatedData updated task data
     * @return updated task
     */
    public TaskEntity updateTask(Long id, TaskEntity updatedData) throws TaskSaveException {
        try{
        log.debug("Updating task with ID: {}", id);
        TaskEntity existing = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found for update with ID: {}", id);
                    return new TaskNotFoundException(id);
                });

        existing.setTitle(updatedData.getTitle());
        existing.setDescription(updatedData.getDescription());
        existing.setDueDate(updatedData.getDueDate());

        log.debug("Saving updated task: {}", existing);
        TaskEntity updatedTask = taskRepository.save(existing);
        log.info("Task updated successfully with ID: {}", updatedTask.getId());
        return updatedTask;
        }
        catch (Exception e) {
            log.error("Error occurred while saving task: {}", updatedData, e);  // Log error with exception details
            throw new TaskSaveException("Failed to save task", e);  // Throw custom exception
        }

    }

    /**
     * Deletes a task by its ID.
     *
     * @param id task ID
     * @throws TaskNotFoundException if task does not exist
     * @throws TaskDeletionException if unexpected error occurs during deletion
     */
    public void deleteTaskById(Long id) throws TaskDeletionException {
        try {
            log.info("Deleting task by ID: {}", id);
            if (!taskRepository.existsById(id)) {
                log.warn("Task not found for deletion with ID: {}", id);
                throw new TaskNotFoundException(id);
            }
            taskRepository.deleteById(id);
            log.info("Task deleted successfully with ID: {}", id);
        } catch (TaskNotFoundException e) {
            throw e; // propagate as-is
        } catch (Exception e) {
            log.error("Unexpected error while deleting task with ID: {}", id, e);
            throw new TaskDeletionException("Failed to delete task with ID: " + id, e);
        }
    }
}
