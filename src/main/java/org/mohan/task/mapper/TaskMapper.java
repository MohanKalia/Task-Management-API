package org.mohan.task.mapper;

import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.UpdateTaskRequest;
import org.mohan.task.domain.dto.CreateTaskRequestDto;
import org.mohan.task.domain.dto.TaskDto;
import org.mohan.task.domain.dto.UpdateTaskRequestDto;
import org.mohan.task.domain.entity.Task;

public interface TaskMapper {

    CreateTaskRequest fromDto(CreateTaskRequestDto dto);
    UpdateTaskRequest fromDto(UpdateTaskRequestDto dto);
    TaskDto toDto(Task task);
}
