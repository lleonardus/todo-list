package com.leonardus.todolist.config;

import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.entities.enums.TaskStatus;
import com.leonardus.todolist.repository.TaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
@Log4j2
public class TestConfig {
    @Bean
    CommandLineRunner initDataBase(TaskRepository repository){
        Task t1 = Task.builder()
                .title("task 1")
                .description("first task")
                .status(TaskStatus.TO_DO)
                .deadline(new Date())
                .build();

        Task t2 = Task.builder()
                .title("task 2")
                .description("second task")
                .status(TaskStatus.TO_DO)
                .deadline(new Date())
                .build();

        Task t3 = Task.builder()
                .title("task 3")
                .description("third task")
                .status(TaskStatus.TO_DO)
                .deadline(new Date())
                .build();

        repository.saveAll(List.of(t1, t2, t3));

        return args -> log.info("H2 database started successfully!");
    }
}
