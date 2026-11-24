package org.mohan.task.domain.dto;

import org.mohan.task.domain.entity.TaskPriority;
import org.mohan.task.domain.entity.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        TaskPriority priority,
        Instant created,
        Instant updated
) {
}
