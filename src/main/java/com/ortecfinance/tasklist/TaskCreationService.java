package com.ortecfinance.tasklist;

public class TaskCreationService {
    private final TaskRepository taskRepository;

    public TaskCreationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(String project, String description) {
        this.taskRepository.addTask(project, description);
    }
}
