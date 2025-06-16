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

        CuentaBancaria cuenta1 = new CuentaBancaria(null);
        cuenta1.setCliente(cliente);

        CuentaBancaria cuenta2 = new CuentaBancaria(null);
        cuenta2.setCliente(cliente);

        List<CuentaBancaria> cuentas = List.of(cuenta1,cuenta2);

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
        cuentaBancaria.setCuenta(cuenta);

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
        Long nroCuentaOrigen = 1L;
        Long nroCuentaDestino = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaOrigen = new CajaDeAhorro(500);
        CajaDeAhorro cuentaDestino = new CajaDeAhorro(200);

        CuentaBancaria cuentaBancariaOrigen = new CuentaBancaria(null);
        cuentaBancariaOrigen.setNroCuenta(nroCuentaOrigen);
        cuentaBancariaOrigen.setCuenta(cuentaOrigen);

        CuentaBancaria cuentaBancariaDestino = new CuentaBancaria(null);
        cuentaBancariaDestino.setNroCuenta(nroCuentaDestino);
        cuentaBancariaDestino.setCuenta(cuentaDestino);

        when(cuentaBancariaRepository.findById(nroCuentaOrigen)).thenReturn(Optional.of(cuentaBancariaOrigen));
        when(cuentaBancariaRepository.findById(nroCuentaDestino)).thenReturn(Optional.of(cuentaBancariaDestino));

        cuentaBancariaService.transferir(cuentaBancariaOrigen, cuentaBancariaDestino, monto);

        assertEquals(400, cuentaDestino.getSaldo());
        assertEquals(300, cuentaOrigen.getSaldo());
    }

    @Test
    void testTransferenciaSaldoInsuficiente(){
        Long nroCuentaOrigen = 1L;
        Long nroCuentaDestino = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaOrigen = new CajaDeAhorro(100);
        CajaDeAhorro cuentaDestino = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaOrigen = new CuentaBancaria(null);
        cuentaBancariaOrigen.setNroCuenta(nroCuentaOrigen);
        cuentaBancariaOrigen.setCuenta(cuentaOrigen);

        CuentaBancaria cuentaBancariaDestino = new CuentaBancaria(null);
        cuentaBancariaDestino.setNroCuenta(nroCuentaDestino);
        cuentaBancariaDestino.setCuenta(cuentaDestino);

        when(cuentaBancariaRepository.findById(nroCuentaOrigen)).thenReturn(Optional.of(cuentaBancariaOrigen));
        when(cuentaBancariaRepository.findById(nroCuentaDestino)).thenReturn(Optional.of(cuentaBancariaDestino));


        assertThrows(SaldoInsuficienteException.class, () -> cuentaBancariaService.transferir(cuentaBancariaOrigen, cuentaBancariaDestino, monto));
    }

    @Test
    void testTransferenciaCuentaInexistente(){
        Long nroCuentaOrigen = 1L;
        Long nroCuentaDestino = 2L;
        double monto = 200.0;

        CajaDeAhorro cuentaOrigen = new CajaDeAhorro(100);
        CajaDeAhorro cuentaDestino = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaOrigen = new CuentaBancaria(null);
        cuentaBancariaOrigen.setNroCuenta(nroCuentaOrigen);
        cuentaBancariaOrigen.setCuenta(cuentaOrigen);

        CuentaBancaria cuentaBancariaDestino = new CuentaBancaria(null);
        cuentaBancariaDestino.setNroCuenta(nroCuentaDestino);
        cuentaBancariaDestino.setCuenta(cuentaDestino);

        when(cuentaBancariaRepository.existsById(nroCuentaOrigen)).thenReturn(true);
        when(cuentaBancariaRepository.findById(nroCuentaDestino)).thenReturn(Optional.empty());

        assertThrows(CuentaInexistenteException.class, () -> cuentaBancariaService.transferir(cuentaBancariaOrigen, cuentaBancariaDestino, monto));
        
    }

    @Test
    void testTransferenciaMontoInvalido(){
        Long nroCuentaOrigen = 1L;
        Long nroCuentaDestino = 2L;
        double monto = -200.0;

        CajaDeAhorro cuentaOrigen = new CajaDeAhorro(1000);
        CajaDeAhorro cuentaDestino = new CajaDeAhorro(500);

        CuentaBancaria cuentaBancariaOrigen = new CuentaBancaria(null);
        cuentaBancariaOrigen.setNroCuenta(nroCuentaOrigen);
        cuentaBancariaOrigen.setCuenta(cuentaOrigen);

        CuentaBancaria cuentaBancariaDestino = new CuentaBancaria(null);
        cuentaBancariaDestino.setNroCuenta(nroCuentaDestino);
        cuentaBancariaDestino.setCuenta(cuentaDestino);

        when(cuentaBancariaRepository.findById(nroCuentaOrigen)).thenReturn(Optional.of(cuentaBancariaOrigen));
        when(cuentaBancariaRepository.findById(nroCuentaDestino)).thenReturn(Optional.of(cuentaBancariaDestino));

        assertThrows(MontoInvalidoException.class, () -> cuentaBancariaService.transferir(cuentaBancariaOrigen, cuentaBancariaDestino, monto));
        
    }

}
