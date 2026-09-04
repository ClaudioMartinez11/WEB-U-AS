package com.citas.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CrearReservaRequest(
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotBlank(message = "La hora es obligatoria")
        @Pattern(regexp = "(?:[01]\\d|2[0-3]):[0-5]\\d", message = "La hora debe tener formato HH:mm") String hora,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        @Pattern(regexp = "[\\p{L} .'-]+", message = "El nombre contiene caracteres no válidos") String nombre,
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "[0-9 +()\\-]{7,25}", message = "El teléfono no es válido") String telefono) {}