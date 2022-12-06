package com.leonardus.todolist.dto;

import com.leonardus.todolist.entities.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @NotBlank(message = "The title field is required")
    private String title;
    @NotBlank(message = "The description field is required")
    private String description;
    private Date deadline;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}