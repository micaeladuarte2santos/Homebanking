package com.homebanking.homebanking.cli;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.homebanking.homebanking.exceptions.CuentaInexistenteException;
import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.exceptions.SaldoInsuficienteException;
import com.homebanking.homebanking.models.CuentaBancaria;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

@Component
@Profile("cli")
public class TransfenciaCli {

    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    private InterfazServiceCli interfazServiceCli;

    public void transferir(CuentaBancaria cuentaOrigen) {
        while (true) {
            try {
                System.out.print("Nro de cuenta destino (o '0' para cancelar): ");
                Long nroCuentaDestino = Long.parseLong(scanner.nextLine());

                if (nroCuentaDestino == 0) {
                    System.out.println("Transferencia cancelada.");
                    return;
                }
                
                System.out.print("Monto a transferir: ");
                double monto = Double.parseDouble(scanner.nextLine());
                
                interfazServiceCli.getCuentaBancaria(nroCuentaDestino);
                CuentaBancaria cuentaOrigenActual = interfazServiceCli.transferir(cuentaOrigen.getNroCuenta(), nroCuentaDestino, monto);

                System.out.println("Transferencia realizada con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", cuentaOrigen.getNroCuenta(), cuentaOrigenActual.getSaldo());
                return; // salir del bucle
                } catch (MontoInvalidoException e) {
                    System.out.println(e.getMessage());
                }catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un monto valido");
                }catch(CuentaInexistenteException e){
                    System.out.println(e.getMessage());
                }catch(SaldoInsuficienteException e){
                    System.out.println(e.getMessage());
                }
            }
    }
}
