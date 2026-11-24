package org.mohan.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mohan.task.domain.CreateTaskRequest;
import org.mohan.task.domain.UpdateTaskRequest;
import org.mohan.task.domain.dto.CreateTaskRequestDto;
import org.mohan.task.domain.dto.TaskDto;
import org.mohan.task.domain.dto.UpdateTaskRequestDto;
import org.mohan.task.domain.entity.Task;
import org.mohan.task.mapper.TaskMapper;
import org.mohan.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> listTasks() {
        List<Task> listTask = taskService.listTasks();
        List<TaskDto> listTaskDto = listTask.stream().map(taskMapper::toDto).toList();
        return new ResponseEntity<>(listTaskDto, HttpStatus.OK);
    }

    @PutMapping(path = "/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskRequestDto dto
    ) {
        UpdateTaskRequest updateTaskRequest = taskMapper.fromDto(dto);
        Task task = taskService.updateTask(taskId, updateTaskRequest);
        TaskDto taskDto = taskMapper.toDto(task);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID uuid) {
        taskService.deleteTask(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
