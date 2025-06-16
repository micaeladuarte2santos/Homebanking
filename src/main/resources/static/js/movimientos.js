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
      selectCuenta.innerHTML = '<option>Error al cargar</option>';
    });
});

let movimientos = [];
let pagina = 1;
const porPagina = 5;

function mostrarMovimientos() {
  const nroCuenta = document.getElementById("cuentaSelect").value;
  const mensaje = document.getElementById("mensaje");
  const tabla = document.getElementById("tablamovimientos");
  const paginacion = document.getElementById("paginacion");

  mensaje.textContent = "";
  tabla.innerHTML = "";
  paginacion.innerHTML = "";
  pagina = 1;

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
    .then(data => {
      movimientos = data;
      if (movimientos.length === 0) {
        tabla.innerHTML = `<tr><td colspan="5" class="text-center">No hay movimientos para esta cuenta.</td></tr>`;
        return;
      }
      renderPagina();
    })
    .catch(() => {
      mensaje.textContent = "Error al cargar movimientos.";
      mensaje.className = "text-danger";
    });
}

  function renderPagina() {
    const tabla = document.getElementById("tablamovimientos");
    const paginacion = document.getElementById("paginacion");

    tabla.innerHTML = "";
    paginacion.innerHTML = "";

    const totalPaginas = Math.ceil(movimientos.length / porPagina);
    const inicio = (pagina - 1) * porPagina;
    const fin = inicio + porPagina;
    const paginaMovs = movimientos.slice(inicio, fin);

    paginaMovs.forEach(m => {
      const fila = document.createElement("tr");
      fila.innerHTML = `
        <td>${m.id}</td>
        <td>${m.nroCuenta}</td>
        <td>${m.descripcion || ''}</td>
        <td>$${m.monto}</td>
        <td>${new Date(m.fechaMovimiento).toLocaleString()}</td>
      `;
      tabla.appendChild(fila);
    });

  // Botones de paginación
    for (let i = 1; i <= totalPaginas; i++) {
      const btn = document.createElement("button");
      btn.textContent = i;
      btn.className = `btn btn-sm mx-1 ${i === pagina ? 'btn-success' : 'btn-outline-success'}`;
      btn.onclick = function () {
        pagina = i;
        renderPagina();
      };
      paginacion.appendChild(btn);
  }
}

