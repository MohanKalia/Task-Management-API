package org.mohan.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.UpdateTaskRequest;
import org.mohan.task.domain.entity.Task;
import org.mohan.task.domain.entity.TaskStatus;
import org.mohan.task.exception.TaskNotFoundException;
import org.mohan.task.repository.TaskRepository;
import org.mohan.task.service.TaskService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Task> listTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "created"));
    }

    @Override
    public Task updateTask(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setStatus(request.taskStatus());
        task.setPriority(request.taskPriority());

        return taskRepository.save(task);
    }
}
