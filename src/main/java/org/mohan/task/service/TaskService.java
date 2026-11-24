package org.mohan.task.service;

import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.entity.Task;

public interface TaskService {
    Task createTask(CreateTaskRequest request);
}
