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
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<%@ include file="shared/_header.jsp" %>
<div class="container-fluid mt-4">
    <h1 class="text-center mb-4">DevSync Change List</h1>
    <div class="row">
        <div class="col-md-12 mb-4">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Task Title</th>
                        <th>Change Description</th>
                        <td>Date Requested</td>
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
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#approveModal" onclick="addMessageForExceptUserInModal('<%= taskChange.getUser().getFirstName() + taskChange.getUser().getLastName() %>')">
                                Approve
                            </button>

                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <script>
                        function addMessageForExceptUserInModal(name) {
                            document.getElementById("ExceptUser").innerHTML = "Except User: " + name;
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
            <form method="post">
            <div class="modal-body text-center">
                <p>Assign the task to another user <p id="ExceptUser"></p></p>

                <select class="form-select" aria-label="Default select example">
                    <option selected>Select User</option>
                    <% List<User> Users = (List<User>) request.getAttribute("users");
                        for (User user : Users) {
                    %>
                    <option value="<%= user.getId() %>"><%= user.getFirstName() %> <%= user.getLastName() %></option>
                    <% } %>
                </select>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-success">Assign</button>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>