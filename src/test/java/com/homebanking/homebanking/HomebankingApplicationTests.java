/*package com.homebanking.homebanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
import com.homebanking.homebanking.entity.CajaDeAhorro;
import com.homebanking.homebanking.entity.Cuenta;
import com.homebanking.homebanking.entity.CuentaBancaria;
import com.homebanking.homebanking.entity.Transferencia;

//@SpringBootTest
class HomebankingApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testDepositar(){

		Cuenta cajaDeAhorro = new CajaDeAhorro();
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		double saldoActual = cuentaB.getSaldo();

		cuentaB.depositar(100);

		assertEquals(saldoActual + 100, cuentaB.getSaldo());
	}

	@Test
	void testRetirar(){
		Cuenta cajaDeAhorro = new CajaDeAhorro(200);
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		double saldoActual = cuentaB.getSaldo();

		cuentaB.retirar(100);

		assertEquals(saldoActual - 100, cuentaB.getSaldo());
	}

	@Test
	void testRetirarInvalido(){
		Cuenta cajaDeAhorro = new CajaDeAhorro(50);
		CuentaBancaria cuentaB= new CuentaBancaria(cajaDeAhorro);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {cuentaB.retirar(100);;});

    	assertEquals("Saldo insuficiente", exception.getMessage());
	}

	@Test
	void testTransferencia(){
		Cuenta cajaDeAhorroOrigen = new CajaDeAhorro(300);
		CuentaBancaria cuentaOrigen= new CuentaBancaria(cajaDeAhorroOrigen);

		Cuenta cajaDeAhorroDestino = new CajaDeAhorro(100);
		CuentaBancaria cuentaDestino= new CuentaBancaria(cajaDeAhorroDestino);

		Transferencia transferencia = new Transferencia(cuentaOrigen, cuentaDestino, 200);
		transferencia.ejecutar();

		assertEquals(300, cuentaDestino.getSaldo());
		assertEquals(100, cuentaOrigen.getSaldo());
	}

	@Test
	void testTransferenciaInvalida(){
		Cuenta cajaDeAhorroOrigen = new CajaDeAhorro(100);
		CuentaBancaria cuentaOrigen= new CuentaBancaria(cajaDeAhorroOrigen);

		Cuenta cajaDeAhorroDestino = new CajaDeAhorro(300);
		CuentaBancaria cuentaDestino= new CuentaBancaria(cajaDeAhorroDestino);

		Transferencia transferencia = new Transferencia(cuentaOrigen, cuentaDestino, 200);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {transferencia.ejecutar();});

    	assertEquals("Saldo insuficiente en transferencia", exception.getMessage());
	}
}*/
