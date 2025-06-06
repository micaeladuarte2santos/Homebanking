package com.homebanking.homebanking.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.homebanking.homebanking.exceptions.CuentaInexistenteException;
import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;
import com.homebanking.homebanking.models.CajaDeAhorro;
import com.homebanking.homebanking.models.Cliente;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.repositories.ClienteRepository;
import com.homebanking.homebanking.repositories.CuentaBancariaRepository;
import com.homebanking.homebanking.repositories.MovimientoRepository;
import com.homebanking.homebanking.services.CuentaBancariaService;

public class TestCuentaBancariaService {

    @InjectMocks
    private CuentaBancariaService cuentaBancariaService;

    @Mock
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @BeforeEach
	public void inicializar() {
		MockitoAnnotations.openMocks(this);
	}


    @Test
    void testEncontrarCuentasPorDniExistente() {
        String dni = "43101667";
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        CuentaBancaria c1 = new CuentaBancaria(null);
        c1.setCliente(cliente);

        CuentaBancaria c2 = new CuentaBancaria(null);
        c2.setCliente(cliente);

        List<CuentaBancaria> cuentas = List.of(c1,c2);

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(cliente));//encuentra al cliente
        when(cuentaBancariaRepository.cuentasPorDni(dni)).thenReturn(cuentas);//encuentra las cuentas

        List<CuentaBancaria> resultado = cuentaBancariaService.encontrarCuentasPorDNI(dni);

