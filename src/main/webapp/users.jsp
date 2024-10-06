<%@ page import="com.example.devsyncss.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/profile.css">
</head>
<body>
    <% List<User> users = (List<User>) request.getAttribute("users"); %>
<%@ include file="shared/_header.jsp" %>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 offset-md-3">
            <h2 class="text-center">Add User</h2>
            <form action="add-user" method="post">
                <div class="form-group mt-3">
                    <label for="username">Username</label>
                    <input type="text" name="username" id="username" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <label for="firstName">First Name</label>
                    <input type="text" name="firstName" id="firstName" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <label for="lastName">Last Name</label>
                    <input type="text" name="lastName" id="lastName" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <label for="email">Email</label>
                    <input type="email" name="email" id="email" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <label for="role">Role</label>
                    <select name="role" id="role" class="form-control">
                        <option value="USER">User</option>
                        <option value="MANAGER">MANAGER</option>
                    </select>
                </div>
                <div class="form-group mt-3">
                    <label for="Manager">Manager</label>
                    <select name="managerID" id="manager" class="form-control">
                        <% for (User u : users.stream().filter((u)->u.getRole().equals("MANAGER")).collect(Collectors.toList())) { %>
                            <option value="<%= u.getId() %>"><%= u.getFirstName() %> <%= u.getLastName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group mt-3">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" class="form-control">
                </div>
                <div class="form-group mt-3">
                    <button type="submit" class="btn btn-primary">Add</button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h2 class="text-center">Users</h2>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (User user : users) { %>
                        <tr>
                            <td><%= user.getId() %></td>
                            <td><%= user.getUsername() %></td>
                            <td><%= user.getEmail() %></td>
                            <td><%= user.getRole() %></td>
                            <td>
                                <a href="users/<%= user.getId() %>" class="btn btn-primary">Edit</a>
                                <form action="delete-user/<%= user.getId() %>" method="post" style="display: inline;">
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
