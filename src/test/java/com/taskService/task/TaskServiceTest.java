package com.taskService.task;

import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.entity.TaskEntity;
import com.taskService.task.exception.TaskDeletionException;
import com.taskService.task.exception.TaskRetrievalException;
import com.taskService.task.exception.TaskSaveException;
import com.taskService.task.service.TaskDatabaseHelper;
import com.taskService.task.service.TaskMapper;
import com.taskService.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskDatabaseHelper taskDatabaseHelper;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private TaskRequest request;
    private TaskResponse response;
    private TaskEntity entity;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new TaskRequest("Test Title", "Test Description", LocalDate.now());
        entity = new TaskEntity();
        response = new TaskResponse(1L, "Test Title", "Test Description", LocalDate.now());
    }

    @Nested
    class CreateTask {

        @Test
        @DisplayName("Should return 200 while creating task and a record should be created in DB")
        void should_create_task_successfully() throws TaskSaveException {
            when(taskMapper.toEntity(request)).thenReturn(entity);
            when(taskDatabaseHelper.saveTask(entity)).thenReturn(entity);
            when(taskMapper.toResponse(entity)).thenReturn(response);

            TaskResponse result = taskService.createTask(request);

            assertEquals("Test Title", result.getTitle());
        }

        @Test
        @DisplayName("Should successfully fetch all the records from DB")
        void should_return_all_tasks() throws TaskRetrievalException {
            List<TaskEntity> entities = List.of(entity);
            List<TaskResponse> responses = List.of(response);

            when(taskDatabaseHelper.findAllTasks()).thenReturn(entities);
            when(taskMapper.toResponseList(entities)).thenReturn(responses);

            List<TaskResponse> result = taskService.getAllTasks();

            assertEquals(1, result.size());
            assertEquals("Test Title", result.get(0).getTitle());
        }
    }

    @Nested
    class GetTaskById {

        @Test
        @DisplayName("Should return fetched record by given ID")
        void should_return_task_by_id() throws TaskRetrievalException {
            when(taskDatabaseHelper.findTaskById(1L)).thenReturn(entity);
            when(taskMapper.toResponse(entity)).thenReturn(response);

            TaskResponse result = taskService.getTaskById(1L);

            assertEquals(1L, result.getId());
        }
    }

    @Nested
    class UpdateTask {

        @Test
        @DisplayName("Should successfully update data in DB and return appropriate result")
        void should_update_task_successfully() throws TaskSaveException {
            when(taskMapper.toEntity(request)).thenReturn(entity);
            when(taskDatabaseHelper.updateTask(1L, entity)).thenReturn(entity);
            when(taskMapper.toResponse(entity)).thenReturn(response);

            TaskResponse result = taskService.updateTask(1L, request);

            assertEquals("Test Title", result.getTitle());
        }
    }

    @Nested
    class DeleteTask {

        @Test
        @DisplayName("Should successfully delete record from DB")
        void should_delete_task_successfully() throws TaskDeletionException {
            doNothing().when(taskDatabaseHelper).deleteTaskById(1L);

            assertDoesNotThrow(() -> taskService.deleteTask(1L));
        }

        @Test
        @DisplayName("Should throw an exception if an error occur while deleting")
        void should_throw_exception_when_deletion_fails() throws TaskDeletionException {
            doThrow(new TaskDeletionException("Failed")).when(taskDatabaseHelper).deleteTaskById(1L);

            assertThrows(TaskDeletionException.class, () -> taskService.deleteTask(1L));
        }
    }
}

