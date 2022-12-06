package com.leonardus.todolist.factory;

import com.leonardus.todolist.dto.TaskDTO;
import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.entities.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class TaskFactory {

    public static Task createTask(){
        return Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .deadline(new Date())
                .status(TaskStatus.TO_DO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TaskDTO createTaskDTO(){
        return TaskDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .deadline(new Date())
                .status(TaskStatus.TO_DO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
