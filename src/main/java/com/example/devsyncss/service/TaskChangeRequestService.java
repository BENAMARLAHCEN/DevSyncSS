package com.example.devsyncss.service;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.ChangeType;
import com.example.devsyncss.repository.TaskChangeRepository;
import com.example.devsyncss.repository.interfc.ITaskChangeRepository;
import com.example.devsyncss.service.interfc.ITaskChangeRequestService;

import java.time.LocalDateTime;
import java.util.List;

public class TaskChangeRequestService implements ITaskChangeRequestService {
    private ITaskChangeRepository taskChangeRepository;

    public TaskChangeRequestService() {
        this.taskChangeRepository = new TaskChangeRepository();
    }

    public List<TaskChange> getAllManagerCreateTaskChanges(Long managerId) {
        return taskChangeRepository.getAllManagerCreateTaskChanges(managerId);
    }

    public List<TaskChange> getUserChangeRequests(User user) {
        return taskChangeRepository.getUserChangeRequests(user);
    }

    @Override
    public void createTaskChangeRequest(Task task, User user, String changeDescription) {
        TaskChange taskChange = new TaskChange();
        taskChange.setTask(task);
        taskChange.setUser(user);
        taskChange.setChangeDescription(changeDescription);
        taskChange.setChangeDate(LocalDateTime.now());
        taskChange.setChangeType(ChangeType.ASSIGNMENT_CHANGE);
        taskChangeRepository.createTaskChange(taskChange);
    }

    public TaskChange getTaskChangeById(Long changeRequestId) {
        return taskChangeRepository.getTaskChange(changeRequestId);
    }

    public void deleteTaskChange(Long id) {
        taskChangeRepository.deleteTaskChange(id);
    }


}
