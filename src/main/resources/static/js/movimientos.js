document.addEventListener("DOMContentLoaded", function () {
  const dni = sessionStorage.getItem("dni");
  const selectCuenta = document.getElementById("cuentaSelect");

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
      selectCuenta.innerHTML = '<option>Error al cargar las cuentas</option>';
    });
});

function mostrarMovimientos() {
  const nroCuenta = document.getElementById("cuentaSelect").value;
  const mensaje = document.getElementById("mensaje");
  const tabla = document.getElementById("tablamovimientos");

  mensaje.textContent = "";
  tabla.innerHTML = "";

  if (!nroCuenta) {
    mensaje.textContent = "Por favor, seleccione una cuenta.";
    mensaje.className = "text-danger";
    return;
  }

  fetch(`http://localhost:8080/cuentas-bancarias/${nroCuenta}/movimientos`)
    .then(res => {
      if (!res.ok) throw new Error("Error al obtener movimientos");
      return res.json();
    })
    .then(movimientos => {
      if (movimientos.length === 0) {
        tabla.innerHTML = `<tr><td colspan="5" class="text-center">No hay movimientos para esta cuenta.</td></tr>`;
        return;
      }

      movimientos.forEach(m => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
          <td>${m.id}</td>
          <td>${m.cuenta.nroCuenta}</td>
          <td>${m.descripcion || ''}</td>
          <td>${m.monto}</td>
          <td>${new Date(m.fecha).toLocaleString()}</td>
        `;
        tabla.appendChild(fila);
      });
    })
    .catch(() => {
      mensaje.textContent = "Error al cargar movimientos.";
      mensaje.className = "text-danger";
    });
}
