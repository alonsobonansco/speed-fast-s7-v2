package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.view.VentanaListaPedidos;

import java.util.List;

public class ControladorListaPedidos {

    private final VentanaListaPedidos ventanaListaPedidos;
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private List<Pedido> listaPedidos;

    public ControladorListaPedidos(VentanaListaPedidos ventanaListaPedidos) {
        this.ventanaListaPedidos = ventanaListaPedidos;

        inicializarListeners();
        obtenerDatosDesdeBD();
        cargarDatosEnTabla();
    }

    private void inicializarListeners() {
        ventanaListaPedidos.addVolverAtrasListener(e -> ventanaListaPedidos.cerrarVentana());
    }

    private void cargarDatosEnTabla() {
        this.ventanaListaPedidos.actualizarTabla(this.listaPedidos);
    }

    private void obtenerDatosDesdeBD() {
        try {
            this.listaPedidos = pedidoDAO.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            ventanaListaPedidos.mostrarMensajeError("Error al obtener los datos de la base de datos: " + e.getMessage());
        }
    }
}
