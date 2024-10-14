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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
                req.getSession().setAttribute("error", "Task not found or you are not authorized to view this task");
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
                req.getSession().setAttribute("error", "Task not found or you are not authorized to delete this task");
                resp.sendRedirect("../tasks");
                return;
            }
            taskService.deleteTask(taskId);
            req.getSession().setAttribute("success", "Task deleted successfully");
            resp.sendRedirect("../tasks");
            return;
        }
        String[] tagIds = req.getParameterValues("tags[]");
        String period = req.getParameter("dateRange");
        if (req.getRequestURI().contains("/tasks")) {
            List<Task> tasks = List.of();
            List<User> users = List.of();
            if (user.getRole().name().equalsIgnoreCase("MANAGER")) {
                tasks = taskService.getUserCreatedTasks(user);
                users = userService.getAllUsersByManagerId(user.getId());

            } else {
                tasks = taskService.getAllTasks().stream()
                            .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId()))
                            .collect(Collectors.toList());
            }
            if (tagIds != null && tagIds.length > 0) {
                List<Long> tagIdsList = Arrays.stream(tagIds)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                tasks = tasks.stream()
                        .filter(task -> task.getTags().stream()
                                .anyMatch(tag -> tagIdsList.contains(tag.getId())))
                        .collect(Collectors.toList());
            }

            if (period != null && !period.isEmpty()) {
                String[] dates = period.split(" - ");
                LocalDate startDate = LocalDate.parse(dates[0]);
                LocalDate endDate = LocalDate.parse(dates[1]);
                tasks = tasks.stream()
                        .filter(task -> !task.getDueDate().toLocalDate().isBefore(startDate) && !task.getDueDate().toLocalDate().isAfter(endDate))
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

            if (title == null || title.isEmpty() || description == null || description.isEmpty() || dueDate == null || dueDate.isEmpty()) {
                req.getSession().setAttribute("error", "All fields are required");
                resp.sendRedirect("../tasks/" + taskId);
                return;
            }

            if (LocalDateTime.parse(dueDate).isBefore(LocalDateTime.now())) {
                req.getSession().setAttribute("error", "Due date should be in the future");
                resp.sendRedirect("../tasks/" + taskId);
                return;
            }

            if (LocalDateTime.parse(dueDate).isBefore(LocalDateTime.now().plusDays(3))) {
                req.getSession().setAttribute("error", "Due date should be at least 3 days from now");
                resp.sendRedirect("../tasks/" + taskId);
                return;
            }

            if (tagIds.isEmpty() || tagIds.size() < 2 ) {
                req.getSession().setAttribute("error", "At least 2 tags are required");
                resp.sendRedirect("../tasks/" + taskId);
                return;
            }

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

            LocalDateTime dueDateTime = LocalDateTime.parse(dueDate);
            if (title == null || title.isEmpty()) {
                req.getSession().setAttribute("error", "Title field is required");
                resp.sendRedirect("tasks");
                return;
            }

            if (description == null || description.isEmpty()) {
                req.getSession().setAttribute("error", "Description field is required");
                resp.sendRedirect("tasks");
                return;
            }

            if (dueDateTime == null || dueDateTime.isBefore(LocalDateTime.now()) || LocalDateTime.now().plusDays(3).isAfter(dueDateTime)) {
                req.getSession().setAttribute("error", "Due date should be at least 3 days from now");
                resp.sendRedirect("tasks");
                return;
            }

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
            task.setDueDate(dueDateTime);
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
