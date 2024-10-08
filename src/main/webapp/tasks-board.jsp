<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.devsyncss.entities.Task"%>
<%@ page import="com.example.devsyncss.entities.Tag"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevSync Task Board</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .task-column {
            min-height: 600px;
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 15px;
        }
        .task-card {
            cursor: move;
            margin-bottom: 15px;
        }
        .task-card .card-body {
            padding: 10px;
        }
        .task-card .card-title {
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }
        .task-card .card-text {
            font-size: 0.875rem;
        }
        .task-tag {
            font-size: 0.75rem;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<%@ include file="shared/_header.jsp" %>
<div class="container-fluid mt-4">
    <h1 class="text-center mb-4">DevSync Task Board</h1>
    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="task-column" id="todo">
                <h2 class="h4 mb-3">To Do</h2>
                <%
                    List<Task> tasks = (List<Task>)request.getAttribute("tasks");
                    for (Task task : tasks) {
                        if (task.getStatus().toString().equals("PENDING")) {
                %>
                <div class="card task-card" draggable="true" id="task-<%= task.getId() %>">
                    <div class="card-body">
                        <h5 class="card-title"><%= task.getTitle() %></h5>
                        <p class="card-text"><%= task.getDescription() %></p>
                        <div>
                            <%
                                for (Tag tag : task.getTags()) {
                            %>
                            <span class="badge bg-secondary task-tag"><%= tag.getName() %></span>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="task-column" id="in-progress">
                <h2 class="h4 mb-3">In Progress</h2>
                <%
                    for (Task task : tasks) {
                        if (task.getStatus().toString().equals("IN_PROGRESS")) {
                %>
                <div class="card task-card" draggable="true" id="task-<%= task.getId() %>">
                    <div class="card-body">
                        <h5 class="card-title"><%= task.getTitle() %></h5>
                        <p class="card-text"><%= task.getDescription() %></p>
                        <div>
                            <%
                                for (Tag tag : task.getTags()) {
                            %>
                            <span class="badge bg-secondary task-tag"><%= tag.getName() %></span>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="task-column" id="done">
                <h2 class="h4 mb-3">Done</h2>
                <%
                    for (Task task : tasks) {
                        if (task.getStatus().toString().equals("COMPLETED")) {
                %>
                <div class="card task-card" draggable="true" id="task-<%= task.getId() %>">
                    <div class="card-body">
                        <h5 class="card-title"><%= task.getTitle() %></h5>
                        <p class="card-text"><%= task.getDescription() %></p>
                        <div>
                            <%
                                for (Tag tag : task.getTags()) {
                            %>
                            <span class="badge bg-secondary task-tag"><%= tag.getName() %></span>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        const tasks = document.querySelectorAll('.task-card');
        const columns = document.querySelectorAll('.task-column');

        tasks.forEach(task => {
            task.addEventListener('dragstart', dragStart);
            task.addEventListener('dragend', dragEnd);
        });

        columns.forEach(column => {
            column.addEventListener('dragover', dragOver);
            column.addEventListener('dragenter', dragEnter);
            column.addEventListener('dragleave', dragLeave);
            column.addEventListener('drop', drop);
        });

        function dragStart() {
            this.classList.add('dragging');
        }

        function dragEnd() {
            this.classList.remove('dragging');
        }

        function dragOver(e) {
            e.preventDefault();
        }

        function dragEnter(e) {
            e.preventDefault();
            this.classList.add('drag-over');
        }

        function dragLeave() {
            this.classList.remove('drag-over');
        }

        function drop() {
            this.classList.remove('drag-over');
            const task = document.querySelector('.dragging');
            this.appendChild(task);

            // Get the new status from the column id
            const newStatus = this.id.toUpperCase().replace('-', '_');
            const taskId = task.id.split('-')[1];

            // Send an AJAX request to update the task status
            updateTaskStatus(taskId, newStatus);
        }

        function updateTaskStatus(taskId, newStatus) {
            fetch('/tasks/updateStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `taskId=${taskId}&newStatus=${newStatus}`
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        console.log('Task status updated successfully');
                    } else {
                        console.error('Failed to update task status');
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    });
</script>
</body>
</html>