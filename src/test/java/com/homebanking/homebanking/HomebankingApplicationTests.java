package com.homebanking.homebanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;
import com.homebanking.homebanking.models.CajaDeAhorro;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.Transferencia;

//@SpringBootTest
class HomebankingApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testDepositar(){

		CajaDeAhorro cajaDeAhorro = new CajaDeAhorro();
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		double saldoActual = cuentaB.getSaldo();

		cuentaB.getCuenta().depositar(100.0);

		assertEquals(saldoActual + 100.0, cuentaB.getSaldo());
	}

	@Test
	void testRetirar(){
		CajaDeAhorro cajaDeAhorro = new CajaDeAhorro(200.0);
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		double saldoActual = cuentaB.getSaldo();

		cuentaB.getCuenta().retirar(100.0);

		assertEquals(saldoActual - 100.0, cuentaB.getSaldo());
	}

	@Test
	void testRetirarInvalido(){
		CajaDeAhorro cajaDeAhorro = new CajaDeAhorro(50.0);
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {cuentaB.getCuenta().retirar(100.0);;});

    	assertEquals("Saldo insuficiente", exception.getMessage());
	}

	@Test
	void testTransferencia(){
		CajaDeAhorro cajaDeAhorroOrigen = new CajaDeAhorro(300.0);
		CuentaBancaria cuentaOrigen= new CuentaBancaria(cajaDeAhorroOrigen);

		CajaDeAhorro cajaDeAhorroDestino = new CajaDeAhorro(100.0);
		CuentaBancaria cuentaDestino= new CuentaBancaria(cajaDeAhorroDestino);

		Transferencia transferencia = new Transferencia(cuentaOrigen, cuentaDestino, 200.0);
		transferencia.ejecutar();

		assertEquals(300.0, cuentaDestino.getSaldo());
		assertEquals(100.0, cuentaOrigen.getSaldo());
	}

	@Test
	void testTransferenciaInvalida(){
		CajaDeAhorro cajaDeAhorroOrigen = new CajaDeAhorro(100.0);
		CuentaBancaria cuentaOrigen= new CuentaBancaria(cajaDeAhorroOrigen);

		CajaDeAhorro cajaDeAhorroDestino = new CajaDeAhorro(300.0);
		CuentaBancaria cuentaDestino= new CuentaBancaria(cajaDeAhorroDestino);

		Transferencia transferencia = new Transferencia(cuentaOrigen, cuentaDestino, 200.0);

		Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {transferencia.ejecutar();});

    	assertEquals("Saldo insuficiente", exception.getMessage());
	}
}
