package com.homebanking.homebanking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homebanking.homebanking.exceptions.CuentaInexistenteException;
import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;
import com.homebanking.homebanking.models.TipoMovimiento;
import com.homebanking.homebanking.repositories.ClienteRepository;
import com.homebanking.homebanking.repositories.CuentaBancariaRepository;
import com.homebanking.homebanking.repositories.MovimientoRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CuentaBancariaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;


    public Optional<CuentaBancaria> buscarCuenta(Long nroCuenta) {
        return cuentaBancariaRepository.findById(nroCuenta);
    }

    public List<CuentaBancaria> encontrarCuentasPorDNI(String dni){
        if(clienteRepository.findByDni(dni).isEmpty()){
            throw new DniInexistenteException(dni);
        }
        return cuentaBancariaRepository.cuentasPorDni(dni);
    }

    public void depositar(Long nroCuenta, double monto) {
        //chequeo si existe la cuenta en la bd, sino lanzo excepcion
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(nroCuenta)
        .orElseThrow(() -> new CuentaInexistenteException(nroCuenta));
        
        //obtengo la caja de ahorro de la cuenta bancaria y deposito en ella el monto
        cuentaBancaria.getCuenta().depositar(monto);
        cuentaBancariaRepository.save(cuentaBancaria);//guardo y actualizo el saldo de la cuenta
        
        //guardo el movimiento de la cuenta bancaria
        Movimiento mov = new Movimiento(nroCuenta,TipoMovimiento.Deposito, monto);
        movimientoRepository.save(mov);
    }

    public void retirar(Long nroCuenta, double monto) {
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(nroCuenta)
        .orElseThrow(() -> new  CuentaInexistenteException(nroCuenta));

        cuentaBancaria.getCuenta().retirar(monto);
        cuentaBancariaRepository.save(cuentaBancaria);

        Movimiento mov = new Movimiento(nroCuenta,TipoMovimiento.Retiro, -monto);
        movimientoRepository.save(mov);
    }

    /*public void transferir(Long nroCuentaOrigen, Long nroCuentaDestino, double monto) {
        CuentaBancaria origen = cuentaBancariaRepository.findById(nroCuentaOrigen)
                .orElseThrow(() -> new CuentaInexistenteException(nroCuentaOrigen));

        CuentaBancaria destino = cuentaBancariaRepository.findById(nroCuentaDestino)
                .orElseThrow(() -> new CuentaInexistenteException(nroCuentaDestino));

        origen.getCuenta().depositar(monto);
        destino.depositar(monto);

        cuentaBancariaRepository.save(origen);
        cuentaBancariaRepository.save(destino);
    }*/

    public List<Movimiento> obtenerMovimientosDeCuenta(Long nroCuenta){
        //chequear que la cuenta exista
        return movimientoRepository.findMovimientosByNroCuenta(nroCuenta);
    }

    /*
     * Scanner scanner = new Scanner(System.in);

// Supongamos que ya buscaste las cuentas del usuario:
List<Cuenta> cuentas = cuentaService.obtenerCuentasPorUsuario(usuarioId);

// Mostrar las cuentas como un "DDL" de texto:
System.out.println("Seleccione una cuenta:");
for (int i = 0; i < cuentas.size(); i++) {
    Cuenta cuenta = cuentas.get(i);
    System.out.printf("%d. %s - %s%n", i + 1, cuenta.getTipo(), cuenta.getNumero());
}

// Leer opción del usuario:
int opcion = scanner.nextInt();
scanner.nextLine(); // limpiar buffer

// Validar y obtener la cuenta seleccionada:
if (opcion < 1 || opcion > cuentas.size()) {
    System.out.println("Opción inválida.");
} else {
    Cuenta cuentaSeleccionada = cuentas.get(opcion - 1);
    System.out.println("Seleccionó: " + cuentaSeleccionada.getNumero());
    // Podés seguir con una transferencia, consulta de saldo, etc.
}

     */
    
}