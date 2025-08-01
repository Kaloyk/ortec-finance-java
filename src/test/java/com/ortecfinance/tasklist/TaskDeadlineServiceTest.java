package com.ortecfinance.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskDeadlineServiceTest {
    private TaskRepository taskRepository;
    private ProjectCreationService projectCreationService;
    private TaskCreationService taskCreationService;
    private TaskDeadlineService taskDeadlineService;

    @BeforeEach
    void setup() {
        this.taskRepository = new TaskRepository();
        this.projectCreationService = new ProjectCreationService(this.taskRepository);
        this.taskCreationService = new TaskCreationService(this.taskRepository);
        this.taskDeadlineService = new TaskDeadlineService(this.taskRepository);
    }

    @Test
    void tasksDeadlineTodayTest() {
        String project = "test";
        String description = "first";

        projectCreationService.addProject(project);
        taskCreationService.addTask(project, description);

        assertEquals(0, taskDeadlineService.getTasksForToday().size());

        taskDeadlineService.setDeadline(1, LocalDate.now());
        assertEquals(1, taskDeadlineService.getTasksForToday().size());
    }

    @Test
    void tasksViewByDeadlineTest() {
        String project = "test";
        String description = "first";

        projectCreationService.addProject(project);
        taskCreationService.addTask(project, description);

        assertTrue(taskDeadlineService.getGroupedByDeadline().containsKey("No deadline"));

        taskDeadlineService.setDeadline(1, LocalDate.now());
        assertTrue(taskDeadlineService.getGroupedByDeadline().containsKey(LocalDate.now().toString()));
        assertFalse(taskDeadlineService.getGroupedByDeadline().containsKey("No deadline"));
    }
}
