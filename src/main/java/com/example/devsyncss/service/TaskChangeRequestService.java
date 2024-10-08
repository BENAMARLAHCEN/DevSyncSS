package com.example.devsyncss.service;

import com.example.devsyncss.repository.TaskChangeRepository;
import com.example.devsyncss.repository.interfc.ITaskChangeRepository;
import com.example.devsyncss.service.interfc.ITaskChangeRequestService;

public class TaskChangeRequestService implements ITaskChangeRequestService {
    private ITaskChangeRepository taskChangeRepository;

    public TaskChangeRequestService() {
        this.taskChangeRepository = new TaskChangeRepository();
    }



}
