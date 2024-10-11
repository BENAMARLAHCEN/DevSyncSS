package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.User;

import java.util.List;

public interface ITaskChangeRequestService {
    List<TaskChange> getAllManagerCreateTaskChanges(Long managerId);

    List<TaskChange> getUserChangeRequests(User user);

    void createTaskChangeRequest(Task task, User user, String changeDescription);

    TaskChange getTaskChangeById(Long changeRequestId);

    void deleteTaskChange(Long id);
}
