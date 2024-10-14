<%@ page import="com.example.devsyncss.entities.User" %>
<style>
    .navbar {
        z-index: 100;
        background-color: #343a40;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        position: sticky;
        top: 0;
    }

    .navbar-nav {
        flex-direction: row;
        justify-content: center;
        width: 100%;
    }

    .navbar-brand {
        margin-right: auto;
        color: #fff;
    }

    .profile-link {
        margin-left: auto;
        color: #fff;
    }

    .nav-link {
        color: #fff;
    }

    .nav-link:hover {
        color: #ffc107;
    }

    .btn-logout {
        color: #fff;
        background-color: #dc3545;
        border: none;
    }

    .btn-logout:hover {
        background-color: #c82333;
    }
</style>

<%
    User user = (User) request.getSession().getAttribute("user");
    String path = request.getContextPath();
%>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/">DevSync</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item <% if (path.equals("/taskBoard")) { %> active <% } %>">
                    <a class="nav-link" href="<%= request.getContextPath() %>/taskBoard">Task Board</a>
                </li>
                <li class="nav-item
                <% if (path.equals("/tasks")) { %> active <% } %>">
                    <a class="nav-link" href="<%= request.getContextPath() %>/tasks">Tasks</a>
                </li>
                <% if (user.getRole().name().equals("MANAGER")) { %>
                <li class="nav-item <% if (path.equals("/users")) { %> active <% } %>">
                    <a class="nav-link" href="<%= request.getContextPath() %>/users">Users</a>
                </li>
                <li class="nav-item <% if (path.equals("/manager-change-requests")) { %> active <% } %>">
                    <a class="nav-link" href="<%= request.getContextPath() %>/manager-change-requests">Change Requests</a>
                </li>
                <% } %>
            </ul>
            <a class="nav-link profile-link" href="profile">Profile</a>
            <form action="logout" method="post">
                <button type="submit" class="btn btn-logout">Logout</button>
            </form>
        </div>
    </div>
</nav>