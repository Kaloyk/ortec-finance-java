package com.ortecfinance.tasklist;

import org.springframework.stereotype.Service;

@Service
public class TaskStatusService {
    private final TaskRepository taskRepository;

    public TaskStatusService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void check(long id){
        taskRepository.findById(id).ifPresent(t -> t.setDone(true));
    }

    public void uncheck(long id){
        taskRepository.findById(id).ifPresent(t -> t.setDone(false));
    }
}
