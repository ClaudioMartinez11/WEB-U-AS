package com.citas.reserva;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {
    private final ReservaService service;
    private final Path exportPath;

    public ReservaController(ReservaService service, @Value("${app.export.path}") String exportPath) {
        this.service = service;
        this.exportPath = Path.of(exportPath);
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody CrearReservaRequest request) {
        return ResponseEntity.status(201).body(ReservaResponse.from(service.crear(request)));
    }

    @GetMapping
    public List<ReservaResponse> listar() { return service.listar().stream().map(ReservaResponse::from).toList(); }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportar() {
        service.exportar();
        Resource resource = new FileSystemResource(exportPath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("reservas.xlsx").build().toString())
                .body(resource);
    }
}