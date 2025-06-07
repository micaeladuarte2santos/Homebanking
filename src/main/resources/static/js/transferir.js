document.addEventListener("DOMContentLoaded", () => {
  const dni = sessionStorage.getItem("dni");
  const selectCuentaO = document.getElementById("NumCuentaO");
  const inputCuentaD = document.getElementById("NumCuentaD");
  const inputMonto = document.getElementById("monto");
  const mensaje = document.getElementById("mensaje");

  // cargo el ddl de origen
  fetch(`http://localhost:8080/cuentas-bancarias/dni/${dni}`)
    .then(res => {
      if (!res.ok) throw new Error("No se pudieron cargar las cuentas");
      return res.json();
    })
    .then(cuentas => {
      selectCuentaO.innerHTML = '<option value="" disabled selected>--Seleccione una cuenta--</option>';
      cuentas.forEach(cuenta => {
        const option = document.createElement("option");
        option.value = cuenta.nroCuenta;
        option.textContent = cuenta.nroCuenta;
        selectCuentaO.appendChild(option);
      });
    })
    .catch(() => {
      selectCuentaO.innerHTML = '<option value="" disabled>Error al cargar</option>';
    });

  // transfiero
  window.realizarTransferencia = () => {
    mensaje.textContent = "";
    mensaje.className = "";

    const nroCuentaOrigen = selectCuentaO.value;
    const nroCuentaDestino = inputCuentaD.value.trim();
    const monto = parseFloat(inputMonto.value);

    if (!nroCuentaOrigen) {
      mensaje.textContent = "Por favor, seleccione la cuenta origen.";
      mensaje.className = "text-danger";
      return;
    }
    if (!nroCuentaDestino) {
      mensaje.textContent = "Ingrese el número de cuenta destino.";
      mensaje.className = "text-danger";
      return;
    }
    if (isNaN(monto) || monto <= 0) {
      mensaje.textContent = "Ingrese un monto válido.";
      mensaje.className = "text-danger";
      return;
    }

    // validv que la cuenta destino exista
    fetch(`http://localhost:8080/cuentas-bancarias/${nroCuentaDestino}`)
      .then(res => {
        if (!res.ok) {
          return res.text().then(msg => {
            throw new Error(msg); 
          });
        }
        return res.json();
      })
      .then(() => {
        // si existe hago la transferencia
        return fetch(`http://localhost:8080/cuentas-bancarias/transferir?nroCuentaOrigen=${nroCuentaOrigen}&nroCuentaDestino=${nroCuentaDestino}&monto=${monto}`, {
          method: "POST"
        });
      })
      .then(res => {
        if (res.ok) {
          mensaje.textContent = "Transferencia realizada con éxito.";
          mensaje.className = "text-success";

          selectCuentaO.selectedIndex = 0;
          inputCuentaD.value = "";
          inputMonto.value = "";
        } else {
          const contentType = res.headers.get("content-type") || "";
          if (contentType.includes("application/json")) {
            return res.json().then(data => {
              throw new Error(data.message || "Error al realizar la transferencia.");
            });
          } else {
            return res.text().then(text => {
              throw new Error(text || "Error al realizar la transferencia.");
            });
          }
        }
      })
      .catch(err => {
        mensaje.textContent = err.message || "No se pudo realizar la transferencia.";
        mensaje.className = "text-danger";
      });
  };
});
