package com.homebanking.homebanking.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.homebanking.homebanking.exceptions.DniInexistenteException;
import com.homebanking.homebanking.exceptions.DniInvalidoException;
import com.homebanking.homebanking.models.Cliente;
import com.homebanking.homebanking.repositories.ClienteRepository;
import com.homebanking.homebanking.services.ClienteService;
import com.homebanking.homebanking.validador.ValidarCliente;

public class TestClienteService {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
	private ValidarCliente validadorCliente;

    @BeforeEach
	public void inicializar() {
		MockitoAnnotations.openMocks(this);
	}

    @Test
    public void testBuscarClientePorDniExistente() {
        String dni = "43101667";
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarClientePorDni(dni);

        assertEquals(cliente, resultado);
    }

    @Test
    public void testBuscarClientePorDniInexistente(){
        String dni = "43101667";

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.empty());

        assertThrows(DniInexistenteException.class,()-> {
            clienteService.buscarClientePorDni(dni);
        });
    }

    @Test
    public void testBuscarClientePorDniInvalido(){
        String dni = "4310166A";
       
        //simula que el validador lanza la excepcion
        doThrow(new DniInvalidoException(dni)).when(validadorCliente).validar(dni);

        assertThrows(DniInvalidoException.class, () -> {
            clienteService.buscarClientePorDni(dni);
        });
    }

}
