package com.homebanking.homebanking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.homebanking.homebanking.models.CuentaBancaria;

@Repository
public interface CuentaBancariaRepository extends CrudRepository<CuentaBancaria, Long>{

    @Query("SELECT c FROM CuentaBancaria c WHERE c.cliente.dni =?1")
    public List<CuentaBancaria> cuentasPorDni(String dni);


}
