package com.ortecfinance.tasklist;

import java.util.*;

public class TaskRepository {
    private final Map<String, List<Task>> tasks = new LinkedHashMap<>();
    private long lastId = 0;

    public void addProject(String project) {
        tasks.put(project, new ArrayList<Task>());
    }

    public void addTask(String project, String description) {
        List<Task> projectTasks = tasks.get(project);
        if (projectTasks == null) {
            return;
        }
        projectTasks.add(new Task(nextId(), description, false));
    }

    public Optional<Task> findById(long id) {
        for (List<Task> list : tasks.values()) {
            Optional<Task> task = list.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst();

            if (task.isPresent()) {
                return task;
            }
        }
        return Optional.empty();
    }

    public Map<String, List<Task>> getAll() {
        return tasks;
    }

    private long nextId() {
        return ++lastId;
    }
}
