package com.ortecfinance.tasklist;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@Service
public class TaskCreationService {
    private final TaskRepository taskRepository;

    public TaskCreationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Long addTask(String project, String description) {
        return this.taskRepository.addTask(project, description);
    }
}
