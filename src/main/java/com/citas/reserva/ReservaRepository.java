package com.citas.reserva;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservaRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservaRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public Reserva guardar(CrearReservaRequest request, LocalTime hora, LocalDateTime creadoEn) {
        try {
            jdbcTemplate.update("INSERT INTO reservas(fecha, hora, nombre, telefono, creado_en) VALUES (?, ?, ?, ?, ?)",
                    request.fecha().toString(), hora.toString(), request.nombre().trim(), request.telefono().trim(), creadoEn.toString());
            return jdbcTemplate.queryForObject("SELECT id, fecha, hora, nombre, telefono, creado_en FROM reservas WHERE fecha = ? AND hora = ?",
                    (rs, rowNum) -> new Reserva(rs.getLong("id"), LocalDate.parse(rs.getString("fecha")), LocalTime.parse(rs.getString("hora")),
                            rs.getString("nombre"), rs.getString("telefono"), LocalDateTime.parse(rs.getString("creado_en"))),
                    request.fecha().toString(), hora.toString());
        } catch (DataAccessException exception) {
            if (exception.getMessage() != null && exception.getMessage().toUpperCase().contains("UNIQUE")) {
                throw new ReservaDuplicadaException();
            }
            throw exception;
        }
    }

    public List<Reserva> listar() {
        return jdbcTemplate.query("SELECT id, fecha, hora, nombre, telefono, creado_en FROM reservas ORDER BY fecha, hora",
                (rs, rowNum) -> new Reserva(rs.getLong("id"), LocalDate.parse(rs.getString("fecha")), LocalTime.parse(rs.getString("hora")),
                        rs.getString("nombre"), rs.getString("telefono"), LocalDateTime.parse(rs.getString("creado_en"))));
    }
}