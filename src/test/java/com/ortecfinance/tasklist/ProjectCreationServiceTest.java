package com.ortecfinance.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectCreationServiceTest {
    private TaskRepository taskRepository;
    private ProjectCreationService projectCreationService;

    @BeforeEach
    void setup() {
        this.taskRepository = new TaskRepository();
        this.projectCreationService = new ProjectCreationService(this.taskRepository);
    }

    @Test
    void projectCreationTest() {
        String project = "test";
        projectCreationService.addProject(project);

        assertTrue(taskRepository.getAll().containsKey(project));
    }
}
