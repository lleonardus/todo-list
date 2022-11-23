package com.leonardus.todolist.controller.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class StandardError {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;
}
