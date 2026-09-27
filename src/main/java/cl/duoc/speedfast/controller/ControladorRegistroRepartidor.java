package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.Repartidor;
import cl.duoc.speedfast.view.VentanaRegistroRepartidor;

import java.sql.SQLException;

public class ControladorRegistroRepartidor {

    private final VentanaRegistroRepartidor ventanaRegistroRepartidor;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public ControladorRegistroRepartidor(VentanaRegistroRepartidor ventanaRegistroRepartidor) {
        this.ventanaRegistroRepartidor = ventanaRegistroRepartidor;

        inicializarListeners();
    }

    private void inicializarListeners() {
        ventanaRegistroRepartidor.addVolverAtrasListener(e -> ventanaRegistroRepartidor.cerrarVentana());
        ventanaRegistroRepartidor.addGuardarListener(e -> procesarGuardado());
    }

    private void procesarGuardado() {
        try {
            String nombreRepartidor = ventanaRegistroRepartidor.getNombreRepartidor();

            if (nombreRepartidor.isBlank()) {
                ventanaRegistroRepartidor.mostrarMensajeError("El nombre del repartidor no puede estar vacío.");
                return;
            }

            Repartidor nuevoRepartidor = new Repartidor(nombreRepartidor);

            repartidorDAO.guardar(nuevoRepartidor);

            ventanaRegistroRepartidor.mostrarMensajeConfirmacion("Repartidor registrado correctamente.");
            ventanaRegistroRepartidor.limpiarFormulario();

        } catch (IllegalArgumentException e) {
            ventanaRegistroRepartidor.mostrarMensajeError(e.getMessage());
        } catch (SQLException ex) {
            ventanaRegistroRepartidor.mostrarMensajeError("Error al guardar el repartidor en la base de datos: " + ex.getMessage());
        }
    }
}
