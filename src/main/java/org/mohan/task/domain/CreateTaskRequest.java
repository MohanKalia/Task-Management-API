package org.mohan.task.domain;


import org.mohan.task.domain.entity.TaskPriority;

import java.time.LocalDate;

public record CreateTaskRequest(
        String title,
        String description,
        LocalDate duedate,
        TaskPriority priority
) {
}
