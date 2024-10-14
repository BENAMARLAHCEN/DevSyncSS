package com.example.devsyncss.servlet.Authentication;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;
import com.example.devsyncss.scheduler.ChangeTaskScheduler;
import com.example.devsyncss.scheduler.TaskScheduler;
import com.example.devsyncss.scheduler.TokenScheduler;
import com.example.devsyncss.service.UserService;
import com.example.devsyncss.service.interfc.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private IUserService userService;
    private TokenScheduler tokenScheduler;
    private TaskScheduler taskScheduler;
    private ChangeTaskScheduler changeTaskScheduler;

    public void init() {
        userService = new UserService();
        if (userService.getAllUsers().isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setEmail("admin@admin.com");
            user.setRole(Role.MANAGER);
            userService.registerUser(user);
            }
        tokenScheduler = new TokenScheduler();
        tokenScheduler.startTokenScheduler();
        taskScheduler = new TaskScheduler();
        taskScheduler.startTaskScheduler();
        changeTaskScheduler = new ChangeTaskScheduler();
        changeTaskScheduler.startChangeTaskScheduler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect("users");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userService.getUserByEmail(email);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            req.getSession().setAttribute("error", "Invalid email or password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        req.getSession().setAttribute("success", "Login successful");
        session.setAttribute("user", user);
        resp.sendRedirect("profile");
    }
}