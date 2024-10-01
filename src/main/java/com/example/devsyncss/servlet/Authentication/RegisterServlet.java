package com.example.devsyncss.servlet.Authentication;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;
import com.example.devsyncss.service.UserService;
import com.example.devsyncss.service.interfc.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private IUserService userService;

    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }

        if (role == Role.USER && managerId == null) {
            req.setAttribute("error", "Manager ID is required for user role");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }

        if (userService.getUserByEmail(email) != null) {
            req.setAttribute("error", "User with this email already exists");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
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
                req.setAttribute("error", "Invalid manager ID or manager not found");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
        }

        if (role == Role.MANAGER && managerId != null) {
            req.setAttribute("error", "Manager cannot have a manager");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }

        if (userService.registerUser(user)) {
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("success", "User registered successfully");
            resp.sendRedirect("login");
            return;
        }
        req.setAttribute("error", "Failed to register user");
        resp.sendRedirect("register");
    }

}