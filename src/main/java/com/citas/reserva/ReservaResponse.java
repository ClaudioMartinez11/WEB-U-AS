package com.citas.reserva;

public record ReservaResponse(long id, String fecha, String hora, String nombre, String telefono) {
    public static ReservaResponse from(Reserva reserva) {
        return new ReservaResponse(reserva.id(), reserva.fecha().toString(), reserva.hora().toString(), reserva.nombre(), reserva.telefono());
    }
}