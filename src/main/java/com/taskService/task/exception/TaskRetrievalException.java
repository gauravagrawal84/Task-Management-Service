package com.taskService.task.exception;

public class TaskRetrievalException extends Exception {

    public TaskRetrievalException(String message) {
        super(message);
    }

    public TaskRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}