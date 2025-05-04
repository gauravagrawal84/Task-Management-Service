package com.taskService.task.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(TaskSaveException.class)
    public ResponseEntity<String> handleTaskSaveException(TaskSaveException e) {
        log.error("Handled TaskSaveException: ", e);  // Log exception details
        return new ResponseEntity<>("Task saving failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskRetrievalException.class)
    public ResponseEntity<String> handleTaskRetrievalException(TaskRetrievalException e) {
        log.error("Handled TaskRetrievalException: ", e);
        return new ResponseEntity<>("Error retrieving tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskDeletionException.class)
    public ResponseEntity<String> handleTaskDeletionException(TaskDeletionException e) {
        log.error("Handled TaskDeletionException: ", e);
        return new ResponseEntity<>("Error deleting task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.badRequest().body(error);
    }
}