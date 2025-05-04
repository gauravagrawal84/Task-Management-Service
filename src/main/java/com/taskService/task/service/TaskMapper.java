//package com.taskService.task.service;
//
//import com.taskService.task.dto.TaskRequest;
//import com.taskService.task.dto.TaskResponse;
//import com.taskService.task.entity.Task;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Component
//public class TaskMapper {
//
//    public Task toEntity(TaskRequest request) {
//        return Task.builder()
//                .title(request.getTitle())
//                .description(request.getDescription())
//                .dueDate(request.getDueDate())
//                .build();
//    }
//
//    public TaskResponse toResponse(Task task) {
//        return TaskResponse.builder()
//                .id(task.getId())
//                .title(task.getTitle())
//                .description(task.getDescription())
//                .dueDate(task.getDueDate())
//                .build();
//    }
//
//
//    public List<TaskResponse> toResponseList(List<Task> tasks) {
//        return tasks.stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//}

package com.taskService.task.service;

import com.taskService.task.dto.TaskRequest;
import com.taskService.task.dto.TaskResponse;
import com.taskService.task.entity.TaskEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a mapper class for mapping responses for task service class
 *
 */

@Slf4j
@Component
public class TaskMapper {

    /**
     * Maps a TaskRequest DTO to a Task entity.
     *
     * @param request the incoming task request
     * @return the mapped Task entity
     */
    public TaskEntity toEntity(TaskRequest request) {
        log.debug("Mapping Task Request to Task Entity: {}", request);
        return TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .build();
    }

    /**
     * Maps a Task entity to a TaskResponse DTO.
     *
     * @param task the task entity
     * @return the corresponding TaskResponse DTO
     */
    public TaskResponse toResponse(TaskEntity task) {
        log.info("Mapping Task entity to TaskResponse for task id: {}", task.getId());
        log.debug("Mapping Task entity to TaskResponse: {}", task);
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .build();
    }

    /**
     * Maps a list of Task entities to a list of TaskResponse DTOs.
     *
     * @param tasks the list of task entities
     * @return a list of corresponding TaskResponse DTOs
     */
    public List<TaskResponse> toResponseList(List<TaskEntity> tasks) {
        log.info("Mapping list of Task entities to list of TaskResponses. Count: {}", tasks.size());
        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }
}
