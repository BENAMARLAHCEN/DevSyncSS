<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
    .error {
        display: none;
    }
</style>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) { %>
    <div class="error"
         data-title="Error"
         data-message="<%= error %>">
    </div>
<% } %>

<script>
    const error = document.querySelector('.error');
    if (error) {
        Swal.fire({
            icon: 'error',
            title: error.dataset.title,
            text: error.dataset.message,
            showConfirmButton: false,
            timer: 3000
        });
    }
</script>