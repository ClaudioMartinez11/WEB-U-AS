package com.citas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservasApplication {
    public static void main(String[] args) {
        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo crear la carpeta data", exception);
        }
        SpringApplication.run(ReservasApplication.class, args);
    }
}