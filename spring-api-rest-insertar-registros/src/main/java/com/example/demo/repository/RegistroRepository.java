package com.example.demo.repository;

import com.example.demo.model.Registro;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    // Aquí puedes agregar métodos personalizados de consulta si es necesario
}
