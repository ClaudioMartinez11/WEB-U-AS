package com.citas.reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservaRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public Reserva guardar(CrearReservaRequest request, LocalTime hora, LocalDateTime creadoEn) {
        try {
                jdbcTemplate.update("INSERT INTO reservas(fecha, hora, nombre, telefono, creado_en) VALUES (?, ?, ?, ?, ?)",
                    request.fecha(), hora, request.nombre().trim(), request.telefono().trim(), creadoEn);
            return jdbcTemplate.queryForObject("SELECT id, fecha, hora, nombre, telefono, creado_en FROM reservas WHERE fecha = ? AND hora = ?",
                    (rs, rowNum) -> new Reserva(rs.getLong("id"), rs.getObject("fecha", LocalDate.class), rs.getObject("hora", LocalTime.class),
                        rs.getString("nombre"), rs.getString("telefono"), rs.getObject("creado_en", LocalDateTime.class)),
                    request.fecha(), hora);
        } catch (DataAccessException exception) {
            if (exception.getMessage() != null && exception.getMessage().toUpperCase().contains("UNIQUE")) {
                throw new ReservaDuplicadaException();
            }
            throw exception;
        }
    }

    public List<Reserva> listar() {
        return jdbcTemplate.query("SELECT id, fecha, hora, nombre, telefono, creado_en FROM reservas ORDER BY fecha, hora",
                (rs, rowNum) -> new Reserva(rs.getLong("id"), rs.getObject("fecha", LocalDate.class), rs.getObject("hora", LocalTime.class),
                    rs.getString("nombre"), rs.getString("telefono"), rs.getObject("creado_en", LocalDateTime.class)));
    }
}