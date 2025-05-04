package com.taskService.task.exception;

public class TaskDeletionException extends Exception {

    public TaskDeletionException(String message) {
        super(message);
    }

    public TaskDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}