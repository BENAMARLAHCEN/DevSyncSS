package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ITaskService {
    Task createTask(Task task);

    List<Task> getAllTasks();

    List<Task> getOverdueTasks();

    List<Task> getUpcomingTasks(int days);

    Map<TaskStatus, Long> getTaskStatusCountForUser(User user);

    List<Task> searchTasks(String searchTerm);

    Double getUserProductivityRate(User user, LocalDateTime startDate, LocalDateTime endDate);

    Task getTaskById(Long taskId);

    void deleteTask(Long taskId);

    void updateTask(Task task);

    List<Task> getUserCreatedTasks(User user);
}
