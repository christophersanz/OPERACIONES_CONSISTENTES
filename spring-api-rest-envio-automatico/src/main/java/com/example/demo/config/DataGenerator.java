package com.example.demo.config;

import com.example.demo.model.Registro;
import com.example.demo.repository.RegistroRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class DataGenerator implements CommandLineRunner {

    @Autowired
    private RegistroRepository registroRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Generar y guardar 1000 registros ficticios
        IntStream.range(0, 10000).forEach(i -> {
            Registro registro = new Registro();
            registro.setNombre(faker.name().fullName());
            registro.setDescripcion(faker.lorem().sentence());

            registroRepository.save(registro);
        });
    }
}
