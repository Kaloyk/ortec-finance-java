package com.ortecfinance.tasklist;

import java.time.LocalDate;
import java.util.*;

public class TaskDeadlineService {
    private final TaskRepository taskRepository;

    public TaskDeadlineService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setDeadline(long id, LocalDate deadline) {
        taskRepository.findById(id).ifPresent(t -> t.setDeadline(deadline));
    }

    public Map<String, List<Task>> getTasksForToday() {
        LocalDate today = LocalDate.now();

        Map<String, List<Task>> todayTasks = new LinkedHashMap<>();
        for (Map.Entry<String, List<Task>> project : this.taskRepository.getAll().entrySet()) {
            List<Task> tasksTodayForProject = project.getValue().stream()
                    .filter(task -> today.equals(task.getDeadline()))
                    .toList();

            if (!tasksTodayForProject.isEmpty()) {
                todayTasks.put(project.getKey(), tasksTodayForProject);
            }
        }

        return todayTasks;
    }

    public Map<String, Map<String, List<Task>>> getGroupedByDeadline() {
        Map<String, Map<String, List<Task>>> grouped = new TreeMap<>();

        for (Map.Entry<String, List<Task>> project : taskRepository.getAll().entrySet()) {
            for (Task task : project.getValue()) {
                String deadline = task.getDeadline() == null ? "No deadline" : task.getDeadline().toString();
                grouped.computeIfAbsent(deadline, k -> new LinkedHashMap<>())
                        .computeIfAbsent(project.getKey(), l -> new ArrayList<>())
                        .add(task);
            }
        }

        return grouped;
    }
}
