package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.event.LogListener;
import cl.duoc.speedfast.model.dao.PedidoDAO;
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
    private LogListener logListener;
    private int repartidoresActivos = 0;

    public synchronized void registrarEntregaEnBD(Pedido pedido, String nombreRepartidor) {
        try {
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);
            escribirMensaje("[ENTREGADO] Pedido #" + pedido.getIdPedido() + " entregado por repartidor [" + nombreRepartidor + "]");

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

        /*for (Pedido pedido : listaPedidos) {
            if (pedido.getEstadoPedido() == EstadoPedido.PENDIENTE) {

                if (pedido.validarPedido()) {
                    agregarPedido(pedido);
                } else {
                    String motivo = switch (pedido.getTipoPedido()) {
                        case COMIDA -> "Comida en mal estado.";
                        case ENCOMIENDA -> "El peso excede el límite máximo de " +
                                PedidoEncomienda.getCapacidadMaximaKg() + " kg.";
                        case EXPRESS -> "La distancia excede el límite máximo de " +
                                PedidoExpress.getDistanciaMaximaKm() + " km.";
                        default -> "Tipo de pedido desconocido.";
                    };

                    escribirMensaje("[RECHAZADO] Pedido #" + pedido.getIdPedido() + ". Motivo: " + motivo);
                }
            }
        }*/

        if (pedidosPendientes.isEmpty()) {
            escribirMensaje("[AVISO] No quedan pedidos pendientes por entregar.");
            return;
        }

        escribirMensaje("\n --- INICIANDO REPARTO CONCURRENTE --- ");

        String[] nombresRepartidores = {"Juan", "María", "Carlos"};
        this.repartidoresActivos = nombresRepartidores.length;

        for (String nombre : nombresRepartidores) {
            Thread hiloRepartidor = new Thread(new Repartidor(nombre, this));
            hiloRepartidor.start();
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
