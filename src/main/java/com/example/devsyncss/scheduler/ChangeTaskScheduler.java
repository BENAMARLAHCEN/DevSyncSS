package com.example.devsyncss.scheduler;

import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.service.TaskChangeRequestService;
import com.example.devsyncss.service.TokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChangeTaskScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TaskChangeRequestService taskChangeRequestService = new TaskChangeRequestService();
    private final TokenService tokenService = new TokenService();
    public void startChangeTaskScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateChangeRequests, 0, 1, TimeUnit.MINUTES);
    }

    private void checkAndUpdateChangeRequests() {
        List<TaskChange> taskChanges = taskChangeRequestService.getAllTaskChanges();
        for (TaskChange taskChange : taskChanges) {
            if (taskChange.getChangeDate().plusHours(12).isBefore(LocalDateTime.now())){
                User user = taskChange.getUser();
                Token token = tokenService.getToken(user);
                token.setResetModificationTokens(token.getResetModificationTokens() + 2);
                tokenService.updateToken(token);
                taskChangeRequestService.deleteTaskChange(taskChange.getId());
            }
            if (taskChange.getTask().getStatus().name().equalsIgnoreCase("CANCELLED")){
                taskChangeRequestService.deleteTaskChange(taskChange.getId());
            }
        }
    }

}
