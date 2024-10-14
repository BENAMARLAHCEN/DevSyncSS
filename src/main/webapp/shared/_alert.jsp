<% String errorMessage = (String) request.getSession().getAttribute("error");
    String successMessage = (String) request.getSession().getAttribute("success");
    if (errorMessage != null) { %>
<div class="alert alert-danger" role="alert">
    <%= errorMessage %>
</div>
<% } else if (successMessage != null) { %>
<div class="alert alert-success" role="alert">
    <%= successMessage %>
</div>
<% } %>
<% request.getSession().removeAttribute("error");
    request.getSession().removeAttribute("success"); %>
