<!-- Login Page (login.jsp) -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container">
  <div class="row py-5 mt-4 align-items-center">
    <div class="col-md-6 ml-auto">
      <h1>Login</h1>
      <form action="login" method="post">
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary">Login</button>
        </div>
        <p class="text-center">Don't have an account? <a href="register">Register</a></p>
      </form>
    </div>
  </div>
</div>

</body>
</html>
