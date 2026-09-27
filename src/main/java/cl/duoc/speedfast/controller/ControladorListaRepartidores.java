package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.service.Repartidor;
import cl.duoc.speedfast.view.VentanaListaRepartidores;

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

    public void inicializarListeners() {
        ventanaListaRepartidores.addVolverAtrasListener(e -> ventanaListaRepartidores.cerrarVentana());
    }

    public void cargarDatosEnTabla() {
        this.ventanaListaRepartidores.actualizarTabla(this.listaRepartidores);
    }

    public void obtenerDatosDesdeBD() {
        try {
            this.listaRepartidores = repartidorDAO.listarTodos();
        } catch (Exception ex) {
            ventanaListaRepartidores.mostrarMensajeError("Error al obtener los datos: " + ex.getMessage());
        }
    }
}
