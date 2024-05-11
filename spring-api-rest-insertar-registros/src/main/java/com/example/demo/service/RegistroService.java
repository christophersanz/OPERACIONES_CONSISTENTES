package com.example.demo.service;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import java.util.List;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED) // garantiza que se puedan ejecutar otras operaciones concurrentes
    public void insertarRegistro(Registro registro) throws Exception {
        try {
            registroRepository.save(registro);
        } catch (Exception e) {
            System.out.println("Error al insertar registro: " + e.getMessage());
            throw e; // Se lanza la excepción para que la transacción sea anulada
        }
    }

    /*@Transactional(isolation = Isolation.READ_COMMITTED) // garantiza que se puedan ejecutar otras operaciones concurrentes
    public void insertarRegistrosEnLotes(List<Registro> registros) throws Exception {
        int batchSize = 1000; // Tamaño del lote
        for (int i = 0; i < registros.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, registros.size());
            List<Registro> batch = registros.subList(i, endIndex);
            registroRepository.saveAll(batch);
        }
    }*/

    @Transactional(isolation = Isolation.READ_COMMITTED) // garantiza que se puedan ejecutar otras operaciones concurrentes
    public void insertarRegistrosEnLotes(List<Registro> registros) throws Exception {
        try {
            registroRepository.saveAll(registros);
        } catch (Exception e) {
            System.out.println("Error al insertar registros en lotes: " + e.getMessage());
            throw e; // Se lanza la excepción para que la transacción sea anulada
        }
    }

}

