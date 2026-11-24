package org.mohan.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.dto.CreateTaskRequestDto;
import org.mohan.task.domain.dto.TaskDto;
import org.mohan.task.domain.entity.Task;
import org.mohan.task.mapper.TaskMapper;
import org.mohan.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequestDto dto
    ) {
        CreateTaskRequest createTaskRequest = taskMapper.fromDto(dto); // takes the body and
        // converts it into something that can be safely used a service request
        Task task = taskService.createTask(createTaskRequest); // saving it to the database
        TaskDto taskDto = taskMapper.toDto(task); // returning the task as JSON
        return new  ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

}
