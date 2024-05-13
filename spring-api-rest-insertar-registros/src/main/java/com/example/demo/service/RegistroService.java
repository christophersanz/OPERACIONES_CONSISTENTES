package com.example.demo.service;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertarRegistrosEnLotes(List<Registro> registros) throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        registros.parallelStream().forEach(registro -> {
            try {
                insertarRegistro(registro);
                counter.incrementAndGet();
            } catch (Exception e) {
                log.error("Error al insertar registro: {}", e.getMessage());
            }
        });
        log.info("Se han insertado {} registros en lotes.", counter.get());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertarRegistro(Registro registro) throws Exception {
        registroRepository.save(registro);
    }
}