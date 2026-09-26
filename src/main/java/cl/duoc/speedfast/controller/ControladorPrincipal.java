package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.view.*;

import java.sql.SQLException;
import java.util.List;

public class ControladorPrincipal {

    private final VentanaPrincipal ventanaPrincipal;
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private VentanaRegistroPedido ventanaRegistroPedido = null;
    private VentanaListaPedidos ventanaListaPedidos = null;
    private VentanaListaRepartidores ventanaListaRepartidores = null;
    private VentanaRegistroRepartidor ventanaRegistroRepartidor = null;

    public ControladorPrincipal(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        inicializarListeners();
    }

    private void inicializarListeners() {
        ventanaPrincipal.addRegistrarPedidoMenuListener(e -> ejecutarRegistroPedido());
        ventanaPrincipal.addListarPedidosMenuListener(e -> ejecutarListarPedidos());
        ventanaPrincipal.addRegistrarRepartidorMenuListener(e -> ejecutarRegistroRepartidor());
        ventanaPrincipal.addListarRepartidoresMenuListener(e -> ejecutarListarRepartidores());
    }

    private void ejecutarRegistroPedido() {
        ventanaPrincipal.clearLog();
        if (ventanaRegistroPedido == null || !ventanaRegistroPedido.isDisplayable()) {
            ventanaRegistroPedido = new VentanaRegistroPedido();

            new ControladorRegistroPedido(ventanaRegistroPedido);

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

        new ControladorListaPedidos(ventanaListaPedidos);

        ventanaListaPedidos.setVisible(true);
        ventanaListaPedidos.toFront();
        ventanaListaPedidos.requestFocus();
    }

    private void ejecutarRegistroRepartidor() {
        if (ventanaRegistroRepartidor == null || !ventanaRegistroRepartidor.isDisplayable()) {
            ventanaRegistroRepartidor = new VentanaRegistroRepartidor();
        }

        new ControladorRegistroRepartidor(ventanaRegistroRepartidor);

        ventanaRegistroRepartidor.setVisible(true);
        ventanaRegistroRepartidor.toFront();
        ventanaRegistroRepartidor.requestFocus();
    }

    private void ejecutarListarRepartidores() {
        if (ventanaListaRepartidores == null || !ventanaListaRepartidores.isDisplayable()) {
            ventanaListaRepartidores = new VentanaListaRepartidores();
        }

        new ControladorListaRepartidores(ventanaListaRepartidores);

        ventanaListaRepartidores.setVisible(true);
        ventanaListaRepartidores.toFront();
        ventanaListaRepartidores.requestFocus();
    }

    private void ejecutarIniciarEntregas() {
        ventanaPrincipal.clearLog();
        ControladorPedidos controladorPedidos = new ControladorPedidos();
        controladorPedidos.setLogListener(ventanaPrincipal::appendLog);

        try {
            List<Pedido> pedidosBD = pedidoDAO.listarTodos();
            controladorPedidos.iniciarSimulacionReparto(pedidosBD);

        } catch (SQLException ex) {
            ex.printStackTrace();
            ventanaPrincipal.appendLog("No se pudo iniciar la simulación. Error de lectura en MySQL.");
        }
    }
}
