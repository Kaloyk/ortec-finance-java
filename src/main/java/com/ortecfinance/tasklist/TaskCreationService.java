package com.ortecfinance.tasklist;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@Service
public class TaskCreationService {
    private final TaskRepository taskRepository;

    public TaskCreationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(String project, String description) {
        this.taskRepository.addTask(project, description);
    }
}
