package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.service.Repartidor;
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
            String nombre = ventanaRegistroRepartidor.getNombreRepartidor();

            if (nombre.isBlank()) {
                ventanaRegistroRepartidor.mostrarMensajeError("El nombre del repartidor no puede estar vacío.");
                return;
            }

            Repartidor nuevoRepartidor = new Repartidor(0, nombre);

            repartidorDAO.guardar(nuevoRepartidor);

            ventanaRegistroRepartidor.mostrarMensajeConfirmacion("Repartidor registrado correctamente.");
            ventanaRegistroRepartidor.limpiarFormulario();

        } catch (IllegalArgumentException e) {
            ventanaRegistroRepartidor.mostrarMensajeError(e.getMessage());
        } catch (SQLException ex) {
            ex.printStackTrace();
            ventanaRegistroRepartidor.mostrarMensajeError("Error al guardar el repartidor en la base de datos: " + ex.getMessage());
        }

        String nombre = ventanaRegistroRepartidor.getNombreRepartidor();
        if (nombre == null || nombre.isEmpty()) {
            ventanaRegistroRepartidor.mostrarMensajeError("El nombre del repartidor no puede estar vacío.");
        }
    }
}
