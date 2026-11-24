package org.mohan.task.mapper.impl;

import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.UpdateTaskRequest;
import org.mohan.task.domain.dto.CreateTaskRequestDto;
import org.mohan.task.domain.dto.TaskDto;
import org.mohan.task.domain.dto.UpdateTaskRequestDto;
import org.mohan.task.domain.entity.Task;
import org.mohan.task.mapper.TaskMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskMapperImpl implements TaskMapper {
    @Override
    public CreateTaskRequest fromDto(CreateTaskRequestDto dto) {
        return new CreateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.priority()
        );
    }

    @Override
    public UpdateTaskRequest fromDto(UpdateTaskRequestDto dto) {
        return new UpdateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.status(),
                dto.priority()
        );
    }

    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getPriority(),
                task.getCreated(),
                task.getUpdated()
        );
    }
}
