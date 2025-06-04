package com.homebanking.homebanking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.homebanking.homebanking.models.Movimiento;

@Repository
public interface MovimientoRepository extends CrudRepository<Movimiento, Long>{

    @Query("SELECT m FROM Movimiento m WHERE m.nroCuenta=?1")
    public List<Movimiento> findMovimientosByNroCuenta(Long nroCuenta);

}
