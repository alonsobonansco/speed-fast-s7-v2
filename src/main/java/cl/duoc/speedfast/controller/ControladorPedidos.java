package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.event.LogListener;
import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.EstadoPedido;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.service.Repartidor;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControladorPedidos {

    private final BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private LogListener logListener;
    private int repartidoresActivos = 0;

    public synchronized void registrarEntregaEnBD(Pedido pedido) {
        try {
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);

        } catch (SQLException ex) {
            ex.printStackTrace();
            escribirMensaje("[ERROR] Error al registrar la entrega en la base de datos: " + ex.getMessage());
        }
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }


    public void iniciarSimulacionReparto(List<Pedido> listaPedidos) {
        if (listaPedidos == null || listaPedidos.isEmpty()) {
            escribirMensaje("[AVISO] No hay pedidos registrados en el sistema para despachar.");
            return;
        }

        pedidosPendientes.clear();

       for (Pedido p : listaPedidos) {
           if (p.getEstadoPedido() == EstadoPedido.PENDIENTE || p.getEstadoPedido() == EstadoPedido.EN_REPARTO) {
               agregarPedido(p);
           }
       }

        if (pedidosPendientes.isEmpty()) {
            escribirMensaje("[AVISO] No quedan pedidos pendientes por entregar.");
            return;
        }

        escribirMensaje("\n --- INICIANDO REPARTO CONCURRENTE DESDE BASE DE DATOS --- ");

        try {
            List<Repartidor> listaRepartidores = repartidorDAO.listarTodos();

            if (listaRepartidores.isEmpty()) {
                escribirMensaje("[AVISO] No hay repartidores registrados en el sistema.");
                return;
            }

            repartidoresActivos = listaRepartidores.size();

            for (Repartidor r : listaRepartidores) {
                r.setControladorPedidos(this);

                Thread hiloRepartidor = new Thread(r);
                hiloRepartidor.start();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            escribirMensaje("[ERROR] Error al listar los repartidores: " + ex.getMessage());
        }
    }

    private void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(Objects.requireNonNull(
                pedido, "El pedido no puede ser nulo."
        ));
    }

    public Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }

    public synchronized void finalizarSimulacion() {
        this.repartidoresActivos--;
        if (repartidoresActivos == 0) {
            escribirMensaje("\n[AVISO] Todos los pedidos han sido procesados.");
        }
    }

    public synchronized void escribirMensaje(String mensaje) {
        if (logListener != null) {
            logListener.registrarMensaje(mensaje);
        }
    }
}
