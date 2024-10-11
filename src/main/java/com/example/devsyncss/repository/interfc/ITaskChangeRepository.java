package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.User;

import java.util.List;

public interface ITaskChangeRepository {
    void createTaskChange(TaskChange taskChange);
    TaskChange getTaskChange(Long id);
    void updateTaskChange(TaskChange taskChange);
    void deleteTaskChange(Long id);
    List<TaskChange> getAllManagerCreateTaskChanges(Long managerId);

    List<TaskChange> getUserChangeRequests(User user);
}
