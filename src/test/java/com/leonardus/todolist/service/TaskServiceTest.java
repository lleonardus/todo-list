package com.leonardus.todolist.service;

import com.leonardus.todolist.dto.TaskDTO;
import com.leonardus.todolist.entities.Task;
import com.leonardus.todolist.entities.enums.TaskStatus;
import com.leonardus.todolist.factory.TaskFactory;
import com.leonardus.todolist.repository.TaskRepository;
import com.leonardus.todolist.service.exceptions.DataIntegrityViolationException;
import com.leonardus.todolist.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService service;

    @Mock
    TaskRepository repository;

    @Mock
    ModelMapper mapper;

    public static final String TITLE = "title";
    public static final Long ID = 1L;
    public static final Long NON_EXISTING_ID = 2L;

    Task task;
    TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = TaskFactory.createTask();
        taskDTO = TaskFactory.createTaskDTO();

        when(repository.findAll()).thenReturn(List.of(task));
        when(repository.findByTitleContainingIgnoreCase(TITLE)).thenReturn(List.of(task));
        when(repository.findById(ID)).thenReturn(Optional.of(task));
        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(repository.save(task)).thenReturn(task);

        when(mapper.map(taskDTO, Task.class)).thenReturn(task);

        doNothing().when(repository).delete(task);
    }

    @Test
    void findAllTasks_ReturnsAListOfAllTasks() {
        List<Task> response = service.findAllTasks();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertNotNull(response.get(0));
        assertEquals(task, response.get(0));
    }

    @Test
    void findTasksByTitle_ReturnsAListOfTasksWithTheSameTitle() {
        List<Task> response = service.findTasksByTitle(task.getTitle());

        assertNotNull(response);
        assertEquals(1, response.size());
        assertNotNull(response.get(0));
        assertEquals(task, response.get(0));
    }

    @Test
    void findTaskById_ReturnsATask_WhenSuccessful() {
        Task response = service.findTaskById(ID);

        assertNotNull(response);
        assertEquals(task, response);
    }

    @Test
    void findTaskById_ThrowsObjectNotFoundException_WhenNotSuccessful() {
        assertThrows(ObjectNotFoundException.class, () -> service.findTaskById(NON_EXISTING_ID));
    }

    @Test
    void createTask_ReturnsATask_WhenDeadLineIsNotNull() {
        Task response = service.createTask(taskDTO);

        assertNotNull(response);
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertEquals(task.getDeadLine(), response.getDeadLine());
        assertEquals(TaskStatus.TO_DO, response.getStatus());
        assertEquals(task.getCreatedAt(), response.getCreatedAt());
        assertEquals(task.getUpdatedAt(), response.getUpdatedAt());
    }

    @Test
    void createTask_ReturnsATask_WhenDeadLineIsNull() {
        taskDTO.setDeadLine(null);
        Task response = service.createTask(taskDTO);

        assertNotNull(response);
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertNotNull(response.getDeadLine());
        assertEquals(TaskStatus.TO_DO, response.getStatus());
        assertEquals(task.getCreatedAt(), response.getCreatedAt());
        assertEquals(task.getUpdatedAt(), response.getUpdatedAt());
    }

    @Test
    void updateTask_ReturnsATask_WhenDeadLineIsNotNull() {
        taskDTO.setTitle("new title");
        taskDTO.setDescription("new description");
        taskDTO.setDeadLine(new Date());

        Task response = service.updateTask(ID, taskDTO);

        assertNotNull(response);
        assertEquals("new title", response.getTitle());
        assertEquals("new description", response.getDescription());
        assertNotNull(response.getDeadLine());
    }

    @Test
    void updateTask_ReturnsATask_WhenDeadLineIsNull() {
        taskDTO.setTitle("new title");
        taskDTO.setDescription("new description");
        taskDTO.setDeadLine(null);

        Task response = service.updateTask(ID, taskDTO);

        assertNotNull(response);
        assertEquals("new title", response.getTitle());
        assertEquals("new description", response.getDescription());
        assertNotNull(response.getDeadLine());
    }

    @Test
    void updateTask_ThrowsObjectNotFoundException_WhenTaskDoesNotExist() {
        assertThrows(ObjectNotFoundException.class, () -> service.updateTask(NON_EXISTING_ID, taskDTO));
    }

    @Test
    void updateStatus_UpdatesStatusToTODO_WhenSuccessful() {
        Task response = service.updateStatus(ID, "to-do");
        assertEquals(TaskStatus.TO_DO, response.getStatus());
    }

    @Test
    void updateStatus_UpdatesStatusToDOING_WhenSuccessful() {
        Task response = service.updateStatus(ID, "doing");
        assertEquals(TaskStatus.DOING, response.getStatus());
    }

    @Test
    void updateStatus_UpdatesStatusToDONE_WhenSuccessful() {
        Task response = service.updateStatus(ID, "done");
        assertEquals(TaskStatus.DONE, response.getStatus());
    }

    @Test
    void updateStatus_ThrowsDataIntegrityViolationException_WhenStatusIsInvalid() {
        assertThrows(DataIntegrityViolationException.class, () -> service.updateStatus(ID, "invalid"));
    }

    @Test
    void updateStatus_ThrowsObjectNotFoundException_WhenTaskDoesNotExist() {
        assertThrows(ObjectNotFoundException.class, () -> service.updateStatus(NON_EXISTING_ID, "doing"));
    }

    @Test
    void deleteTaskById_DoesNothing_WhenSuccessful() {
        service.deleteTaskById(ID);
        verify(repository, times(1)).delete(task);
    }

    @Test
    void deleteTaskById_ThrowsObjectNotFoundException_WhenNotSuccessful() {
        assertThrows(ObjectNotFoundException.class, () -> service.deleteTaskById(NON_EXISTING_ID));
    }
}