document.addEventListener("DOMContentLoaded", function () {
  const dni = sessionStorage.getItem("dni");
  const selectCuenta = document.getElementById("nroCuentas");

  fetch(`http://localhost:8080/cuentas-bancarias/${dni}`)
    .then(res => {
      if (!res.ok) throw new Error("No se pudieron cargar las cuentas");
      return res.json();
    })
    .then(cuentas => {
      selectCuenta.innerHTML = '<option value="" disabled selected>--Seleccione una cuenta--</option>';
      cuentas.forEach(cuenta => {
        const option = document.createElement("option");
        option.value = cuenta.nroCuenta;
        option.textContent = cuenta.nroCuenta;
        selectCuenta.appendChild(option);
      });
    })
    .catch(() => {
      selectCuenta.innerHTML = '<option value="" disabled>Error al cargar</option>';
    });
});

function mostrarMensaje(texto, tipo) {
  const mensaje = document.getElementById("mensaje");
  mensaje.textContent = texto;
  mensaje.className = tipo === "success" ? "text-success" : "text-danger";
}

function realizarRetiro() {
  const nroCuenta = document.getElementById("nroCuentas").value;
  const monto = document.getElementById("monto").value;

  fetch(`http://localhost:8080/cuentas-bancarias/retirar?nroCuenta=${nroCuenta}&monto=${monto}`, {
    method: "POST"
  })
  .then(res => {
    if (res.ok) {
        mostrarMensaje("Retiro exitoso.", "success");
        document.getElementById("nroCuentas").selectedIndex = 0;
        document.getElementById("monto").value = "";
    } else {
        const contentType = res.headers.get("content-type") || "";

        if (contentType.includes("application/json")) {
        return res.json().then(data => {
            mostrarMensaje(data.message, "danger");
        });
        } else {
        return res.text().then(text => {
            mostrarMensaje(text, "danger");
        });
        }
    }
    })
    .catch(() => {
    mostrarMensaje("No se pudo realizar el retiro.", "danger");
    });
}
