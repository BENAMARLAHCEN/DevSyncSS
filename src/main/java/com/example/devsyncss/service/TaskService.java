package com.example.devsyncss.service;


import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.repository.TagRepository;
import com.example.devsyncss.repository.TaskRepository;
import com.example.devsyncss.repository.interfc.ITagRepository;
import com.example.devsyncss.repository.interfc.ITaskRepository;
import com.example.devsyncss.service.interfc.ITaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService implements ITaskService {
    public final ITaskRepository taskRepository;
    public final ITagRepository tagRepository;

    public TaskService() {
        this.taskRepository = new TaskRepository();
        this.tagRepository = new TagRepository();
    }

    public TaskService(ITaskRepository taskRepository, ITagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.createTask(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks();
    }

    public List<Task> searchTasks(String searchTerm) {
        return taskRepository.searchTasks(searchTerm);
    }

    public Double getUserProductivityRate(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.getTaskCompletionRate(user, startDate, endDate);
    }

    public List<Task> getStagnantTasks(int daysWithoutUpdate) {
        LocalDateTime stagnantDate = LocalDateTime.now().minusDays(daysWithoutUpdate);
        return taskRepository.findStagnantTasks(stagnantDate);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.getTask(taskId);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteTask(taskId);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    public List<Task> getUserCreatedTasks(User user) {
        return taskRepository.getUserCreatedTasks(user);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepository.getUserTasks(user);
    }
}