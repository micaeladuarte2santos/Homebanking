package com.homebanking.homebanking.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.homebanking.homebanking.exceptions.CuentaInexistenteException;
import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Movimiento;
import com.homebanking.homebanking.models.TipoMovimiento;
import com.homebanking.homebanking.models.Transferencia;
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

    @Transactional(readOnly=true)
    public CuentaBancaria buscarCuenta(Long nroCuenta) {
        return cuentaBancariaRepository.findById(nroCuenta).orElseThrow(()-> new CuentaInexistenteException(nroCuenta));
    }

    @Transactional(readOnly=true)
    public List<CuentaBancaria> encontrarCuentasPorDNI(String dni){
        if(clienteRepository.findByDni(dni).isEmpty()){
            throw new DniInexistenteException(dni);
        }
        return cuentaBancariaRepository.cuentasPorDni(dni);
    }

    @Transactional
    public CuentaBancaria depositar(Long nroCuenta, double monto) {

        CuentaBancaria cuentaBancaria = buscarCuenta(nroCuenta);
        
        cuentaBancaria.getCuenta().depositar(monto);
        cuentaBancariaRepository.save(cuentaBancaria);
        
        Movimiento mov = new Movimiento(nroCuenta,TipoMovimiento.Deposito, monto);
        movimientoRepository.save(mov);

        return cuentaBancaria;
    }

    @Transactional
    public CuentaBancaria retirar(Long nroCuenta, double monto) {
        CuentaBancaria cuentaBancaria = buscarCuenta(nroCuenta);

        cuentaBancaria.getCuenta().retirar(monto);
        cuentaBancariaRepository.save(cuentaBancaria);

        Movimiento mov = new Movimiento(nroCuenta,TipoMovimiento.Retiro, -monto);
        movimientoRepository.save(mov);

        return cuentaBancaria;
    }

    @Transactional
    public void transferir(CuentaBancaria origen, CuentaBancaria destino, double monto) {
        buscarCuenta(origen.getNroCuenta());
        buscarCuenta(destino.getNroCuenta());

        Transferencia transferencia = new Transferencia(origen, destino, monto);
        transferencia.ejecutar();
        
        cuentaBancariaRepository.save(origen);
        cuentaBancariaRepository.save(destino);

        Movimiento movOrigen = new Movimiento(origen.getNroCuenta(),TipoMovimiento.Transferencia, -monto);
        movimientoRepository.save(movOrigen);
        
        Movimiento movDestino = new Movimiento(destino.getNroCuenta(),TipoMovimiento.Transferencia, monto);
        movimientoRepository.save(movDestino);
    }

    public List<Movimiento> obtenerMovimientosDeCuenta(Long nroCuenta){
        buscarCuenta(nroCuenta);
        return movimientoRepository.findMovimientosByNroCuenta(nroCuenta);
    }

    public double obtenerSaldo(Long nroCuenta){
        return cuentaBancariaRepository.getSaldoByNroCuenta(nroCuenta);
    }
    

    
}