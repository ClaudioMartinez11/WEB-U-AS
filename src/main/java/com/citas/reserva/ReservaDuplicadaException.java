package com.citas.reserva;

public class ReservaDuplicadaException extends RuntimeException {
    public ReservaDuplicadaException() { super("La fecha y el horario seleccionados ya están reservados"); }
}