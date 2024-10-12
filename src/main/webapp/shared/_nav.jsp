<%@ page import="com.example.devsyncss.entities.User" %>
<header class="header">
    <nav class="navbar navbar-expand-lg navbar-light py-3">
        <div class="container">
            <a href="<%= request.getContextPath() %>" class="navbar-brand">
                <img src="assets/img/logo.png" alt="logo" width="150">
            </a>
            <% User user = (User) session.getAttribute("user"); %>
            <% if (user != null) { %>
                <div class="ml-auto">
                    <a href="taskBoard" class="btn btn-primary mr-2">Task Board</a>
                    <a href="logout" class="btn btn-secondary">Logout</a>
                </div>
            <% } else { %>
                <div class="ml-auto">
                    <a href="login" class="btn btn-primary mr-2">Login</a>
                    <a href="register" class="btn btn-secondary">Register</a>
                </div>
            <% } %>

        </div>
    </nav>
</header>