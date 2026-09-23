package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.view.VentanaListaPedidos;
import cl.duoc.speedfast.view.VentanaPrincipal;
import cl.duoc.speedfast.view.VentanaRegistroPedido;

import java.util.ArrayList;
import java.util.List;

public class ControladorPrincipal {

    private final VentanaPrincipal ventanaPrincipal;
    private final List<Pedido> listaPedidos;
    private VentanaRegistroPedido ventanaRegistroPedido = null;
    private VentanaListaPedidos ventanaListaPedidos = null;

    public ControladorPrincipal(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.listaPedidos = new ArrayList<>();

        inicializarListeners();
    }

    private void inicializarListeners() {
        ventanaPrincipal.addRegistrarPedidoListener(e -> ejecutarRegistroPedido());
        ventanaPrincipal.addListarPedidosListener(e -> ejecutarListarPedidos());
        ventanaPrincipal.addIniciarEntregasListener(e -> ejecutarIniciarEntregas());
    }

    private void ejecutarRegistroPedido() {
        ventanaPrincipal.clearLog();
        if (ventanaRegistroPedido == null || !ventanaRegistroPedido.isDisplayable()) {
            ventanaRegistroPedido = new VentanaRegistroPedido();

            new ControladorRegistro(ventanaRegistroPedido, listaPedidos);

            ventanaRegistroPedido.setVisible(true);
        } else {
            ventanaRegistroPedido.toFront();
            ventanaRegistroPedido.requestFocus();
        }
    }

    private void ejecutarListarPedidos() {
        if (ventanaListaPedidos == null || !ventanaListaPedidos.isDisplayable()) {
            ventanaListaPedidos = new VentanaListaPedidos();
        }

        new ControladorLista(ventanaListaPedidos, listaPedidos);

        ventanaListaPedidos.setVisible(true);
        ventanaListaPedidos.toFront();
        ventanaListaPedidos.requestFocus();
    }

    private void ejecutarIniciarEntregas() {
        ventanaPrincipal.clearLog();
        ControladorPedidos controladorPedidos = new ControladorPedidos();
        controladorPedidos.setLogListener(ventanaPrincipal::appendLog);
        controladorPedidos.iniciarSimulacionReparto(listaPedidos);
    }
}
