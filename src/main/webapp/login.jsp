<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" href="assets/css/auth.css">
  <script src="https://kit.fontawesome.com/a076d05399.js"></script>
  <script src="assets/js/auth.js"></script>
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

    .input-group-text {
      background-color: #343a40;
      color: #fff;
      border: none;
    }

    .input-group-text .fa {
      color: #ffc107;
    }

    .form-control {
      border: 1px solid #343a40;
    }

    .btn-primary {
      background-color: #343a40;
      border: none;
    }

    .btn-primary:hover {
      background-color: #23272b;
    }
  </style>
</head>
<body>

<%@include file="shared/_nav.jsp" %>

<div class="container">
    <%@ include file="shared/_alert.jsp" %>

  <div class="row py-5 mt-4 align-items-center">
    <div class="col-md-5 pr-lg-5 mb-5 mb-md-0">
        <img src="https://bootstrapious.com/i/snippets/sn-registeration/illustration.svg" alt="" class="img-fluid mb-3 d-none d-md-block">
        <h1>Login</h1>
        <p class="font-italic text-muted mb-0">Login using the form below.</p>
    </div>

    <!-- Login Form -->
    <div class="col-md-7 col-lg-6 ml-auto">
        <form action="login" method="post">
            <div class="row">
                <div class="input-group col-lg-12 mb-4">
                    <div class="input-group-prepend">
                            <span class="input-group-text bg-white px-4 border-md border-right-0">
                                <i class="fa fa-envelope text-muted"></i>
                            </span>
                    </div>
                    <input id="email" type="email" name="email" placeholder="Email Address" class="form-control bg-white border-left-0 border-md" required>
                </div>

                <div class="input-group col-lg-12 mb-4">
                    <div class="input-group-prepend">
                            <span class="input-group-text bg-white px-4 border-md border-right-0">
                                <i class="fa fa-lock text-muted"></i>
                            </span>
                    </div>
                    <input id="password" type="password" name="password" placeholder="Password" class="form-control bg-white border-left-0 border-md" required>
                </div>

                <!-- Submit Button -->
                <div class="form-group col-lg-12 mx-auto mb-0">
                    <button type="submit" class="btn btn-primary btn-block py-2">
                        <span class="font-weight-bold">Login</span>
                    </button>
                </div>
            </div>
        </form>
    </div>
    </div>
</div>

</body>
</html>