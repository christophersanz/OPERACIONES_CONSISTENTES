package com.example.demo.controller;

import com.example.demo.model.Registro;
import com.example.demo.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RestController
@EnableScheduling
public class EnvioAutoController {

    private final RegistroService registroService;
    private final RestTemplate restTemplate;

    @Autowired
    public EnvioAutoController(RegistroService registroService, RestTemplate restTemplate) {
        this.registroService = registroService;
        this.restTemplate = restTemplate;
    }

    // Endpoint para el envío automático incremental
    @Scheduled(fixedDelay = 10000) // Envía cada 10 segundos (ajusta según necesites)
    public void enviarRegistrosAutomaticamente() {
        int pageSize = 10; // Tamaño de cada página
        int pageNumber = 0; // Número de página inicial

        try {
            while (true) {
                // Obtener registros paginados
                Page<Registro> registros = registroService.obtenerRegistrosPaginados(PageRequest.of(pageNumber, pageSize));
                List<Registro> registrosList = registros.getContent();

                // Verificar si la lista de registros está vacía
                if (registrosList.isEmpty()) {
                    // Si no hay más registros, termina el envío
                    break;
                }

                // Lógica para enviar los registros a una API a través de HTTP
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<List<Registro>> request = new HttpEntity<>(registrosList, headers);
                String urlDestino = "http://insertar-app:9081/api/insertar-registros-en-lotes";
                restTemplate.postForObject(urlDestino, request, String.class);

                if (!registros.hasNext()) {
                    // Si no hay más registros, termina el envío
                    break;
                }

                pageNumber++; // Avanzar a la siguiente página
            }
        } catch (Exception e) {
            System.err.println("Error al enviar los registros: " + e.getMessage());
        }
    }
}