document.addEventListener("DOMContentLoaded", function () {
  const dni = sessionStorage.getItem("dni");
  const selectCuenta = document.getElementById("nroCuentas");

  fetch(`http://localhost:8080/cuentas-bancarias/dni/${dni}`)
    .then(res => res.json())
    .then(cuentas => {
      selectCuenta.innerHTML = '<option value="" disabled selected>Seleccione una cuenta</option>';
      cuentas.forEach(cuenta => {
        const option = document.createElement("option");
        option.value = cuenta.nroCuenta;
        option.textContent = cuenta.nroCuenta;
        selectCuenta.appendChild(option);
      });
    })
    .catch(() => {
      selectCuenta.innerHTML = '<option>Error al cargar</option>';
    });
});

function realizarDeposito() {
  const nroCuenta = document.getElementById("nroCuentas").value;
  const monto = document.getElementById("monto").value;
  const mensaje = document.getElementById("mensaje");


  fetch(`http://localhost:8080/cuentas-bancarias/depositar?nroCuenta=${nroCuenta}&monto=${monto}`, {
    method: "POST"
  })
    .then(res => {
      if (res.ok) {
        mensaje.textContent = "Depósito exitoso.";
        mensaje.className = "text-success";

        document.getElementById("nroCuentas").selectedIndex = 0;
        document.getElementById("monto").value = "";
      } else {
        return res.json().then(data => {
          mensaje.textContent = data.message || "Error al depositar.";
          mensaje.className = "text-danger";
        });
      }
    })
    .catch(() => {
      mensaje.textContent = "No se pudo realizar el depósito.";
      mensaje.className = "text-danger";
    });
}
