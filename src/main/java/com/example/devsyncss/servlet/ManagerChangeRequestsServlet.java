package com.example.devsyncss.servlet;

import com.example.devsyncss.service.TaskChangeRequestService;
import com.example.devsyncss.service.TaskService;
import com.example.devsyncss.service.interfc.ITaskChangeRequestService;
import com.example.devsyncss.service.interfc.ITaskService;
import jakarta.servlet.http.HttpServlet;

public class ManagerChangeRequestsServlet extends HttpServlet {
    private ITaskService taskService;
    private ITaskChangeRequestService taskChangeRequestService;

    public void init() {
        taskService = new TaskService();
        taskChangeRequestService = new TaskChangeRequestService();
    }
}
