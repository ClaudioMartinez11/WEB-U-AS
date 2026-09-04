package com.citas.reserva;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        Reserva reserva = repository.guardar(request, hora, LocalDateTime.now());
        excelExporter.exportar(repository.listar());
        return reserva;
    }

    public List<Reserva> listar() { return repository.listar(); }
    public void exportar() { excelExporter.exportar(repository.listar()); }
}