package cl.duoc.speedfast.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrega {

    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;
    private int idEntrega;

    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID del pedido debe ser válido.");
        }
        if (idRepartidor <= 0) {
            throw new IllegalArgumentException("El ID del repartidor debe ser válido.");
        }
        if  (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (hora == null) {
            throw new IllegalArgumentException("La hora no puede ser nula.");
        }

        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        if (idEntrega <= 0) {
            throw new IllegalArgumentException("El ID de la entrega debe ser válido.");
        }
        this.idEntrega = idEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}
