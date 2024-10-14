package com.example.devsyncss.servlet;

import com.example.devsyncss.entities.User;
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
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private IUserService userService;

    public void init() {
        userService = new UserService();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            req.setAttribute("user", user);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect("login");
    }

    protected  void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null || email.isEmpty()) {
                req.getSession().setAttribute("error", "All fields are required");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            if (password != null && !password.isEmpty() && !password.equals(confirmPassword)) {
                req.getSession().setAttribute("error", "Passwords do not match");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            boolean isUpdate = userService.updateUser(user);
            if (!isUpdate) {
                req.getSession().setAttribute("error", "An error occurred. Please try again");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("success", "Profile updated successfully");
            session.setAttribute("user", user);
            resp.sendRedirect("profile");
            return;
        }
        resp.sendRedirect("login");
    }
}
