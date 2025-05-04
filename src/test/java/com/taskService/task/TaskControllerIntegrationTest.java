package com.taskService.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskService.task.controller.TaskController;
import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.exception.TaskNotFoundException;
import com.taskService.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(TaskController.class)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST requests for task")
    class PostRequests {

        @Test
        @DisplayName("Should return 200 while creating task and a record should be created in DB")
        void should_return_200_while_creating_task_and_a_record_should_be_created_in_db() throws Exception {

            ClassPathResource resource = new ClassPathResource("dataset/create-task-data/should_return_200_for_adding_task.json");
            String addTaskRequestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            ClassPathResource resource1 = new ClassPathResource("dataset/create-task-data/create-task-response.json");
            String expectedResponse = new String(resource1.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            TaskResponse mockResponse = new TaskResponse(1L, "Title", "Description", LocalDate.of(2025, 5, 15));
            Mockito.when(taskService.createTask(Mockito.any())).thenReturn(mockResponse);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(addTaskRequestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expectedResponse)); // full body match
        }

        @Test
        @DisplayName("Should return 400 when creating task with invalid data")
        void should_return_400_when_creating_task_with_invalid_data() throws Exception {
            ClassPathResource resource = new ClassPathResource("dataset/create-task-data/invalid-create-task.json");
            String addTaskRequestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(addTaskRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when creating task with invalid description")
        void should_return_400_when_creating_task_with_invalid_description() throws Exception {
            TaskRequest request = new TaskRequest("Updated", "", LocalDate.now()); // Invalid data (empty description)

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when updating task with invalid title")
        void should_return_400_when_creating_task_with_invalid_title() throws Exception {
            TaskRequest request = new TaskRequest("", "", LocalDate.now()); // Invalid data (empty description)

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when updating task with invalid dueDate")
        void should_return_400_when_creating_task_with_invalid_dueDate() throws Exception {
            TaskRequest request = new TaskRequest("title", "description", null);

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET requests for task")
    class GetRequests {

        @Test
        @DisplayName("Should successfully fetch all the records from DB")
        void should_successfully_fetched_all_the_records_from_db() throws Exception {
            List<TaskResponse> tasks = Arrays.asList(
                    new TaskResponse(1L, "Task 1", "Desc 1", LocalDate.now()),
                    new TaskResponse(2L, "Task 2", "Desc 2", LocalDate.now())
            );
            Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()", is(2)));
        }

        @Test
        @DisplayName("Should return fetched record by given ID")
        void should_successfully_return_a_record_by_the_given_id() throws Exception {
            TaskResponse response = new TaskResponse(1L, "Task 1", "Desc 1", LocalDate.now());

            Mockito.when(taskService.getTaskById(1L)).thenReturn(response);

            mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Task 1")));
        }

        @Test
        @DisplayName("Should return 404 when fetching non-existent task by ID")
        void should_return_404_when_fetching_non_existent_task_by_id() throws Exception {
            // Simulate task not found
            Mockito.when(taskService.getTaskById(999L)).thenThrow(new TaskNotFoundException(999L));

            mockMvc.perform(get("/tasks/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 404 when fetching all tasks and no tasks exist")
        void should_return_200_when_fetching_all_tasks_and_no_tasks_exist() throws Exception {
            // Simulate no tasks found
            Mockito.when(taskService.getAllTasks()).thenReturn(List.of());

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()", is(0)));
        }
    }

    @Nested
    @DisplayName("PUT requests for task")
    class PutRequests {

        @Test
        @DisplayName("Should successfully update data in DB and return appropriate result")
        void should_successfully_update_data_in_db_and_return_appropriate_result() throws Exception {
            TaskRequest request = new TaskRequest("Updated", "Updated Desc", LocalDate.now());
            TaskResponse response = new TaskResponse(1L, "Updated", "Updated Desc", LocalDate.now());

            Mockito.when(taskService.updateTask(Mockito.eq(1L), Mockito.any())).thenReturn(response);

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Updated")));
        }

        @Test
        @DisplayName("Should return 404 when updating task that does not exist")
        void should_return_404_when_updating_task_that_does_not_exist() throws Exception {
            TaskRequest request = new TaskRequest("Updated", "Updated Desc", LocalDate.now());

            // Simulate task not found
            Mockito.when(taskService.updateTask(Mockito.eq(999L), Mockito.any())).thenThrow(new TaskNotFoundException(999L));

            mockMvc.perform(put("/tasks/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when updating task with invalid description")
        void should_return_400_when_updating_task_with_invalid_description() throws Exception {
            TaskRequest request = new TaskRequest("Updated", "", LocalDate.now()); // Invalid data (empty description)

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when updating task with invalid title")
        void should_return_400_when_updating_task_with_invalid_title() throws Exception {
            TaskRequest request = new TaskRequest("", "", LocalDate.now()); // Invalid data (empty description)

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when updating task with invalid dueDate")
        void should_return_400_when_updating_task_with_invalid_dueDate() throws Exception {
            TaskRequest request = new TaskRequest("title", "description", null);

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE requests for task")
    class DeleteRequests {

        @Test
        @DisplayName("Should successfully delete record from DB")
        void should_successfully_deleted_record_from_db() throws Exception {
            Mockito.doNothing().when(taskService).deleteTask(1L);

            mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 when deleting task that does not exist")
        void should_return_404_when_deleting_task_that_does_not_exist() throws Exception {
            // Simulate task not found for deletion
            Mockito.doThrow(new TaskNotFoundException(999L)).when(taskService).deleteTask(999L);

            mockMvc.perform(delete("/tasks/999"))
                    .andExpect(status().isNotFound());
        }
    }
}