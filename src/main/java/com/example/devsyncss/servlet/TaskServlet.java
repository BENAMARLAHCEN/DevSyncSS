package com.example.devsyncss.servlet;


import com.example.devsyncss.entities.Tag;
import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.service.TagService;
import com.example.devsyncss.service.TaskService;
import com.example.devsyncss.service.UserService;
import com.example.devsyncss.service.interfc.ITagService;
import com.example.devsyncss.service.interfc.ITaskService;
import com.example.devsyncss.service.interfc.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "TaskServlet", value = {"/tasks", "/tasks/*", "/delete-task/*"})
public class TaskServlet extends HttpServlet {
    private ITaskService taskService;
    private IUserService userService;
    private ITagService tagService;

    public void init() {
        taskService = new TaskService();
        userService = new UserService();
        tagService = new TagService();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        if (req.getRequestURI().contains("/tasks/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long taskId = Long.parseLong(uriParts[uriParts.length - 1]);
            Task task = taskService.getTaskById(taskId);
            if (task == null || (user != null && !task.getCreatedBy().getId().equals(user.getId()))) {
                resp.sendRedirect("tasks");
                return;
            }
            List<User> users = userService.getAllUsers().stream()
                    .filter(u -> u.getRole().name().equalsIgnoreCase("USER"))
                    .collect(Collectors.toList());
            List<Tag> tags = tagService.getAllTags();
            req.setAttribute("task", task);
            req.setAttribute("users", users);
            req.setAttribute("tags", tags);
            req.getRequestDispatcher("/edit-task.jsp").forward(req, resp);
            return;
        }
        if (req.getRequestURI().contains("/delete-task/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long taskId = Long.parseLong(uriParts[uriParts.length - 1]);
            Task task = taskService.getTaskById(taskId);
            if (task == null || (user != null && !task.getCreatedBy().getId().equals(user.getId()))) {
                resp.sendRedirect("tasks");
                return;
            }
            taskService.deleteTask(taskId);
            resp.sendRedirect("tasks");
            return;
        }
        if (req.getRequestURI().contains("/tasks")) {
            List<Task> tasks;
            List<User> users = List.of();
            if (user.getRole().name().equalsIgnoreCase("MANAGER")) {
                tasks = taskService.getUserCreatedTasks(user);
                users = userService.getAllUsersByManagerId(user.getId());

            } else {
                tasks = taskService.getAllTasks().stream()
                        .filter(task -> task.getAssignedTo().getId().equals(user.getId()))
                        .collect(Collectors.toList());
            }
            List<Tag> tags = tagService.getAllTags();
            req.setAttribute("tasks", tasks);
            req.setAttribute("users", users);
            req.setAttribute("tags", tags);
            req.getRequestDispatcher("/tasks.jsp").forward(req, resp);
            return;
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        if (req.getRequestURI().contains("/tasks/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long taskId = Long.parseLong(uriParts[uriParts.length - 1]);
            Task task = taskService.getTaskById(taskId);
            if (task == null || (user != null && !task.getCreatedBy().getId().equals(user.getId()))) {
                resp.sendRedirect("../tasks/" + taskId);
                return;
            }
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String dueDate = req.getParameter("dueDate");
            TaskStatus status = TaskStatus.valueOf(req.getParameter("status"));
            List<Long> tagIds = Stream.of(req.getParameterValues("tags[]"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(LocalDateTime.parse(dueDate));
            Set<Tag> tags = tagIds.stream()
                    .map(tagId -> tagService.getTagById(tagId)).collect(Collectors.toSet());
            task.setTags(tags);
            task.setStatus(status);
            taskService.updateTask(task);
            resp.sendRedirect("../tasks");
            return;
        }
        if (req.getRequestURI().contains("/tasks")) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            TaskStatus status = TaskStatus.valueOf(req.getParameter("status"));
            String dueDate = req.getParameter("dueDate");
            String assignedToStr = req.getParameter("assignedTo");

            if (assignedToStr == null || assignedToStr.isEmpty()) {
                req.getSession().setAttribute("error", "Assigned to field is required");
                resp.sendRedirect("tasks");
                return;
            }
            Long assignedToId = Long.parseLong(assignedToStr);
            List<Long> tagIds = Stream.of(req.getParameterValues("tags[]"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setCreatedBy(user);
            task.setCreationDate(LocalDateTime.now());
            task.setStatus(status);
            task.setDueDate(LocalDateTime.parse(dueDate));
            task.setAssignedTo(userService.getUserById(assignedToId));
            Set<Tag> tags = tagIds.stream()
                    .map(tagId -> tagService.getTagById(tagId)).collect(Collectors.toSet());
            task.setTags(tags);
            taskService.createTask(task);
            resp.sendRedirect("tasks");
            return;
        }

    }
}
