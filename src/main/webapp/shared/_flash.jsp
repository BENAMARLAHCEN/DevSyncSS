<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
    .flash {
        display: none;
    }
</style>

<%
    Flash flash = (Flash) request.getAttribute("error");
    if (flash != null) { %>
    <div class="flash"
         data-type="${flash.type}"
         data-title="${flash.title}"
         data-message="${flash.message}">
    </div>
<% } %>
<script>
    const flash = document.querySelector('.flash');
    if (flash) {
        Swal.fire({
            icon: flash.dataset.type,
            title: flash.dataset.title,
            text: flash.dataset.message,
            showConfirmButton: false,
            timer: 3000
        });
    }
</script>
