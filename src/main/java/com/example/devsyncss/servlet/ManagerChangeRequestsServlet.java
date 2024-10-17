package com.example.devsyncss.servlet;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.TaskStatus;
import com.example.devsyncss.service.TaskChangeRequestService;
import com.example.devsyncss.service.TaskService;
import com.example.devsyncss.service.TokenService;
import com.example.devsyncss.service.UserService;
import com.example.devsyncss.service.interfc.ITaskChangeRequestService;
import com.example.devsyncss.service.interfc.ITaskService;
import com.example.devsyncss.service.interfc.ITokenService;
import com.example.devsyncss.service.interfc.IUserService;
import jakarta.persistence.criteria.Fetch;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet({"/manager-change-requests", "/my-change-requests", "/send-change-request", "/approve-request", "/send-delete-request", "/delete-request"})
public class ManagerChangeRequestsServlet extends HttpServlet {
    private ITaskService taskService;
    private IUserService userService;
    private ITaskChangeRequestService taskChangeRequestService;
    private ITokenService tokenService;

    public void init() {
        taskService = new TaskService();
        userService = new UserService();
        tokenService = new TokenService();
        taskChangeRequestService = new TaskChangeRequestService();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String path = req.getRequestURI();
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        if (path.contains("/manager-change-requests")) {
            if (!user.getRole().name().equalsIgnoreCase("MANAGER")) {
                resp.sendRedirect("taskBoard");
                return;
            }
            List<User> users = userService.getAllUsersByManagerId(user.getId());
            req.setAttribute("users", users);
            req.setAttribute("changeRequests", taskChangeRequestService.getAllManagerCreateTaskChanges(user.getId()));
            req.getRequestDispatcher("/manager-change-requests.jsp").forward(req, resp);
            return;
        }
        if (path.contains("/my-change-requests")) {
            if (!user.getRole().name().equalsIgnoreCase("MANAGER")) {
                resp.sendRedirect("taskBoard");
                return;
            }
            req.setAttribute("changeRequests", taskChangeRequestService.getUserChangeRequests(user));
            req.getRequestDispatcher("/my-change-requests.jsp").forward(req, resp);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathURI = request.getRequestURI();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        if (pathURI.contains("/send-change-request")) {
            if (user.getRole().name().equalsIgnoreCase("MANAGER")) {
                response.sendRedirect("manager-change-requests");
                return;
            }
            Long taskId = Long.parseLong(request.getParameter("taskId"));
            String changeDescription = request.getParameter("changeDescription");
            Task task = taskService.getTaskById(taskId);
            User taskUser = task.getAssignedTo();
            if (taskUser != null && !taskUser.getId().equals(user.getId())) {
                request.getSession().setAttribute("error", "You can't change request for this task");
                response.sendRedirect("tasks");
                return;
            }

            if (task.getStatus().equals(TaskStatus.COMPLETED)) {
                request.getSession().setAttribute("error", "Task is already completed");
                response.sendRedirect("tasks");
                return;
            }

            if (task.getStatus().equals(TaskStatus.CANCELLED)) {
                request.getSession().setAttribute("error", "Task is already cancelled");
                response.sendRedirect("tasks");
                return;
            }

            if (task.getCreatedBy().getId().equals(user.getId())) {
                request.getSession().setAttribute("error", "You can't change request for this task");
                response.sendRedirect("tasks");
                return;
            }

            if (task.isLocked()) {
                request.getSession().setAttribute("error", "Task is locked");
                response.sendRedirect("tasks");
                return;
            }

            if (taskChangeRequestService.isTaskChangeRequestExists(task)) {
                request.getSession().setAttribute("error", "Change request already exists for this task");
                response.sendRedirect("tasks");
                return;
            }

            if (task != null) {
                Token token = tokenService.getTokenByUserId(user.getId());
                if (token == null) {
                    request.getSession().setAttribute("error", "Token not found");
                    response.sendRedirect("tasks");
                    return;
                }
                if (token.getModificationTokens()<1) {
                    request.getSession().setAttribute("error", "You have no modification tokens");
                    response.sendRedirect("tasks");
                    return;
                }
                task.setLocked(true);
                taskService.updateTask(task);
                taskChangeRequestService.createTaskChangeRequest(task, user, changeDescription);
                token.setModificationTokens(token.getModificationTokens()-1);
                tokenService.updateToken(token);
                request.getSession().setAttribute("success", "Change request sent successfully");
                response.sendRedirect("tasks");
            } else {
                request.getSession().setAttribute("error", "Task not found");
                response.sendRedirect("tasks");
            }
            return;
        }

        if (pathURI.contains("/send-delete-request")) {
            if (user.getRole().name().equalsIgnoreCase("MANAGER")) {
                response.sendRedirect("manager-change-requests");
                return;
            }
            Long taskId = Long.parseLong(request.getParameter("taskId"));
            Task task = taskService.getTaskById(taskId);
            User taskUser = task.getAssignedTo();
            if (taskUser != null && !taskUser.getId().equals(user.getId())) {
                request.getSession().setAttribute("error", "You can't delete request for this task");
                response.sendRedirect("tasks");
                return;
            }
            if (task.getStatus().name().equalsIgnoreCase(TaskStatus.COMPLETED.name())) {
                request.getSession().setAttribute("error", "Task is already completed");
                response.sendRedirect("tasks");
                return;
            }

            if (task.getStatus().name().equalsIgnoreCase(TaskStatus.CANCELLED.name())) {
                request.getSession().setAttribute("error", "Task is already cancelled");
                response.sendRedirect("tasks");
                return;
            }

            if (task.getCreatedBy().getId().equals(user.getId())) {
                request.getSession().setAttribute("error", "You can't delete request for this task");
                response.sendRedirect("tasks");
                return;
            }

            if (task.isLocked()) {
                request.getSession().setAttribute("error", "Task is locked");
                response.sendRedirect("tasks");
                return;
            }

            if (taskChangeRequestService.isTaskChangeRequestExists(task)) {
                request.getSession().setAttribute("error", "Change request already exists for this task");
                response.sendRedirect("tasks");
                return;
            }

            if (task != null) {
                Token token = tokenService.getTokenByUserId(user.getId());
                if (token == null) {
                    request.getSession().setAttribute("error", "Token not found");
                    response.sendRedirect("tasks");
                    return;
                }
                if (token.getDeletionTokens()<1) {
                    request.getSession().setAttribute("error", "You have no deletion tokens");
                    response.sendRedirect("tasks");
                    return;
                }
                task.setLocked(true);
                taskService.updateTask(task);
                taskChangeRequestService.createTaskDeleteRequest(task, user);
                token.setDeletionTokens(token.getDeletionTokens()-1);
                tokenService.updateToken(token);
                request.getSession().setAttribute("success", "Delete request sent successfully");
                response.sendRedirect("tasks");
            } else {
                request.getSession().setAttribute("error", "Task not found");
                response.sendRedirect("tasks");
            }
            return;
        }

        if (pathURI.contains("/approve-request")) {
            if (user.getRole().name().equalsIgnoreCase("USER")) {
                response.sendRedirect("taskBoard");
                return;
            }
            Long ChangeRequestId = Long.parseLong(request.getParameter("changeId"));
            Long newAssigneeId = Long.parseLong(request.getParameter("newAssigneeId"));
            TaskChange taskChange = taskChangeRequestService.getTaskChangeById(ChangeRequestId);
            if (taskChange != null) {
                Task task = taskChange.getTask();
                if (task.getStatus().name().equalsIgnoreCase(TaskStatus.COMPLETED.name())) {
                    request.getSession().setAttribute("error", "Task is already completed");
                    response.sendRedirect("manager-change-requests");
                    return;
                }
                if (task.getStatus().name().equalsIgnoreCase(TaskStatus.CANCELLED.name())) {
                    request.getSession().setAttribute("error", "Task is already cancelled");
                    response.sendRedirect("manager-change-requests");
                    return;
                }

                if (taskChange.getUser().getId() == newAssigneeId) {
                    request.getSession().setAttribute("error", "Task is already assigned to this user");
                    response.sendRedirect("manager-change-requests");
                    return;
                }
                if (task.getCreatedBy().getId().equals(user.getId())) {
                User newAssignee = userService.getUserById(newAssigneeId);
                task.setAssignedTo(newAssignee);
                taskService.updateTask(task);
                taskChangeRequestService.deleteTaskChange(taskChange.getId());
                request.getSession().setAttribute("success", "Request approved successfully");
                response.sendRedirect("manager-change-requests");
                } else {
                    request.getSession().setAttribute("error", "You are not allowed to approve this request");
                    response.sendRedirect("manager-change-requests");
                }
            } else {
                request.getSession().setAttribute("error", "Request not found");
                response.sendRedirect("manager-change-requests");
            }
        }

        if (pathURI.contains("/delete-request")) {
            if (user.getRole().name().equalsIgnoreCase("USER")) {
                response.sendRedirect("taskBoard");
                return;
            }
            Long ChangeRequestId = Long.parseLong(request.getParameter("changeId"));
            TaskChange taskChange = taskChangeRequestService.getTaskChangeById(ChangeRequestId);
            if (taskChange != null) {
                Task task = taskChange.getTask();
                if (task.getCreatedBy().getId().equals(user.getId())) {
                    taskChangeRequestService.deleteTaskChange(taskChange.getId());
                    taskService.deleteTask(task.getId());
                    request.getSession().setAttribute("success", "Request deleted successfully");
                    response.sendRedirect("manager-change-requests");
                } else {
                    request.getSession().setAttribute("error", "You are not allowed to delete this request");
                    response.sendRedirect("manager-change-requests");
                }
            } else {
                request.getSession().setAttribute("error", "Request not found");
                response.sendRedirect("manager-change-requests");
            }
        }
    }



}
