package org.mohan.task.service;

import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.UpdateTaskRequest;
import org.mohan.task.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(CreateTaskRequest request);
    List<Task> listTasks();
    Task updateTask(UUID id, UpdateTaskRequest request);
    void deleteTask(UUID id);
}
