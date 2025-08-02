package com.ortecfinance.tasklist;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectCreationService {
    private final TaskRepository taskRepository;

    public ProjectCreationService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addProject(String project) {
        this.taskRepository.addProject(project);
    }

    public Map<String, List<String>> getAll() {
        return this.taskRepository.getAll().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        p -> p.getValue().stream()
                                .map(Task::getDescription)
                                .collect(Collectors.toList())
                ));
    }
}
