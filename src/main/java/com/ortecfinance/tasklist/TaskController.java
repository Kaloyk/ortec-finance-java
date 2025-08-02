package com.ortecfinance.tasklist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class TaskController {
    private final ProjectCreationService projectCreationService;
    private final TaskCreationService taskCreationService;
    private final TaskDeadlineService taskDeadlineService;
    private final TaskStatusService taskStatusService;

    public TaskController(ProjectCreationService projectCreationService, TaskCreationService taskCreationService, TaskDeadlineService taskDeadlineService, TaskStatusService taskStatusService) {
        this.projectCreationService = projectCreationService;
        this.taskCreationService = taskCreationService;
        this.taskDeadlineService = taskDeadlineService;
        this.taskStatusService = taskStatusService;
    }


    @PostMapping
    public ResponseEntity<String> createProject(@RequestParam String name) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid project name");
            }
            projectCreationService.addProject(name);
            return ResponseEntity.ok(name);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getProjects() {
        try {
            Map<String, List<String>> allProjects = projectCreationService.getAll();
            if (allProjects == null || allProjects.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(allProjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{project}/tasks")
    public ResponseEntity<String> addTask(@PathVariable String project, @RequestParam String description) {
        try {
            if (project == null || project.isEmpty() || description == null || description.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid params");
            }
            taskCreationService.addTask(project, description);
            return ResponseEntity.ok("Created task " + description + " for project " + project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PutMapping("/tasks")
    public ResponseEntity<String> setDeadline(@RequestParam Long id, @RequestParam String deadline) {
        try {
            LocalDate deadlineDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            taskDeadlineService.setDeadline(id, deadlineDate);

            return ResponseEntity.ok("Set deadline for task with id " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @GetMapping("/view-by-deadline")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getGroupedByDeadline() {
        try {
            Map<String, Map<String, List<String>>> grouped = taskDeadlineService.getGroupedByDeadline();
            return ResponseEntity.ok(grouped);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
