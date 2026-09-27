package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.model.dao.EntregaDAO;
import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.Entrega;
import cl.duoc.speedfast.model.entity.EstadoPedido;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.service.Repartidor;
import cl.duoc.speedfast.view.VentanaRegistroEntrega;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ControladorRegistroEntrega {

    private final VentanaRegistroEntrega ventanaRegistroEntrega;
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public ControladorRegistroEntrega(VentanaRegistroEntrega ventanaRegistroEntrega) {
        this.ventanaRegistroEntrega = ventanaRegistroEntrega;
        inicializarListeners();
        cargarDatosEnComponentes();
    }

    public void cargarDatosEnComponentes() {
        try {
            ventanaRegistroEntrega.cargarPedidos(pedidoDAO.listarPendientes());
            ventanaRegistroEntrega.cargarRepartidores(repartidorDAO.listarTodos());
        } catch (SQLException ex) {
            ventanaRegistroEntrega.mostrarMensajeError("Error al cargar los datos: " + ex.getMessage());
        }
    }

    public void inicializarListeners() {
        ventanaRegistroEntrega.addGuardarListener(e -> procesarAsignacion());
        ventanaRegistroEntrega.addVolverAtrasListener(e -> ventanaRegistroEntrega.cerrarVentana());
    }

    public void procesarAsignacion() {
        Pedido pedidoSelec = (Pedido) ventanaRegistroEntrega.getComboPedidos().getSelectedItem();
        Repartidor repartidorSelec = (Repartidor) ventanaRegistroEntrega.getComboRepartidores().getSelectedItem();

        if (pedidoSelec == null || repartidorSelec == null) {
            ventanaRegistroEntrega.mostrarMensajeError("Debe seleccionar un pedido y un repartidor obligatoriamente.");
            return;
        }

        try {
            Entrega nuevaEntrega = new Entrega(
                    pedidoSelec.getIdPedido(),
                    repartidorSelec.getIdRepartidor(),
                    LocalDate.now(),
                    LocalTime.now()
            );

            entregaDAO.guardar(nuevaEntrega);

            ventanaRegistroEntrega.getComboPedidos().removeItem(pedidoSelec);

            ventanaRegistroEntrega.mostrarMensajeConfirmacion("Entrega registrada con éxito en la Base de Datos");

        } catch (IllegalArgumentException | DateTimeException e) {
            ventanaRegistroEntrega.mostrarMensajeError("Error al asignar la entrega: " + e.getMessage());

        } catch (SQLException ex) {
            ventanaRegistroEntrega.mostrarMensajeError("Error al guardar la entrega en la base de datos: " + ex.getMessage());
        }
    }
}