        assertEquals(2, resultado.size());
    }

    @Test
    void testEncontrarCuentasPorDniInexiste() {
        String dni = "11236457";

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.empty());

        assertThrows(DniInexistenteException.class, () -> cuentaBancariaService.encontrarCuentasPorDNI(dni));

    }

    @Test
    void testDepositarExitoso() {
        Long nroCuenta = 1L;
        double monto = 500.0;

        CajaDeAhorro cuenta = mock(CajaDeAhorro.class);
        CuentaBancaria cuentaBancaria = new CuentaBancaria(null);
        cuentaBancaria.setNroCuenta(nroCuenta);
        cuentaBancaria.setCuenta(cuenta);

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.of(cuentaBancaria));

        cuentaBancariaService.depositar(nroCuenta, monto);

        verify(cuentaBancariaRepository).findById(nroCuenta);
        verify(cuenta).depositar(monto);
        verify(cuentaBancariaRepository).save(cuentaBancaria);
    }

    @Test
    void testDepositarAMontoInvalido() {
        Long nroCuenta = 1L;
        double monto = -100.0;

        CajaDeAhorro cuenta = mock(CajaDeAhorro.class);
        CuentaBancaria cuentaBancaria = new CuentaBancaria(null);
        cuentaBancaria.setCuenta(cuenta);;

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.of(cuentaBancaria));
        
        doThrow(new MontoInvalidoException()).when(cuenta).depositar(monto);
        assertThrows(MontoInvalidoException.class, () -> cuentaBancariaService.depositar(nroCuenta, monto));
    }

    @Test
    void testDepositarACuentaInexistente() {
        Long nroCuenta = 999L;
        double monto = 200.0;

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.empty());

        assertThrows(CuentaInexistenteException.class, () -> cuentaBancariaService.depositar(nroCuenta, monto));
    }

    @Test
    void testRetirarExitoso() {
        Long nroCuenta = 1L;
        double monto = 200.0;

        CajaDeAhorro cuenta = new CajaDeAhorro(300);
        CuentaBancaria cuentaBancaria = new CuentaBancaria(null);
        cuentaBancaria.setNroCuenta(nroCuenta);
        cuentaBancaria.setCuenta(cuenta);

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.of(cuentaBancaria));

        cuentaBancariaService.retirar(nroCuenta, monto);

        assertEquals(100, cuenta.getSaldo());
    }

    @Test
    void testRetirarSaldoInsuficiente() {
        Long nroCuenta = 1L;
        double monto = 200.0;

        CajaDeAhorro cuenta = new CajaDeAhorro(100);
        CuentaBancaria cuentaBancaria = new CuentaBancaria(null);
        cuentaBancaria.setNroCuenta(nroCuenta);
        cuentaBancaria.setCuenta(cuenta);

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.of(cuentaBancaria));
        assertThrows(SaldoInsuficienteException.class, () -> cuentaBancariaService.retirar(nroCuenta, monto));
    }

    @Test
    void testRetirarACuentaInexistente() {
        Long nroCuenta = 999L;
        double monto = 200.0;

        when(cuentaBancariaRepository.findById(nroCuenta)).thenReturn(Optional.empty());

        assertThrows(CuentaInexistenteException.class, () -> cuentaBancariaService.retirar(nroCuenta, monto));
    }

    @Test
    void testTransferenciaValida(){
        Long nroCuentaO = 1L;
        Long nroCuentaD = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaO = new CajaDeAhorro(500);
        CajaDeAhorro cuentaD = new CajaDeAhorro(200);

        CuentaBancaria cuentaBancariaO = new CuentaBancaria(null);
        cuentaBancariaO.setNroCuenta(nroCuentaO);
        cuentaBancariaO.setCuenta(cuentaO);

        CuentaBancaria cuentaBancariaD = new CuentaBancaria(null);
        cuentaBancariaD.setNroCuenta(nroCuentaD);
        cuentaBancariaD.setCuenta(cuentaD);

        when(cuentaBancariaRepository.existsById(nroCuentaO)).thenReturn(true);
        when(cuentaBancariaRepository.existsById(nroCuentaD)).thenReturn(true);

        cuentaBancariaService.transferir(cuentaBancariaO, cuentaBancariaD, monto);

        assertEquals(400, cuentaD.getSaldo());
        assertEquals(300, cuentaO.getSaldo());
    }

    @Test
    void testTransferenciaSaldoInsuficiente(){
        Long nroCuentaO = 1L;
        Long nroCuentaD = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaO = new CajaDeAhorro(100);
        CajaDeAhorro cuentaD = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaO = new CuentaBancaria(null);
        cuentaBancariaO.setNroCuenta(nroCuentaO);
        cuentaBancariaO.setCuenta(cuentaO);

        CuentaBancaria cuentaBancariaD = new CuentaBancaria(null);
        cuentaBancariaD.setNroCuenta(nroCuentaD);
        cuentaBancariaD.setCuenta(cuentaD);

        when(cuentaBancariaRepository.existsById(nroCuentaO)).thenReturn(true);
        when(cuentaBancariaRepository.existsById(nroCuentaD)).thenReturn(true);

        assertThrows(SaldoInsuficienteException.class, () -> cuentaBancariaService.transferir(cuentaBancariaO, cuentaBancariaD, monto));
    }

    @Test
    void testTransferenciaCuentaInexistente(){
        Long nroCuentaO = 1L;
        Long nroCuentaD = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaO = new CajaDeAhorro(100);
        CajaDeAhorro cuentaD = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaO = new CuentaBancaria(null);
        cuentaBancariaO.setNroCuenta(nroCuentaO);
        cuentaBancariaO.setCuenta(cuentaO);

        CuentaBancaria cuentaBancariaD = new CuentaBancaria(null);
        cuentaBancariaD.setNroCuenta(nroCuentaD);
        cuentaBancariaD.setCuenta(cuentaD);

        when(cuentaBancariaRepository.existsById(nroCuentaO)).thenReturn(true);
        when(cuentaBancariaRepository.findById(nroCuentaD)).thenReturn(Optional.empty());

        assertThrows(CuentaInexistenteException.class, () -> cuentaBancariaService.transferir(cuentaBancariaO, cuentaBancariaD, monto));
        
    }

    @Test
    void testTransferenciaMontoInvalido(){
        Long nroCuentaO = 1L;
        Long nroCuentaD = 2L;
        double monto = -200.0;

        CajaDeAhorro cuentaO = new CajaDeAhorro(1000);
        CajaDeAhorro cuentaD = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaO = new CuentaBancaria(null);
        cuentaBancariaO.setNroCuenta(nroCuentaO);
        cuentaBancariaO.setCuenta(cuentaO);

        CuentaBancaria cuentaBancariaD = new CuentaBancaria(null);
        cuentaBancariaD.setNroCuenta(nroCuentaD);
        cuentaBancariaD.setCuenta(cuentaD);

        when(cuentaBancariaRepository.existsById(nroCuentaO)).thenReturn(true);
        when(cuentaBancariaRepository.existsById(nroCuentaD)).thenReturn(true);

        assertThrows(MontoInvalidoException.class, () -> cuentaBancariaService.transferir(cuentaBancariaO, cuentaBancariaD, monto));
        
    }

}
