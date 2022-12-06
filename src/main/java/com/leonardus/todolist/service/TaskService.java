package com.leonardus.todolist.service;

import com.leonardus.todolist.dto.TaskDTO;
import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.entities.enums.TaskStatus;
import com.leonardus.todolist.repository.TaskRepository;
import com.leonardus.todolist.service.exceptions.DataIntegrityViolationException;
import com.leonardus.todolist.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository repository;

    @Autowired
    ModelMapper mapper;

    public List<Task> findAllTasks(){return repository.findAll();}

    public List<Task> findTasksByTitle(String title){
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public Task findTaskById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Task not found"));
    }

    public Task createTask(TaskDTO taskDTO){
        taskDTO.setStatus(TaskStatus.TO_DO);

        if (taskDTO.getDeadline() == null){
            taskDTO.setDeadline(new Date());
        }

        return repository.save(mapper.map(taskDTO, Task.class));
    }

    public Task updateTask(Long id, TaskDTO taskDTO){
        Task task = this.findTaskById(id);

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());

        if (taskDTO.getDeadline() != null) {
            task.setDeadline(taskDTO.getDeadline());
        }

        return repository.save(task);
    }

    public Task updateStatus(Long id, String status){
        Task task = this.findTaskById(id);

        switch (status){
            case "to-do" -> task.setStatus(TaskStatus.TO_DO);
            case "doing" -> task.setStatus(TaskStatus.DOING);
            case "done" -> task.setStatus(TaskStatus.DONE);
            default -> throw new DataIntegrityViolationException("Invalid status");
        }

        return repository.save(task);
    }

    public void deleteTaskById(Long id){
        Task task = this.findTaskById(id);
        repository.delete(task);
    }
}
