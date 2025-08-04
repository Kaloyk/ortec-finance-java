package com.ortecfinance.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStatusServiceTest {
    private TaskRepository taskRepository;
    private ProjectCreationService projectCreationService;
    private TaskCreationService taskCreationService;
    private TaskStatusService taskStatusService;

    @BeforeEach
    void setup() {
        this.taskRepository = new TaskRepository();
        this.projectCreationService = new ProjectCreationService(this.taskRepository);
        this.taskCreationService = new TaskCreationService(this.taskRepository);
        this.taskStatusService = new TaskStatusService(this.taskRepository);
    }

    @Test
    void taskCheckUncheckTest() {
        String project = "test";
        String description = "first";

        projectCreationService.addProject(project);
        Long id = taskCreationService.addTask(project, description);

        taskStatusService.check(id);
        assertTrue(taskRepository.findById(id).get().isDone());

        taskStatusService.uncheck(id);
        assertFalse(taskRepository.findById(id).get().isDone());
    }
}
