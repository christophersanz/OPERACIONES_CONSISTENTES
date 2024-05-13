package com.example.demo.controller;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvioApiController {

    private RegistroRepository registroRepository;

    private static final Logger logger = LoggerFactory.getLogger(EnvioApiController.class);

    @Autowired
    public EnvioApiController(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    @PostMapping("/api/registrar")
    public ResponseEntity<String> registrar(@RequestBody Registro registro) {
        try {
            logger.info("Solicitud recibida para registrar: {}", registro);
            registroRepository.save(registro);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registro guardado!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar registro: " + e.getMessage());
        }
    }

}
