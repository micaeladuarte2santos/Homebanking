function verificarDni() {
    const dni = document.getElementById("dni").value;
    const mensaje = document.getElementById("mensaje");

    fetch(`/clientes/existe/${dni}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(textoError => {
                    throw new Error(textoError);
                });
            }
            return response.json();
        })
        .then(data => {
            mensaje.textContent = "DNI válido. Redirigiendo...";
            mensaje.className = "exito";
            setTimeout(() => {
                window.location.href = "/cuentas.html";
            }, 1500);
        })
        .catch(err => {
            mensaje.textContent = err.message;
            mensaje.className = "error";
        });
}
