package com.example.demo.controller;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EnvioApiController {

    private RegistroRepository registroRepository;

    @Autowired
    public EnvioApiController(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> insertarRegistro(@RequestBody Registro registro) {
        try {
            registroRepository.save(registro);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registro guardado!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar registro: " + e.getMessage());
        }
    }

}
