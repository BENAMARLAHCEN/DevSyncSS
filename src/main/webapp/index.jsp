
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            color: #343a40;
        }

        .header {
            background-color: #343a40;
            color: #fff;
        }

        .navbar-light .navbar-brand {
            color: #fff;
        }

        .navbar-light .navbar-brand:hover {
            color: #ffc107;
        }

        .btn-primary {
            background-color: #343a40;
            border: none;
        }

        .btn-primary:hover {
            background-color: #23272b;
        }

        .content {
            margin-top: 50px;
        }

        .card {
            margin: 20px 0;
        }
    </style>
</head>
<body>

    <%@include file="shared/_nav.jsp" %>
    <div class="container content">
    <h2 class="text-center my-4">Welcome to DevSync</h2>
    <p class="lead text-center mb-5">Your one-stop solution for task management and collaboration.</p>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title">Task Board</h5>
                    <p class="card-text">View and manage your tasks.</p>
                    <a href="taskBoard" class="btn btn-primary">Go to Task Board</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title">Tasks</h5>
                    <p class="card-text">Create, edit, and delete tasks.</p>
                    <a href="tasks" class="btn btn-primary">Manage Tasks</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title">Profile</h5>
                    <p class="card-text">View and edit your profile.</p>
                    <a href="profile" class="btn btn-primary">Go to Profile</a>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .content {
        margin-top: 50px;
    }

    .card {
        margin: 20px 0;
        transition: transform 0.3s, box-shadow 0.3s;
    }

    .card:hover {
        transform: translateY(-10px);
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
    }

    .card-title {
        font-size: 1.5rem;
        color: #343a40;
    }

    .card-text {
        color: #6c757d;
    }

    .btn-primary {
        background-color: #343a40;
        border: none;
        transition: background-color 0.3s;
    }

    .btn-primary:hover {
        background-color: #23272b;
    }
</style>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>