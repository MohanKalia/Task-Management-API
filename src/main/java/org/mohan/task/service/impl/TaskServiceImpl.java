package org.mohan.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.entity.Task;
import org.mohan.task.domain.entity.TaskStatus;
import org.mohan.task.repository.TaskRepository;
import org.mohan.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(CreateTaskRequest request) {
        Instant now = Instant.now();
        Task task = new Task(
                null, // hibernate will automacally generate a UUID
                request.title(),
                request.description(),
                request.duedate(),
                TaskStatus.OPEN,
                request.priority(),
                now,
                now
        );
        return taskRepository.save(task);
    }
}
