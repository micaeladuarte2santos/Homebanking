package com.homebanking.homebanking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;
import com.homebanking.homebanking.services.CuentaBancariaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cuentas-bancarias")
@RequiredArgsConstructor
public class CuentaBancariaController {

    @Autowired
    private CuentaBancariaService cuentaService;

    @GetMapping("/{dni}")
    public List<CuentaBancaria> obtenerCuentasPorDni(@PathVariable String dni) {
        return cuentaService.encontrarCuentasPorDNI(dni);
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestParam Long nroCuenta, @RequestParam double monto) {
        cuentaService.depositar(nroCuenta, monto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(@RequestParam Long nroCuenta, @RequestParam double monto) {
        cuentaService.retirar(nroCuenta, monto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transferir")
    public ResponseEntity<Void> transferir(@RequestParam Long nroCuentaOrigen, @RequestParam Long nroCuentaDestino, @RequestParam double monto) {

        CuentaBancaria origen = cuentaService.buscarCuenta(nroCuentaOrigen);
        CuentaBancaria destino = cuentaService.buscarCuenta(nroCuentaDestino);

        cuentaService.transferir(origen, destino, monto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{nroCuenta}/movimientos")
    public List<Movimiento> obtenerMovimientos(@PathVariable Long nroCuenta) {
        return cuentaService.obtenerMovimientosDeCuenta(nroCuenta);
    }
    
}
