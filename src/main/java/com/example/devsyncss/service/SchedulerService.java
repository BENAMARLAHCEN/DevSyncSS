package com.example.devsyncss.service;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.repository.TaskRepository;
import com.example.devsyncss.repository.TokenRepository;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Singleton
@Startup
public class SchedulerService {

    private final TaskRepository taskRepository;

    private final TokenRepository tokenRepository;

    public SchedulerService() {
        this.taskRepository = new TaskRepository();
        this.tokenRepository = new TokenRepository();
    }

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void markOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepository.getAllTasks().stream()
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED
                        && task.getStatus() != TaskStatus.CANCELLED
                        && task.getDueDate().isBefore(now))
                .collect(Collectors.toList());

        for (Task task : overdueTasks) {
            task.setStatus(TaskStatus.CANCELLED);
            taskRepository.updateTask(task);
        }
    }

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void resetDailyTokens() {
        List<Token> allTokens = tokenRepository.getAllTokens();
        for (Token token : allTokens) {
            token.setModificationTokens(2);
            tokenRepository.save(token);
        }
    }

    @Schedule(hour = "0", minute = "0", dayOfMonth = "1", persistent = false)
    public void resetMonthlyTokens() {
        List<Token> allTokens = tokenRepository.getAllTokens();
        for (Token token : allTokens) {
            token.setDeletionTokens(1);
            tokenRepository.save(token);
        }
    }

    // Add a new method to reset evry second

    @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    public void resetSecondTokens() {
        List<Token> allTokens = tokenRepository.getAllTokens();
        for (Token token : allTokens) {
            token.setDeletionTokens(10);
            token.setLastResetDate(LocalDateTime.now());
            tokenRepository.save(token);
        }
    }
}