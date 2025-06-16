package com.homebanking.homebanking.cli;

import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.exceptions.DniInvalidoException;
import com.homebanking.homebanking.exceptions.MontoInvalidoException;
import com.homebanking.homebanking.models.CuentaBancaria;
import com.homebanking.homebanking.models.TipoMovimiento;
import com.homebanking.homebanking.services.ClienteService;
import com.homebanking.homebanking.services.CuentaBancariaService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.homebanking.homebanking.InterfazOperacionesUsuarios.InterfazOperaciones;

@Component
@Profile("cli")
public class InterfazCli implements InterfazOperaciones{

    private final ClienteService clienteService;
    private final CuentaBancariaService cuentaBancariaService;
    private final Scanner scanner = new Scanner(System.in);

    public InterfazCli(ClienteService clienteService, CuentaBancariaService cuentaBancariaService) {
        this.clienteService = clienteService;
        this.cuentaBancariaService = cuentaBancariaService;
    }


    public List<CuentaBancaria> obtenerCuentasPorDni(String dni){
        return cuentaBancariaService.encontrarCuentasPorDNI(dni);
    }

    public CuentaBancaria getCuentaBancaria(Long nroCuenta){

        return cuentaBancariaService.buscarCuenta(nroCuenta);
    }

    private void depositar(CuentaBancaria cuenta) {
        while (true) {
            try {
                System.out.print("Monto a depositar (o '0' para cancelar): ");
                String montoString = scanner.nextLine();
                
                if (montoString.equals("0")) {
                    System.out.println("Operación cancelada.");
                    return;
                }
                
                
                double monto = Double.parseDouble(montoString);

                cuentaBancariaService.depositar(cuenta.getNroCuenta(), monto);

                CuentaBancaria cuentaActualizada = cuentaBancariaService.buscarCuenta(cuenta.getNroCuenta());
                cuenta.setCuenta(cuentaActualizada.getCuenta());

                System.out.println("Depósito realizado con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n",
                        cuenta.getNroCuenta(), cuenta.getCuenta().getSaldo());

                return;

            } catch (MontoInvalidoException e) {
                System.out.println(e.getMessage());
            }catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un monto valido");
            }
        }
    }

    private void retirar(CuentaBancaria cuenta) {
        while (true) {
            try {
                System.out.print("Monto a retirar (o '0' para cancelar): ");
                double monto = Double.parseDouble(scanner.nextLine());
                if (monto == 0) {
                    System.out.println("Operación cancelada.");
                    return;
                }

                cuentaBancariaService.retirar(cuenta.getNroCuenta(), monto);

                CuentaBancaria cuentaActualizada = cuentaBancariaService.buscarCuenta(cuenta.getNroCuenta());
                cuenta.setCuenta(cuentaActualizada.getCuenta());

                System.out.println("Retiro realizado con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", cuenta.getNroCuenta(), cuenta.getCuenta().getSaldo());

                return; // salir del bucle
            } catch (MontoInvalidoException e) {
                System.out.println(e.getMessage());
            }catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un monto valido");
            }
        }
    }

    private void transferir(CuentaBancaria cuentaOrigen) {
        while (true) {
            try {
                System.out.print("Nro de cuenta destino (o '0' para cancelar): ");
                Long nroDestino = Long.parseLong(scanner.nextLine());
                if (nroDestino == 0) {
                    System.out.println("Transferencia cancelada.");
                    return;
                }
                
                System.out.print("Monto a transferir: ");
                double monto = Double.parseDouble(scanner.nextLine());
                
                CuentaBancaria cuentaDestino = getCuentaBancaria(nroDestino);
                cuentaBancariaService.transferir(cuentaOrigen, cuentaDestino, monto);

                cuentaOrigen = getCuentaBancaria(cuentaOrigen.getNroCuenta());
                System.out.println("Transferencia realizada con éxito.");
                System.out.printf("Saldo actual de la cuenta Nº %d: $%.2f\n", cuentaOrigen.getNroCuenta(), cuentaOrigen.getCuenta().getSaldo());
                return; // salir del bucle
                } catch (MontoInvalidoException e) {
                    System.out.println(e.getMessage());
                }catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un monto valido");
                }
            }
    }

    private void obtenerMovimientos(CuentaBancaria cuenta) {
        System.out.println("\nMovimientos de la cuenta Nº " + cuenta.getNroCuenta());
        var movimientos = cuentaBancariaService.obtenerMovimientosDeCuenta(cuenta.getNroCuenta());
        
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (var mov : movimientos) {
            String fechaFormateada = mov.getFechaMovimiento().format(formatter);
            String descripcionLegible = mov.getDescripcion().name();
            double monto = mov.getMonto();

            System.out.printf("%s - %s de $%.2f\n", fechaFormateada, descripcionLegible, monto);
        }
        }
    }



}