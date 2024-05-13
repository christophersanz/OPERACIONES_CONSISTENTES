package com.example.demo.controller;

import com.example.demo.model.Registro;
import com.example.demo.service.RegistroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping("/insertar-registros-en-lotes")
    public ResponseEntity<String> insertarRegistros(@RequestBody List<Registro> registros) {
        try {
            log.info("Insertando registros en lotes...");
            registroService.insertarRegistrosEnLotes(registros);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registros en lote insertados exitosamente.");
        } catch (Exception e) {
            log.error("Error al insertar los registros en lotes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar los registros en lotes: " + e.getMessage());
        }
    }

    @PostMapping("/insertar-registro")
    public ResponseEntity<String> insertarRegistro(@RequestBody Registro registro) {
        try {
            registroService.insertarRegistro(registro);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registro insertado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar el registro: " + e.getMessage());
        }
    }

}
