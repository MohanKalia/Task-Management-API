package org.mohan.task.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.mohan.task.domain.entity.TaskPriority;

import java.time.LocalDate;

public record CreateTaskRequestDto(
        @NotBlank(message = ERROR_EXCEEDING_TITLE_CHAR_LENGTH)
        @Length(max = 255, message = ERROR_EXCEEDING_TITLE_CHAR_LENGTH)
        String title,
        @Length(max = 1000, message = ERROR_MESSAGE_DESCRIPTION_LENGTH)
        @Nullable
        String description,
        @Nullable
        @FutureOrPresent(message = ERROR_MESSAGE_DUE_DATE_FUTURE)
        LocalDate dueDate,
        @NotNull(message = ERROR_MESSAGE_TASK_PRIORITY)
        TaskPriority priority
) {
    private static final String ERROR_EXCEEDING_TITLE_CHAR_LENGTH =
            "Title must be between 1 and 255 characters.";
    private static final String ERROR_MESSAGE_DESCRIPTION_LENGTH =
            "Description must be less than 1000 characters.";
    private static final String ERROR_MESSAGE_DUE_DATE_FUTURE =
            "The due date should be in the future.";
    private static final String ERROR_MESSAGE_TASK_PRIORITY =
            "The task priority must be provided";
}
