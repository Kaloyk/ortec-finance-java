package com.ortecfinance.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskCreationServiceTest {
    private TaskRepository taskRepository;
    private ProjectCreationService projectCreationService;
    private TaskCreationService taskCreationService;

    @BeforeEach
    void setup() {
        this.taskRepository = new TaskRepository();
        this.projectCreationService = new ProjectCreationService(this.taskRepository);
        this.taskCreationService = new TaskCreationService(this.taskRepository);
    }

    @Test
    void taskCreationTest() {
        String project = "test";
        String taskOne = "first";
        String taskTwo = "second";

        projectCreationService.addProject(project);

        taskCreationService.addTask(project, taskOne);
        taskCreationService.addTask(project, taskTwo);

        assertTrue(taskRepository.findById(1).isPresent());
        assertEquals(taskOne, taskRepository.findById(1).get().getDescription());
        assertTrue(taskRepository.findById(2).isPresent());
        assertEquals(taskTwo, taskRepository.findById(2).get().getDescription());
    }
}
