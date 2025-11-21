document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('loginModal');
    const modalBody = modal.querySelector('.modal-body');
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const curso = button.getAttribute('data-curso');
        switch (curso) {
            case 'java':
                modalBody.innerHTML = `
            <h5>Curso de Java</h5>
            <p> Bienvenido al curso de Java</p>
            <button>Saber más</button>
            `;
                break;
            case 'php':
                modalBody.innerHTML = `
                <h5>Curso de PHP</h5>
                <p> Bienvenido al curso de PHP</p>
                `;
                break;
        }
    });
});