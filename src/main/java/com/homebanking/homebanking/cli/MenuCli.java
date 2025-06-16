package com.homebanking.homebanking.cli;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.exceptions.DniInvalidoException;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.services.ClienteService;
import com.homebanking.homebanking.services.CuentaBancariaService;
import com.homebanking.homebanking.cli.InterfazCli;


@Component
@Profile("cli")
public class MenuCli {

    private final ClienteService clienteService;
    private final CuentaBancariaService cuentaBancariaService;
    private final Scanner scanner = new Scanner(System.in);
    private final InterfazCli cli;

    public MenuCli(ClienteService clienteService, CuentaBancariaService cuentaBancariaService, InterfazCli cli) {
        this.clienteService = clienteService;
        this.cuentaBancariaService = cuentaBancariaService;
        this.cli = cli;
    }

    private String solicitarDni() {
        System.out.print("Ingrese su DNI: ");
        return scanner.nextLine();
    }


    public void iniciar() {
       while(true){
            try {
            String dni = cli.solicitarDni();
            clienteService.existeDni(dni);

            List<CuentaBancaria> cuentas = cli.obtenerCuentasPorDni(dni);
            if (cuentas.isEmpty()) {
                System.out.println("No tiene cuentas asociadas.");
                return;
            }

            CuentaBancaria cuentaSeleccionada = seleccionarCuenta(cuentas);
            realizarOperaciones(cuentaSeleccionada);
        } catch (DniInexistenteException e) {
                System.out.println(e.getMessage());
            }catch(DniInvalidoException e){
                System.out.println(e.getMessage());
            }
       }
    }

     private CuentaBancaria seleccionarCuenta(List<CuentaBancaria> cuentas) {
        while (true) {
        System.out.println("\nSus cuentas:");
            for (int i = 0; i < cuentas.size(); i++) {
                CuentaBancaria cuenta = cuentas.get(i);
                System.out.printf("%d - Cuenta Nº %d - Saldo: $%.2f\n", i + 1, cuenta.getNroCuenta(), cuenta.getCuenta().getSaldo());
            }

        System.out.print("Seleccione una cuenta: ");
        String input = scanner.nextLine();

        try {
            int opcion = Integer.parseInt(input);

            if (opcion < 1 || opcion > cuentas.size()) {
                System.out.println("Opción invalida");
                continue;
            }

            return cuentas.get(opcion - 1);

        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
        }
    }
    }

    private void realizarOperaciones(CuentaBancaria cuenta) {
        while (true) {
            System.out.println("\nOperaciones disponibles:");
            System.out.println("1 - Ver saldo");
            System.out.println("2 - Depositar");
            System.out.println("3 - Retirar");
            System.out.println("4 - Transferir");
            System.out.println("5 - Ver movimientos");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opción: ");
            String operacion = scanner.nextLine();

            switch (operacion) {
                case "1" -> mostrarSaldo(cuenta);
                case "2" -> depositar(cuenta);
                case "3" -> retirar(cuenta);
                case "4" -> transferir(cuenta);
                case "5" -> mostrarMovimientos(cuenta);
                case "0" -> {
                    System.out.println("Saliendo...");
                    return; 
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

}
