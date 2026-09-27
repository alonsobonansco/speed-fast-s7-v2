package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.model.entity.TipoPedido;
import cl.duoc.speedfast.view.VentanaRegistroPedido;

import java.sql.SQLException;

public class ControladorRegistroPedido {

    private final VentanaRegistroPedido ventanaRegistroPedido;
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public ControladorRegistroPedido(VentanaRegistroPedido ventanaRegistroPedido) {
        this.ventanaRegistroPedido = ventanaRegistroPedido;

        inicializarListeners();
    }

    private void inicializarListeners() {
        ventanaRegistroPedido.addVolverAtrasListener(e -> ventanaRegistroPedido.cerrarVentana());
        ventanaRegistroPedido.addGuardarListener(e -> procesarGuardado());
    }

    private void procesarGuardado() {
        try {
            String direccionEntrega = ventanaRegistroPedido.getDireccionEntrega();
            String tipoPedidoStr = ventanaRegistroPedido.getTipoPedido();

            if (direccionEntrega.isBlank()) {
                ventanaRegistroPedido.mostrarMensajeError("La dirección de entrega no puede estar vacía.");
                return;
            }

            TipoPedido tipoPedidoEnum = TipoPedido.valueOf(tipoPedidoStr.toUpperCase());

            Pedido nuevoPedido = new Pedido(tipoPedidoEnum, direccionEntrega);

            pedidoDAO.guardar(nuevoPedido);

            ventanaRegistroPedido.mostrarMensajeConfirmacion("Pedido registrado exitosamente.");
            ventanaRegistroPedido.limpiarFormulario();

        } catch (IllegalArgumentException e) {
            ventanaRegistroPedido.mostrarMensajeError(e.getMessage());
        } catch (SQLException ex) {
            ventanaRegistroPedido.mostrarMensajeError("Error al guardar el pedido en la base de datos: " + ex.getMessage());
        }
    }
}
