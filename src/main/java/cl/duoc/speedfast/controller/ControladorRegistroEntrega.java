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
            ex.getStackTrace();
            ventanaRegistroEntrega.mostrarMensajeError("Error al cargar los datos: " + ex.getMessage());
        }
    }

    public void inicializarListeners() {
        ventanaRegistroEntrega.addGuardarListener(e -> procesarAsignacion());
        ventanaRegistroEntrega.addVolverAtrasListener(e -> ventanaRegistroEntrega.cerrarVentana());
    }

    public void procesarAsignacion() {
        try {
            Pedido pedidoSelec = (Pedido) ventanaRegistroEntrega.getComboPedidos().getSelectedItem();
            Repartidor repartidorSelec = (Repartidor) ventanaRegistroEntrega.getComboRepartidores().getSelectedItem();

            if (pedidoSelec == null || repartidorSelec == null) {
                ventanaRegistroEntrega.mostrarMensajeError("Debe seleccionar un pedido y un repartidor obligatoriamente.");
                return;
            }

            Entrega nuevaEntrega = new Entrega(
                    pedidoSelec.getIdPedido(),
                    repartidorSelec.getIdRepartidor(),
                    LocalDate.now(),
                    LocalTime.now()
            );


            entregaDAO.guardar(nuevaEntrega);

            pedidoDAO.actualizarEstado(pedidoSelec.getIdPedido(), EstadoPedido.EN_REPARTO);

            ventanaRegistroEntrega.getComboPedidos().removeItem(pedidoSelec);

            ventanaRegistroEntrega.mostrarMensajeConfirmacion("Entrega registrada con éxito en la Base de Datos");
            ventanaRegistroEntrega.cerrarVentana();

        } catch (Exception ex) {
            ventanaRegistroEntrega.mostrarMensajeError("Error al asignar la entrega: " + ex.getMessage());
        }

    }

}
