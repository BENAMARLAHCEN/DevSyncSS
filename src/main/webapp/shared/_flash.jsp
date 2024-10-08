<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
