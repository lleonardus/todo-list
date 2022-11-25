package com.leonardus.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardus.todolist.dto.TaskDTO;
import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.factory.TaskFactory;
import com.leonardus.todolist.service.TaskService;
import com.leonardus.todolist.service.exceptions.DataIntegrityViolationException;
import com.leonardus.todolist.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    public static final String TITLE = "title";
    public static final long ID = 1L;
    public static final String STATUS = "status";
    public static final long NON_EXISTING_ID = 2L;
    public static final String INVALID_STATUS = "invalid status";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TaskService service;

    @MockBean
    ModelMapper mapper;

    TaskDTO taskDTO;
    Task task;

    String json;

    @BeforeEach
    void setUp() throws Exception {
        task = TaskFactory.createTask();
        taskDTO = TaskFactory.createTaskDTO();

        json = objectMapper.writeValueAsString(taskDTO);

        when(service.findAllTasks()).thenReturn(List.of(task));
        when(service.findTasksByTitle(TITLE)).thenReturn(List.of(task));
        when(service.findTaskById(ID)).thenReturn(task);
        when(service.findTaskById(NON_EXISTING_ID)).thenThrow(ObjectNotFoundException.class);
        when(service.createTask(taskDTO)).thenReturn(task);
        when(service.updateTask(ID, taskDTO)).thenReturn(task);
        when(service.updateTask(NON_EXISTING_ID, taskDTO)).thenThrow(ObjectNotFoundException.class);
        when(service.updateStatus(ID, STATUS)).thenReturn(task);
        when(service.updateStatus(NON_EXISTING_ID, STATUS)).thenThrow(ObjectNotFoundException.class);
        when(service.updateStatus(ID, INVALID_STATUS)).thenThrow(DataIntegrityViolationException.class);

        when(mapper.map(any(), any())).thenReturn(taskDTO);

        doNothing().when(service).deleteTaskById(ID);
        doThrow(ObjectNotFoundException.class).when(service).deleteTaskById(NON_EXISTING_ID);
    }

    @Test
    void findAllTasks_ReturnsAListOfAllTasks() throws Exception{
        json = objectMapper.writeValueAsString(List.of(taskDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void findTasksByTitle_ReturnsAListOfTasksWithTheSameTitle() throws Exception{
        json = objectMapper.writeValueAsString(List.of(taskDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/title/{title}", TITLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void findTaskById_ReturnsATask_WhenSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void findTaskById_ThrowsObjectNotFoundException_WhenNotSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/id/{id}", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTask_ReturnsATask_WhenSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void createTask_ThrowsMethodArgumentNotValidException_WhenTitleIsNull() throws Exception{
        taskDTO.setTitle(null);
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createTask_ThrowsMethodArgumentNotValidException_WhenTitleHasInvalidSize() throws Exception{
        taskDTO.setTitle("");
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createTask_ThrowsMethodArgumentNotValidException_WhenDescriptionIsNull() throws Exception{
        taskDTO.setDescription(null);
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createTask_ThrowsMethodArgumentNotValidException_WhenDescriptionHasInvalidSize() throws Exception{
        taskDTO.setDescription("");
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTask_ReturnsATask_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void updateTask_ThrowsObjectNotFoundException_WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateTask_ThrowsMethodArgumentNotValidException_WhenTitleIsNull() throws Exception{
        taskDTO.setTitle(null);
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTask_ThrowsMethodArgumentNotValidException_WhenTitleHasInvalidSize() throws Exception{
        taskDTO.setTitle("");
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTask_ThrowsMethodArgumentNotValidException_WhenDescriptionIsNull() throws Exception{
        taskDTO.setDescription(null);
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateTask_ThrowsMethodArgumentNotValidException_WhenDescriptionHasInvalidSize() throws Exception{
        taskDTO.setDescription("");
        json = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStatus_ReturnsATask_WhenSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}/status/{status}", ID, STATUS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void updateStatus_ThrowsDataIntegrityViolationException_WhenStatusIsInvalid() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}/status/{status}", ID, INVALID_STATUS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateStatus_ThrowsObjectNotFoundException_WhenTaskDoesNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/id/{id}/status/{status}", NON_EXISTING_ID, STATUS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTaskById_DoesNothing_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteTaskById_ThrowsObjectNotFoundException_WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/id/{id}", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}