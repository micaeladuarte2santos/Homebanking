document.addEventListener("DOMContentLoaded", function () {
  const dni = sessionStorage.getItem("dni");

  fetch(`http://localhost:8080/cuentas-bancarias/${dni}`)
    .then(response => response.json())
    .then(data => {
      const tableBody = document.getElementById("tablaCuentas");
      tableBody.innerHTML = "";

      data.forEach(cuenta => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
          <td>${cuenta.nroCuenta}</td>
          <td>${cuenta.cliente.dni}</td>
          <td>$${cuenta.saldo.toFixed(2)}</td>
        `;
        tableBody.appendChild(fila);
      });
    })
    .catch(error => {
      console.error("Error al obtener cuentas:", error);
    });
});
