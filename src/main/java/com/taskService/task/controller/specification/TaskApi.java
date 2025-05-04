package com.taskService.task.controller.specification;

import com.taskService.task.constants.Endpoints;
import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.exception.TaskDeletionException;
import com.taskService.task.exception.TaskRetrievalException;
import com.taskService.task.exception.TaskSaveException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping(Endpoints.BASE)
public interface TaskApi {

    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error while saving the task")
    })
    @PostMapping
    ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) throws TaskSaveException;

    @Operation(summary = "Get all tasks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks"),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving tasks")
    })
    @GetMapping
    List<TaskResponse> getAll() throws TaskRetrievalException;

    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found with the provided ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving the task")
    })
    @GetMapping(Endpoints.GET_TASK_BY_ID)
    TaskResponse getById(@PathVariable Long id) throws TaskRetrievalException;


    @Operation(summary = "Update a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Task not found to update"),
            @ApiResponse(responseCode = "500", description = "Internal server error while updating the task")
    })
    @PutMapping(Endpoints.UPDATE_TASK)
    TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) throws TaskSaveException;

    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error while deleting the task")
    })
    @DeleteMapping(Endpoints.DELETE_TASK)
    ResponseEntity<Void> delete(@PathVariable Long id) throws TaskDeletionException;
}
