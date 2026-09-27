package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.EntregaDAO;
import cl.duoc.speedfast.model.entity.Entrega;
import cl.duoc.speedfast.view.VentanaListaEntregas;

import java.sql.SQLException;
import java.util.List;

public class ControladorListaEntregas {

    private final VentanaListaEntregas ventanaListaEntregas;
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private List<Entrega> listaEntregas;

    public ControladorListaEntregas(VentanaListaEntregas ventanaListaEntregas) {
        this.ventanaListaEntregas = ventanaListaEntregas;

        inicializarListeners();
        obtenerDatosDesdeBD();
        cargarDatosEnTabla();
    }

    private void inicializarListeners() {
        ventanaListaEntregas.addVolverAtrasListener(e -> ventanaListaEntregas.cerrarVentana());
    }

    private void obtenerDatosDesdeBD() {
        try {
            listaEntregas = entregaDAO.listarTodos();
        } catch (SQLException ex) {
            ventanaListaEntregas.mostrarMensajeError("Error al obtener los datos de la base de datos: " + ex.getMessage());
        }
    }

    private void cargarDatosEnTabla() {
        ventanaListaEntregas.actualizarTabla(listaEntregas);
    }
}
