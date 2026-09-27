package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.event.LogListener;
import cl.duoc.speedfast.model.dao.EntregaDAO;
import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.Entrega;
import cl.duoc.speedfast.model.entity.EstadoPedido;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.service.Repartidor;

import java.sql.SQLException;
import java.util.List;

public class ControladorPedidos {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private List<Pedido> listaPedidosEnSimulacion;
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

        this.listaPedidosEnSimulacion = listaPedidos;

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

    public synchronized Pedido retirarPedidoPorRepartidor(int idRepartidor) {
        try {
            List<Entrega> listaEntregas = entregaDAO.listarTodos();

            for (Pedido p : listaPedidosEnSimulacion) {
                if (p.getEstadoPedido() == EstadoPedido.EN_REPARTO) {
                    for (Entrega e : listaEntregas) {
                        if (e.getIdPedido() == p.getIdPedido() && e.getIdRepartidor() == idRepartidor) {
                            p.setEstadoPedido(EstadoPedido.ENTREGADO);
                            return p;
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            escribirMensaje("[ERROR] Error al retirar el pedido: " + ex.getMessage());
        }

        return null;
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
