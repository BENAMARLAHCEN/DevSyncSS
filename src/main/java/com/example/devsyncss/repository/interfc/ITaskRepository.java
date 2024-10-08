package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ITaskRepository {
    List<Task> findOverdueTasks();

    List<Task> findTasksDueSoon(LocalDateTime endDate);

    List<Object[]> countTasksByStatusForUser(User user);

    List<Task> searchTasks(String searchTerm);

    Double getTaskCompletionRate(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Task> findStagnantTasks(LocalDateTime stagnantDate);

    List<Task> getAllTasks();

    Task createTask(Task task);

    Task getTask(Long id);

    void deleteTask(Long id);

    void updateTask(Task task);

    List<Task> getUserCreatedTasks(User user);
}
