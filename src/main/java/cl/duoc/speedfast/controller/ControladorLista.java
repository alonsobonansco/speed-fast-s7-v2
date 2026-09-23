package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.Pedido;
import cl.duoc.speedfast.view.VentanaListaPedidos;

import java.util.List;

public class ControladorLista {

    private final VentanaListaPedidos ventanaListaPedidos;
    private final List<Pedido> listaPedidos;

    public ControladorLista(VentanaListaPedidos ventanaListaPedidos, List<Pedido> listaPedidos) {
        this.ventanaListaPedidos = ventanaListaPedidos;
        this.listaPedidos = listaPedidos;

        inicializarListeners();
        cargarDatosEnTabla();
    }

    private void inicializarListeners() {
        ventanaListaPedidos.addVolverAtrasListener(e -> ventanaListaPedidos.cerrarVentana());
    }

    private void cargarDatosEnTabla() {
        this.ventanaListaPedidos.actualizarTabla(this.listaPedidos);
    }
}
