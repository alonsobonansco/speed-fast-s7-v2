package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.Repartidor;
import cl.duoc.speedfast.view.VentanaListaRepartidores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorListaRepartidores {

    private final VentanaListaRepartidores ventanaListaRepartidores;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private List<Repartidor> listaRepartidores;

    public ControladorListaRepartidores(VentanaListaRepartidores ventanaListaRepartidores) {
        this.ventanaListaRepartidores = ventanaListaRepartidores;

        inicializarListeners();
        obtenerDatosDesdeBD();
        cargarDatosEnTabla();
    }

    private void inicializarListeners() {
        ventanaListaRepartidores.addVolverAtrasListener(e -> ventanaListaRepartidores.cerrarVentana());
    }

    private void cargarDatosEnTabla() {
        this.ventanaListaRepartidores.actualizarTabla(this.listaRepartidores);
    }

    private void obtenerDatosDesdeBD() {
        try {
            this.listaRepartidores = repartidorDAO.listarTodos();

        } catch (SQLException ex) {
            ventanaListaRepartidores.mostrarMensajeError("Error al obtener los datos: " + ex.getMessage());
            listaRepartidores = new ArrayList<>();
        }
    }
}
