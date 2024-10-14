<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.devsyncss.entities.Task"%>
<%@ page import="com.example.devsyncss.entities.Tag"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.devsyncss.entities.TaskChange" %>
<%@ page import="com.example.devsyncss.entities.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevSync Change Requests</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            color: #343a40;
        }

        .table th, .table td {
            vertical-align: middle;
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

        .modal-header {
            background-color: #343a40;
            color: #fff;
        }

        .modal-footer .btn-primary {
            background-color: #343a40;
            border: none;
        }

        .modal-footer .btn-primary:hover {
            background-color: #23272b;
        }
    </style>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<%@ include file="shared/_header.jsp" %>
<div class="container-fluid mt-4">
    <h1 class="text-center mb-4">DevSync Change List</h1>
    <%@ include file="shared/_alert.jsp" %>
    <div class="row">
        <div class="col-md-12 mb-4">
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Task Title</th>
                        <th>Change Description</th>
                        <th>Date Requested</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<TaskChange> taskChanges = (List<TaskChange>) request.getAttribute("changeRequests");
                        for (TaskChange taskChange : taskChanges) {
                    %>
                    <tr>
                        <td><%= taskChange.getTask().getTitle() %></td>
                        <td><%= taskChange.getChangeDescription() %></td>
                        <td><%= taskChange.getChangeDate() %></td>
                        <td>
                            <% if (taskChange.getChangeType().name().equalsIgnoreCase("ASSIGNMENT_CHANGE")) { %>
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#approveModal" onclick="addMessageForExceptUserInModal('<%= taskChange.getUser().getFirstName() + taskChange.getUser().getLastName() %>',<%= taskChange.getId() %>)">
                                Approve
                            </button>
                            <% } else if (taskChange.getChangeType().name().equalsIgnoreCase("DELETION")) { %>
                            <form method="post" action="<%= request.getContextPath() %>/delete-request">
                                <input type="hidden" name="changeId" value="<%= taskChange.getId() %>">
                                <button type="submit" class="btn btn-danger">Delete</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <script>
                        function addMessageForExceptUserInModal(name, id) {
                            document.getElementById("ExceptUser").innerHTML = "Except User: " + name;
                            document.getElementById("changeId").value = id;
                        }
                    </script>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Approve Modal -->
<div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="approveModalLabel">Approve Change Request</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/approve-request">
            <div class="modal-body text-center">
                <p>Assign the task to another user <p id="ExceptUser"></p></p>

                <select class="form-select" aria-label="Default select example" name="newAssigneeId">
                    <option selected>Select User</option>
                    <% List<User> Users = (List<User>) request.getAttribute("users");
                        for (User u : Users) {
                    %>
                    <option value="<%= u.getId() %>"><%= u.getFirstName() %> <%= u.getLastName() %></option>
                    <% } %>
                </select>
                <input type="hidden" name="changeId" id="changeId">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-success">Assign</button>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>