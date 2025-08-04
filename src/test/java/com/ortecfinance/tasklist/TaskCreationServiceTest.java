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

        Long idOne = taskCreationService.addTask(project, taskOne);
        Long idTwo = taskCreationService.addTask(project, taskTwo);

        assertTrue(taskRepository.findById(idOne).isPresent());
        assertEquals(taskOne, taskRepository.findById(idOne).get().getDescription());
        assertTrue(taskRepository.findById(idTwo).isPresent());
        assertEquals(taskTwo, taskRepository.findById(idTwo).get().getDescription());
    }
}
