package com.homebanking.homebanking.cli;

import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.TipoMovimiento;
import com.homebanking.homebanking.services.ClienteService;
import com.homebanking.homebanking.services.CuentaBancariaService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
@Profile("cli")
public class InterfazCli {

    private final ClienteService clienteService;
    private final CuentaBancariaService cuentaBancariaService;
    private final Scanner scanner = new Scanner(System.in);

    public InterfazCli(ClienteService clienteService, CuentaBancariaService cuentaBancariaService) {
        this.clienteService = clienteService;
        this.cuentaBancariaService = cuentaBancariaService;
    }
    
    public void iniciar() {
       try {
        String dni = solicitarDni();
        clienteService.existeDni(dni);

        List<CuentaBancaria> cuentas = cuentaBancariaService.encontrarCuentasPorDNI(dni);
        if (cuentas.isEmpty()) {
            System.out.println("No tiene cuentas asociadas.");
            return;
        }

        CuentaBancaria cuentaSeleccionada = seleccionarCuenta(cuentas);
        realizarOperaciones(cuentaSeleccionada);
       } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String solicitarDni() {
        System.out.print("Ingrese su DNI: ");
        return scanner.nextLine();
    }

    private CuentaBancaria seleccionarCuenta(List<CuentaBancaria> cuentas) {
        System.out.println("\nSus cuentas:");
        for (int i = 0; i < cuentas.size(); i++) {
            CuentaBancaria c = cuentas.get(i);
            System.out.printf("%d - Cuenta Nº %d - Saldo: $%.2f\n", i + 1, c.getNroCuenta(), c.getCuenta().getSaldo());
        }

        System.out.print("Seleccione una cuenta: ");
        int opcion = Integer.parseInt(scanner.nextLine());
        return cuentas.get(opcion - 1);
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

    private void depositar(CuentaBancaria cuenta) {
        System.out.print("Monto a depositar: ");
        double monto = Double.parseDouble(scanner.nextLine());
        cuentaBancariaService.depositar(cuenta.getNroCuenta(), monto);

        CuentaBancaria cuentaActualizada = cuentaBancariaService.buscarCuenta(cuenta.getNroCuenta()); //recarga la cuenta desde la base de datos para obtener el saldo actualizado
        cuenta.setCuenta(cuentaActualizada.getCuenta()); //actualiza el saldo en el objeto en memoria (no cambia la referencia, solo los datos)
        System.out.println("Depósito realizado con éxito.");
    }

    private void retirar(CuentaBancaria cuenta) {
        System.out.print("Monto a retirar: ");
        double monto = Double.parseDouble(scanner.nextLine());
        cuentaBancariaService.retirar(cuenta.getNroCuenta(), monto);

        CuentaBancaria cuentaActualizada = cuentaBancariaService.buscarCuenta(cuenta.getNroCuenta());
        cuenta.setCuenta(cuentaActualizada.getCuenta());
        System.out.println("Retiro realizado con éxito.");
    }

    private void transferir(CuentaBancaria cuentaOrigen) {
        System.out.print("Nro de cuenta destino: ");
        Long nroDestino = Long.parseLong(scanner.nextLine());
        System.out.print("Monto a transferir: ");
        double monto = Double.parseDouble(scanner.nextLine());

        CuentaBancaria cuentaDestino = cuentaBancariaService.buscarCuenta(nroDestino);
        cuentaBancariaService.transferir(cuentaOrigen, cuentaDestino, monto);

        cuentaOrigen = cuentaBancariaService.buscarCuenta(cuentaOrigen.getNroCuenta());//refrescar la cuenta origen para que el saldo esté actualizado

        System.out.println("Transferencia realizada con éxito."); //actualizar referencia
    }

    private void mostrarMovimientos(CuentaBancaria cuenta) {
        System.out.println("\nMovimientos de la cuenta Nº " + cuenta.getNroCuenta());
        var movimientos = cuentaBancariaService.obtenerMovimientosDeCuenta(cuenta.getNroCuenta());
        
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (var mov : movimientos) {
            String fechaFormateada = mov.getFechaMovimiento().format(formatter);
            String descripcionLegible = obtenerDescripcionLegible(mov.getDescripcion());
            double monto = mov.getMonto();

            System.out.printf("%s - %s de $%.2f\n", fechaFormateada, descripcionLegible, monto);
        }
        }
    }

    private String obtenerDescripcionLegible(TipoMovimiento tipo) {
        switch (tipo) {
            case Deposito:
                return "Depósito";
            case Retiro:
                return "Retiro";
            case Transferencia:
                return "Transferencia";
            default:
                return tipo.name();
        }
    }

    private void mostrarSaldo(CuentaBancaria cuenta) {
        System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", cuenta.getNroCuenta(), cuenta.getCuenta().getSaldo());
    }

}