package org.mohan.task.domain;

import org.mohan.task.domain.entity.TaskPriority;
import org.mohan.task.domain.entity.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus taskStatus,
        TaskPriority taskPriority
) {
}
