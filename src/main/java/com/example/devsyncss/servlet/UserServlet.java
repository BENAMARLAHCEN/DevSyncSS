package com.example.devsyncss.servlet;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;
import com.example.devsyncss.service.UserService;
import com.example.devsyncss.service.interfc.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

@WebServlet({"/users", "/users/*", "/delete-user/*", "/add-user"})
public class UserServlet extends HttpServlet {

    private IUserService userService;

    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().contains("/users/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long userId = Long.parseLong(uriParts[uriParts.length - 1]);
            User user = userService.getUserById(userId);
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/edit-user.jsp").forward(req, resp);
            return;
        } else if (req.getRequestURI().contains("/users")) {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/users.jsp").forward(req, resp);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().contains("/users")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            Role role = Role.valueOf(req.getParameter("role"));
            String managerIdStr = req.getParameter("managerId");
            Long managerId = managerIdStr != null && !managerIdStr.isEmpty() ? Long.parseLong(managerIdStr) : null;

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);

            userService.addUser(user, managerId);
            resp.sendRedirect("user");
        } else if (req.getRequestURI().contains("/delete-user/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long userId = Long.parseLong(uriParts[uriParts.length - 1]);
            User user = userService.getUserById(userId);
            if (user == null) {
                req.setAttribute("error", "User not found");
                resp.sendRedirect("users");
                return;
            }
            userService.deleteUser(user);
            resp.sendRedirect("users");
        } else if (req.getRequestURI().contains("/users/")) {
            String[] uriParts = req.getRequestURI().split("/");
            Long userId = Long.parseLong(uriParts[uriParts.length - 1]);
            User user = userService.getUserById(userId);
            if (user == null) {
                req.setAttribute("error", "User not found");
                resp.sendRedirect("users");
                return;
            }
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            Role role = Role.valueOf(req.getParameter("role"));
            String managerIdStr = req.getParameter("managerId");
            Long managerId = managerIdStr != null && !managerIdStr.isEmpty() ? Long.parseLong(managerIdStr) : null;

            if (username == null || username.isEmpty() || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty() || role == null) {
                req.setAttribute("error", "All fields are required");
                resp.sendRedirect("users");
            }
            if (role == Role.USER && managerId == null) {
                req.setAttribute("error", "Manager ID is required for user role");
                resp.sendRedirect("users");
            }

            if (password != null && !password.isEmpty()) {
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            }
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);
            if (managerId != null) {
                try {
                    user.setManager(userService.getUserById(managerId));
                } catch (IllegalArgumentException e) {
                    req.setAttribute("error", "Invalid manager ID or manager not found");
                    resp.sendRedirect("users");
                    return;
                }
            }

            userService.updateUser(user);
            req.setAttribute("success", "User updated successfully");
            resp.sendRedirect("users");
        } else if (req.getRequestURI().contains("/add-user")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            Role role = Role.valueOf(req.getParameter("role"));
            String managerIdStr = req.getParameter("managerId");
            Long managerId = managerIdStr != null && !managerIdStr.isEmpty() ? Long.parseLong(managerIdStr) : null;

            if (username == null || username.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty() || role == null) {
                req.setAttribute("error", "All fields are required");
                resp.sendRedirect("users");
            }

            if (role == Role.USER && managerId == null) {
                req.getSession().setAttribute("error", "Manager ID is required for user role");
                resp.sendRedirect("users");
            }

            if (userService.getUserByEmail(email) != null) {
                req.getSession().setAttribute("error", "User with this email already exists");
                resp.sendRedirect("users");
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);
            if (managerId != null) {
                try {
                    user.setManager(userService.getUserById(managerId));
                } catch (IllegalArgumentException e) {
                    req.getSession().setAttribute("error", "Invalid manager ID or manager not found");
                    resp.sendRedirect("users");
                    return;
                }
            }

            if (role == Role.MANAGER && managerId != null) {
                req.getSession().setAttribute("error", "Manager cannot have a manager");
                resp.sendRedirect("users");
            }

            userService.registerUser(user);
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("success", "User registered successfully");
            resp.sendRedirect("users");
        }
    }
}