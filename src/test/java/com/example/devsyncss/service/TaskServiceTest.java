package com.example.devsyncss.service;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.repository.interfc.ITagRepository;
import com.example.devsyncss.repository.interfc.ITaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private ITagRepository tagRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        when(taskRepository.createTask(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);
        assertEquals(task, createdTask);
        verify(taskRepository, times(1)).createTask(task);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.getAllTasks()).thenReturn(tasks);

        List<Task> retrievedTasks = taskService.getAllTasks();
        assertEquals(2, retrievedTasks.size());
        verify(taskRepository, times(1)).getAllTasks();
    }

    @Test
    void testGetOverdueTasksWhenNoTasksOverdue() {
        when(taskRepository.findOverdueTasks()).thenReturn(List.of());

        List<Task> overdueTasks = taskService.getOverdueTasks();
        assertEquals(0, overdueTasks.size(), "Expected no overdue tasks");
        verify(taskRepository, times(1)).findOverdueTasks();
    }

    @Test
    void testGetOverdueTasks() {
        List<Task> overdueTasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findOverdueTasks()).thenReturn(overdueTasks);

        List<Task> retrievedTasks = taskService.getOverdueTasks();
        assertEquals(2, retrievedTasks.size());
        verify(taskRepository, times(1)).findOverdueTasks();
    }


    @Test
    void testSearchTasks() {
        String searchTerm = "task";
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.searchTasks(searchTerm)).thenReturn(tasks);

        List<Task> searchedTasks = taskService.searchTasks(searchTerm);
        assertEquals(2, searchedTasks.size());
        verify(taskRepository, times(1)).searchTasks(searchTerm);
    }

    @Test
    void testGetUserProductivityRateWithNoTasks() {
        User user = new User();
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        when(taskRepository.getTaskCompletionRate(user, startDate, endDate)).thenReturn(0.0);

        double productivityRate = taskService.getUserProductivityRate(user, startDate, endDate);
        assertEquals(0.0, productivityRate, "Expected productivity rate to be 0 when there are no tasks");
        verify(taskRepository, times(1)).getTaskCompletionRate(user, startDate, endDate);
    }

    @Test
    void testGetUserProductivityRateWithAllTasksCompleted() {
        User user = new User();
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        when(taskRepository.getTaskCompletionRate(user, startDate, endDate)).thenReturn(1.0);

        double productivityRate = taskService.getUserProductivityRate(user, startDate, endDate);
        assertEquals(1.0, productivityRate, "Expected productivity rate to be 1 when all tasks are completed");
        verify(taskRepository, times(1)).getTaskCompletionRate(user, startDate, endDate);
    }


    @Test
    void testGetTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        when(taskRepository.getTask(taskId)).thenReturn(task);

        Task retrievedTask = taskService.getTaskById(taskId);
        assertEquals(task, retrievedTask);
        verify(taskRepository, times(1)).getTask(taskId);
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteTask(taskId);

        taskService.deleteTask(taskId);
        verify(taskRepository, times(1)).deleteTask(taskId);
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        doNothing().when(taskRepository).updateTask(task);

        taskService.updateTask(task);
        verify(taskRepository, times(1)).updateTask(task);
    }

    @Test
    void testGetUserCreatedTasks() {
        User user = new User();
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.getUserCreatedTasks(user)).thenReturn(tasks);

        List<Task> userTasks = taskService.getUserCreatedTasks(user);
        assertEquals(2, userTasks.size());
        verify(taskRepository, times(1)).getUserCreatedTasks(user);
    }
}