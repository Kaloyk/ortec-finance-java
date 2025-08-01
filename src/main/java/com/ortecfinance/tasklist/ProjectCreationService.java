package com.ortecfinance.tasklist;

public class ProjectCreationService {
    private final TaskRepository taskRepository;

    public ProjectCreationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addProject(String project) {
        this.taskRepository.addProject(project);
    }
}
