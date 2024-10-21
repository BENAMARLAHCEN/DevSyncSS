<%@ page import="com.example.devsyncss.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.devsyncss.entities.Tag" %>
<%@ page import="com.example.devsyncss.entities.Task" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tasks</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
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

        .select2-container--default .select2-selection--multiple {
            border: 1px solid #ced4da;
        }

        .daterangepicker {
            color: #343a40;
        }
    </style>
</head>
<body>
<% List<Tag> tags = (List<Tag>) request.getAttribute("tags"); %>
<% List<Task> tasks = (List<Task>) request.getAttribute("tasks"); %>
<% List<User> users = (List<User>) request.getAttribute("users"); %>

<%@ include file="shared/_header.jsp" %>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <%@ include file="shared/_alert.jsp" %>
                <h1>Tasks</h1>

                <form method="get" action="<%= request.getContextPath() %>/tasks">
                    <div class="form-row">
                        <div class="form-group col-md-5">
                            <label for="filterTag">Filter by Tags</label>
                            <select id="filterTag" name="tags[]" class="form-control" multiple>
                                <% for (Tag tag : tags) { %>
                                <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="form-group col-md-5">
                            <label for="dateRange">Date Range</label>
                            <input type="text" id="dateRange" name="dateRange" class="form-control">
                        </div>
                        <div class="form-group col-md-2 " style="margin-top: 30px;">
                            <button type="submit" class="btn btn-primary">Filter</button>
                        </div>
                    </div>
                </form>

                <button type="button" class="btn btn-primary mb-3" data-toggle="modal" data-target="#createTaskModal">
                    Create Task
                </button>
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Title</th>
                            <th scope="col">Description</th>
                            <th scope="col">Status</th>
                            <% if (user.getRole().name().equals("MANAGER")) { %>
                                <th scope="col">Assignee</th>
                            <% } %>
                            <th scope="col">Due Date</th>
                            <th scope="col">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Task task : tasks) { %>
                            <tr>
                                <th scope="row"><%= task.getId() %></th>
                                <td><%= task.getTitle() %></td>
                                <td><%= task.getDescription() %></td>
                                <td><%= task.getStatus() %></td>
                                <% if (user.getRole().name().equals("MANAGER")) { %>
                                    <td><%= task.getAssignedTo().getFirstName() %> <%= task.getAssignedTo().getLastName() %></td>
                                <% } %>
                                <td><%= task.getDueDate() %></td>

                                <td>
                                    <% if (task.getCreatedBy().getId() == user.getId()) { %>
                                    <a href="tasks/<%= task.getId() %>" class="btn btn-primary">Edit</a>
                                    <a href="delete-task/<%= task.getId() %>" class="btn btn-danger">Delete</a>
                                    <% } %>
                                    <% if (user.getRole().name().equals("USER") && !task.getCreatedBy().getId().equals(user.getId())) { %>
                                        <a href="#" class="btn btn-danger" onclick="submitDeleteRequest(<%= task.getId() %>);">Delete Request</a>
                                        <a href="#" class="btn btn-primary" data-toggle="modal" data-target="#changeRequestModal" onclick="setTaskId(<%= task.getId() %>);">Send Change Request</a>
                                    <script>
                                        function setTaskId(taskId) {
                                        document.getElementById('modalTaskId').value = taskId;
                                        }

                                        function submitDeleteRequest(taskId) {
                                            var form = document.createElement('form');
                                            form.method = 'post';
                                            form.action = '<%= request.getContextPath() %>/send-delete-request';
                                            var input = document.createElement('input');
                                            input.type = 'hidden';
                                            input.name = 'taskId';
                                            input.value = taskId;
                                            form.appendChild(input);
                                            document.body.appendChild(form);
                                            form.submit();
                                        }
                                    </script>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="changeRequestModal" tabindex="-1" aria-labelledby="changeRequestModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="changeRequestModalLabel">Send Change Request</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="<%= request.getContextPath() %>/send-change-request" method="post">
                        <input type="hidden" id="modalTaskId" name="taskId">
                        <div class="form-group mb-2">
                            <label for="changeDescription">Change Description</label>
                            <textarea class="form-control" id="changeDescription" name="changeDescription" required></textarea>
                        </div>
                        <div class="form-group mb-2">
                            <button type="submit" class="btn btn-primary">Send Request</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <div class="modal fade" id="createTaskModal" tabindex="-1" aria-labelledby="createTaskModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createTaskModalLabel">Create Task</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="<%= request.getContextPath() %>/tasks" method="post">
                        <div class="form-group mb-2">
                            <label for="title">Title</label>
                            <input type="text" class="form-control" id="title" name="title" required>
                        </div>
                        <div class="form-group mb-2">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" required></textarea>
                        </div>
                        <div class="form-group mb-2">
                            <label for="status">Status</label>
                            <select name="status" id="status" class="form-control" required>
                                <option value="PENDING">Pending</option>
                                <option value="IN_PROGRESS">In Progress</option>
                                <option value="COMPLETED">Completed</option>
                                <option value="CANCELLED">Cancelled</option>
                            </select>
                        </div>
                        <% if (user.getRole().name().equals("MANAGER")) { %>
                        <div class="form-group mb-2">
                            <label for="assignee">Assignee</label>
                            <select name="assignedTo" id="assignee" class="form-control" required>
                                <option value="" selected>Select Assignee</option>
                                <% for (User u : users) { %>
                                    <option value="<%= u.getId() %>"><%= u.getFirstName() %> <%= u.getLastName() %></option>
                                <% } %>
                            </select>
                        </div>
                        <% } else { %>
                        <input type="hidden" name="assignedTo" value="<%= user.getId() %>">
                        <% } %>
                        <div class="form-group mb-2">
                            <label for="dueDate">Due Date</label>
                            <input type="datetime-local" class="form-control" id="dueDate" name="dueDate" required>
                        </div>
                        <div class="form-group mb-2">
                            <label for="tags">Tags</label>
                            <select name="tags[]" id="tags" class="form-control" multiple>
                                <% for (Tag tag : tags) { %>
                                    <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-group mb-2">
                            <button type="submit" class="btn btn-primary">Create</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#filterTag').select2({
                placeholder: "Select tags",
                allowClear: true
            });

            $('#dateRange').daterangepicker({
                opens: 'left',
                locale: {
                    format: 'YYYY-MM-DD'
                }
            });
        });
    </script>
</body>
</html>