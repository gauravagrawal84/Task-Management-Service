package com.taskService.task.constants;

/**
 * This class contains constants for all the Task API endpoints.
 */
public final class Endpoints {

    private Endpoints() {
        // Prevent instantiation
    }

    public static final String BASE = "/tasks";

    public static final String GET_TASK_BY_ID =  "/{id}";
    public static final String UPDATE_TASK = "/{id}";
    public static final String DELETE_TASK =  "/{id}";
}