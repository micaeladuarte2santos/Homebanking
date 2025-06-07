 document.addEventListener("DOMContentLoaded", () => {
    const dni = sessionStorage.getItem("dni");
    const tabla = document.getElementById("cliente-info-body");

    fetch(`http://localhost:8080/clientes/${dni}`)
      .then(res => {
        if (!res.ok) throw new Error("Cliente no encontrado");
        return res.json();
      })
      .then(cliente => {
        tabla.innerHTML = `
          <tr>
            <td>${cliente.dni}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.apellido}</td>
            <td>${cliente.email}</td>
          </tr>
        `;
      })
      .catch(err => {
        tabla.innerHTML = `<tr><td colspan="4" class="text-danger text-center">${err.message}</td></tr>`;
      });
  });

