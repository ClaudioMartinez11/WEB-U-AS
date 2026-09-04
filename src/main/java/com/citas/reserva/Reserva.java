package com.citas.reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record Reserva(long id, LocalDate fecha, LocalTime hora, String nombre, String telefono, LocalDateTime creadoEn) {}