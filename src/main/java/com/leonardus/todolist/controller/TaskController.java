package com.leonardus.todolist.controller;

import com.leonardus.todolist.dto.TaskDTO;
import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService service;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAllTasks(){
        List<Task> tasks = service.findAllTasks();
        return ResponseEntity.ok().body(tasks.stream().map(task -> mapper.map(task, TaskDTO.class)).toList());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<TaskDTO>> findTasksByTitle(@PathVariable String title){
        List<Task> tasks = service.findTasksByTitle(title);
        return ResponseEntity.ok().body(tasks.stream().map(task -> mapper.map(task, TaskDTO.class)).toList());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TaskDTO> findTaskById(@PathVariable Long id){
        Task task = service.findTaskById(id);
        return ResponseEntity.ok().body(mapper.map(task, TaskDTO.class));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO){
        Task task = service.createTask(taskDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/id/{id}").buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(mapper.map(task, TaskDTO.class));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO){
        Task task = service.updateTask(id, taskDTO);
        return ResponseEntity.ok().body(mapper.map(task, TaskDTO.class));
    }

    @PutMapping("/id/{id}/status/{status}")
    public ResponseEntity<TaskDTO> updateStatus(@PathVariable Long id, @PathVariable String status){
        Task task = service.updateStatus(id, status);
        return ResponseEntity.ok().body(mapper.map(task, TaskDTO.class));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<TaskDTO> deleteTaskById(@PathVariable Long id){
        service.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
