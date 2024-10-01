<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="user" method="post">
    <%--@declare id="role"--%><%--@declare id="managerid"--%>
    <input type="text" name="username" placeholder="Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="text" name="firstName" placeholder="First Name" required>
    <input type="text" name="lastName" placeholder="Last Name" required>
    <input type="email" name="email" placeholder="Email" required>

    <label for="role">Role:</label>
    <select name="role" required>
        <option value="USER">User</option>
        <option value="MANAGER">Manager</option>
    </select>

    <label for="managerId">Manager ID (optional):</label>
    <input type="text" name="managerId" placeholder="Manager ID">

    <button type="submit">Add User</button>
</form>

<h2>All Users:</h2>
<ul>
    <jsp:useBean id="users" scope="request" type="java.util.List"/>
    <c:forEach var="user" items="${users}">
        <li>${user.username} (${user.firstName} ${user.lastName}) - ${user.role}</li>
    </c:forEach>
</ul>

</body>
</html>
