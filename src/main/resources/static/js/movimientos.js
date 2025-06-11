document.addEventListener('DOMContentLoaded', () => {
  const dni = sessionStorage.getItem("dniCliente");
  const select = document.getElementById('cuentaSelect');
  const tabla = document.getElementById('tablamovimientos');
  const btnVer = document.getElementById('btnMostrarMovimientos');

  // cuentas del cliente
  fetch(`http://localhost:8080/cuentas-bancarias/dni/${dni}`)
    .then(res => {
      if (!res.ok) throw new Error("No se pudieron cargar las cuentas");
      return res.json();
    })
    .then(cuentas => {
      select.innerHTML = '<option value="" disabled selected>--Seleccione una cuenta--</option>';
      cuentas.forEach(cuenta => {
        const option = document.createElement("option");
        option.value = cuenta.nroCuenta;
        option.textContent = cuenta.nroCuenta;
        select.appendChild(option);
      });
    })
    .catch(() => {
      select.innerHTML = '<option value="" disabled>Error al cargar</option>';
    });

  // Al hacer clic en "Ver movimientos"
  btnVer.addEventListener('click', () => {
    const nroCuenta = select.value;
    if (!nroCuenta) return;

    fetch(`http://localhost:8080/cuentas-bancarias/${nroCuenta}/movimientos`)
      .then(response => {
        if (!response.ok) throw new Error('Error al obtener movimientos');
        return response.json();
      })
      .then(movimientos => {
        tabla.innerHTML = '';
        if (movimientos.length === 0) {
          tabla.innerHTML = `<tr><td colspan="4" class="text-center">No hay movimientos para esta cuenta.</td></tr>`;
          return;
        }

        movimientos.forEach(m => {
          const fila = document.createElement('tr');
          fila.innerHTML = `
            <td>${m.id}</td>
            <td>${new Date(m.fecha).toLocaleString()}</td>
            <td>${m.monto}</td>
            <td>${m.cuenta.nroCuenta}</td>
          `;
          tabla.appendChild(fila);
        });
      })
      .catch(err => {
        console.error(err);
        tabla.innerHTML = `<tr><td colspan="4" class="text-center">Error al cargar movimientos</td></tr>`;
      });
  });
});
