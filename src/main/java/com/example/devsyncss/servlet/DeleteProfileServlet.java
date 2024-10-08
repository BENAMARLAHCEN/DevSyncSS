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

@WebServlet("/profile/delete")
public class DeleteProfileServlet extends HttpServlet {

    private IUserService userService;

    public void init() {
        userService = new UserService();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");  // Check the hidden _method parameter
        if ("DELETE".equalsIgnoreCase(method)) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            String password = req.getParameter("password");
            if (password == null || password.isEmpty()) {
                req.setAttribute("error", "Password is required");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            if (!BCrypt.checkpw(password, user.getPassword())) {
                req.setAttribute("error", "Invalid password");
                req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                return;
            }
            boolean isDeleted = userService.deleteUser(user);
            if (isDeleted) {
                session.invalidate();
                resp.sendRedirect("login");
                return;
            }
            req.setAttribute("er" +
                    "" +
                    "ror", "An error occurred. Please try again");
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect("login");
    }



}
