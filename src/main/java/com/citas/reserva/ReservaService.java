package com.citas.reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReservaService {
    private final ReservaRepository repository;
    private final ExcelExporter excelExporter;

    public ReservaService(ReservaRepository repository, ExcelExporter excelExporter) {
        this.repository = repository;
        this.excelExporter = excelExporter;
    }

    public synchronized Reserva crear(CrearReservaRequest request) {
        if (request.fecha().isBefore(LocalDate.now())) throw new IllegalArgumentException("La fecha no puede estar en el pasado");
        LocalTime hora = LocalTime.parse(request.hora());
        return repository.guardar(request, hora, LocalDateTime.now());
    }

    public List<Reserva> listar() { return repository.listar(); }
    public void exportar() { excelExporter.exportar(repository.listar()); }
}