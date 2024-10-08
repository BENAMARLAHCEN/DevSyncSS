<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.devsyncss.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.devsyncss.entities.Tag" %>
<%@ page import="com.example.devsyncss.entities.Task" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Task</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/profile.css">
</head>
<body>
<% List<Tag> tags = (List<Tag>) request.getAttribute("tags"); %>
<% Task task = (Task) request.getAttribute("task"); %>
<% User user = (User) session.getAttribute("user"); %>
<%@ include file="shared/_header.jsp" %>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1>Edit Task</h1>
                <form action="<%= request.getContextPath() %>/tasks/<%= task.getId() %>" method="post">
                    <div class="form-group mb-2">
                        <label for="title">Title</label>
                        <input type="text" class="form-control" id="title" name="title" value="<%= task.getTitle() %>" required>
                    </div>
                    <div class="form-group mb-2">
                        <label for="description">Description</label>
                        <textarea class="form-control" id="description" name="description" required><%= task.getDescription() %></textarea>
                    </div>
                    <div class="form-group mb-2">
                        <label for="status">Status</label>
                        <select name="status" id="status" class="form-control" required>
                            <option value="PENDING" <%= task.getStatus().name().equals("PENDING") ? "selected" : "" %>>Pending</option>
                            <option value="IN_PROGRESS" <%= task.getStatus().name().equals("IN_PROGRESS") ? "selected" : "" %>>In Progress</option>
                            <option value="COMPLETED" <%= task.getStatus().name().equals("COMPLETED") ? "selected" : "" %>>Completed</option>
                            <option value="CANCELLED" <%= task.getStatus().name().equals("CANCELLED") ? "selected" : "" %>>Cancelled</option>
                        </select>
                    </div>
                    <div class="form-group
                    mb-2">
                        <label for="dueDate">Due Date</label>
                        <input type="datetime-local" class="form-control" id="dueDate" name="dueDate" value="<%= task.getDueDate() %>" required>
                    </div>
                    <div class="form-group mb-2">
                        <label for="tags">Tags</label>
                        <select name="tags[]" id="tags" class="form-control" multiple>
                            <% for (Tag tag : tags) {
                                boolean isSelected = false;
                                for (Tag t : task.getTags()) {
                                    if (t.getId().equals(tag.getId())) {
                                        isSelected = true;
                                        break;
                                    }
                                }
                            %>
                            <option value="<%= tag.getId() %>" <%= isSelected ? "selected" : "" %>><%= tag.getName() %></option>
                            <% } %>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Update Task</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>