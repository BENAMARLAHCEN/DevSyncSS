package com.example.devsyncss.servlet;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.service.TaskService;
import com.example.devsyncss.service.interfc.ITaskService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet({"/taskBoard", "/update-status"})
public class TaskBoardServlet extends HttpServlet {
    private ITaskService taskService;

    public void init() {
        taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        List<Task> tasks = taskService.getUserCreatedTasks(user);
        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("/tasks-board.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long taskId = Long.parseLong(request.getParameter("taskId"));
        String newStatus = request.getParameter("newStatus");
        User user = (User) request.getSession().getAttribute("user");
        Task task = taskService.getTaskById(taskId);
        if (newStatus.equalsIgnoreCase(TaskStatus.CANCELLED.name()) && task.getDueDate().isAfter(LocalDateTime.now())){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"Cannot cancel task that is not overdue\"}");
            return;
        }
        if (task != null && task.getCreatedBy().getId().equals(user.getId())) {
            task.setStatus(TaskStatus.valueOf(newStatus.toUpperCase()));
            taskService.updateTask(task);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"success\": false, \"error\": \"Task not found\"}");
        }
    }
}