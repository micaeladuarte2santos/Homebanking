package com.homebanking.homebanking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;
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
            //agregar exepcion si el dni no existe
        }
        return cuentaBancariaRepository.cuentasPorDni(dni);
    }

    public void depositar(Long nroCuenta, double monto) {
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(nroCuenta)
        //agregar excepcion de cuenta no encontrada
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        cuenta.depositar(monto);
        cuentaBancariaRepository.save(cuenta);
        //falta guardar el movimiento
    }

    public void retirar(Long nroCuenta, double monto) {
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(nroCuenta)
        //agregar excepcion de cuenta no encontrada
        //fijarse que en monto a retirar sea menor-igual al monto en cuenta
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        cuenta.retirar(monto);
        cuentaBancariaRepository.save(cuenta);
    }

    public void transferir(Long origenId, Long destinoId, double monto) {
        CuentaBancaria origen = cuentaBancariaRepository.findById(origenId)
        //agregar excepcion de cuenta no encontrada
                .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada"));
        CuentaBancaria destino = cuentaBancariaRepository.findById(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada"));

        origen.retirar(monto);
        destino.depositar(monto);

        cuentaBancariaRepository.save(origen);
        cuentaBancariaRepository.save(destino);
    }

    public List<Movimiento> obtenerMovimientosDeCuenta(Long nroCuenta){
        //chequear que la cuenta exista
        return movimientoRepository.findMovimientosByNroCuenta(nroCuenta);
    }
    
}