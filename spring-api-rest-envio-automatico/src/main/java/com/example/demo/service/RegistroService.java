package com.example.demo.service;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    public Page<Registro> obtenerRegistrosPaginados(Pageable pageable) {
        return registroRepository.findAll(pageable);
    }
}
