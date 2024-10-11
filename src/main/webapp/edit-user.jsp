<%@ page import="com.example.devsyncss.entities.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit User</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/profile.css">
    <style>
        body {
            background-color: #f8f9fa;
            color: #343a40;
        }

        .form-group label {
            font-weight: bold;
            color: #343a40;
        }

        .btn-primary {
            background-color: #343a40;
            border: none;
        }

        .btn-primary:hover {
            background-color: #23272b;
        }

        .btn-danger {
            background-color: #dc3545;
            border: none;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <% User u = (User) request.getAttribute("user"); %>
<%@ include file="shared/_header.jsp" %>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 offset-md-3">
            <h2 class="text-center">Edit User</h2>
            <form action="../users/<%= u.getId() %>" method="post">
                <div class="form-group mt-3">
                    <label for="username">Username</label>
                    <input type="text" name="username" id="username" class="form-control" value="<%= u.getUsername() %>">
                </div>
                <div class="form-group mt-3">
                    <label for="firstName">First Name</label>
                    <input type="text" name="firstName" id="firstName" class="form-control" value="<%= u.getFirstName() %>">
                </div>
                <div class="form-group mt-3">
                    <label for="lastName">Last Name</label>
                    <input type="text" name="lastName" id="lastName" class="form-control" value="<%= u.getLastName() %>">
                </div>
                <div class="form-group mt-3">
                    <label for="email">Email</label>
                    <input type="email" name="email" id="email" class="form-control" value="<%= u.getEmail() %>">
                </div>
                <div class="form-group mt-3">
                    <label for="role">Role</label>
                    <select name="role" id="role" class="form-control">
                        <option value="USER" <%= u.getRole().equals("USER") ? "selected" : "" %>>User</option>
                        <option value="MANAGER" <%= u.getRole().equals("MANAGER") ? "selected" : "" %>>Manager</option>
                    </select>
                </div>
                <div class="form-group mt-3">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <label for="manager">Manager</label>
                    <select name="managerID" id="manager" class="form-control">
                        <% List<User> users = (List<User>) request.getAttribute("users"); %>
                        <% for (User u : users) { %>
                            <option value="<%= u.getId() %>" <%= u.getManager() != null && u.getManager().getId() == u.getId() ? "selected" : "" %>><%= u.getFirstName() %> <%= u.getLastName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group mt-3">
                    <button type="submit" class="btn btn-primary">Update</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>