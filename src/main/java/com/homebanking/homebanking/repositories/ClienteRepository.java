package com.homebanking.homebanking.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.homebanking.homebanking.entity.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.dni=?1")
    public Optional<Cliente> findByDni(String dni);
    
}
