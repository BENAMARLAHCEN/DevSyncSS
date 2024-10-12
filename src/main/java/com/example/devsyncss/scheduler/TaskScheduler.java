package com.example.devsyncss.scheduler;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TaskService taskService = new TaskService();

    public void startTaskScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateTasks, 0, 10, TimeUnit.MINUTES);
    }

    private void checkAndUpdateTasks() {
        List<Task> tasks = taskService.getAllTasks();
        for (Task task : tasks) {
            if (task.getDueDate().isBefore(LocalDateTime.now()) && task.getStatus() != TaskStatus.COMPLETED) {
                task.setStatus(TaskStatus.CANCELLED);
                taskService.updateTask(task);
            }
        }
    }
}
