document.addEventListener('DOMContentLoaded', function () {
    const inputsAndSelects = document.querySelectorAll('input, select');

    inputsAndSelects.forEach(function (element) {
        element.addEventListener('focus', function () {
            const inputGroupText = this.parentElement.querySelector('.input-group-text');
            if (inputGroupText) {
                inputGroupText.style.borderColor = '#80bdff';
            }
        });

        element.addEventListener('blur', function () {
            const inputGroupText = this.parentElement.querySelector('.input-group-text');
            if (inputGroupText) {
                inputGroupText.style.borderColor = '#ced4da';
            }
        });
    });
});
