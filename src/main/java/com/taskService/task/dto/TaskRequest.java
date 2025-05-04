package com.taskService.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank(message = "title is mandatory")
    private String title;

    @Size(max = 500)
    @NotBlank(message = "description is mandatory")
    private String description;

    @NotNull(message = "due date is mandatory")
    private LocalDate dueDate;
}