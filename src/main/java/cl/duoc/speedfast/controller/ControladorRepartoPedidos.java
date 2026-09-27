package cl.duoc.speedfast.controller;

import cl.duoc.speedfast.event.LogListener;
import cl.duoc.speedfast.model.dao.EntregaDAO;
import cl.duoc.speedfast.model.dao.PedidoDAO;
import cl.duoc.speedfast.model.dao.RepartidorDAO;
import cl.duoc.speedfast.model.entity.Entrega;
import cl.duoc.speedfast.model.entity.EstadoPedido;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.model.entity.Repartidor;
import cl.duoc.speedfast.view.VentanaPrincipal;

import java.sql.SQLException;
import java.util.List;

public class ControladorRepartoPedidos {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final VentanaPrincipal ventanaPrincipal;
    private List<Pedido> listaPedidosEnSimulacion;
    private LogListener logListener;
    private int repartidoresActivos = 0;

    public ControladorRepartoPedidos(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    public synchronized void registrarEntregaEnBD(Pedido pedido) {
        try {
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), EstadoPedido.ENTREGADO);

        } catch (SQLException ex) {
            escribirMensaje("[ERROR] Error al registrar la entrega en la base de datos: " + ex.getMessage());
        }
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    public void iniciarSimulacionReparto(List<Pedido> listaPedidos) {
        if (listaPedidos == null || listaPedidos.isEmpty()) {
            escribirMensaje("[AVISO] No hay pedidos registrados en el sistema para despachar.");
            habilitarInicioSimulacion();
            return;
        }

        try {
            List<Repartidor> listaRepartidores = repartidorDAO.listarTodos();

            if (listaRepartidores.isEmpty()) {
                escribirMensaje("[AVISO] No hay repartidores registrados en el sistema.");
                habilitarInicioSimulacion();
                return;
            }

            if (listaPedidos.stream().noneMatch(p -> p.getEstadoPedido() == EstadoPedido.EN_REPARTO)) {
                escribirMensaje("[AVISO] No hay pedidos asignados para entregar.");
                habilitarInicioSimulacion();
                return;
            }

            this.listaPedidosEnSimulacion = listaPedidos;

            escribirMensaje("\n --- INICIANDO REPARTO CONCURRENTE DESDE BASE DE DATOS --- ");

            repartidoresActivos = listaRepartidores.size();

            for (Repartidor r : listaRepartidores) {
                r.setControladorPedidos(this);

                new Thread(r).start();
            }

        } catch (SQLException ex) {
            escribirMensaje("[ERROR] Error al listar los repartidores: " + ex.getMessage());
            habilitarInicioSimulacion();
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
            escribirMensaje("[ERROR] Error al retirar el pedido: " + ex.getMessage());
        }

        return null;
    }

    public synchronized void finalizarSimulacion() {
        this.repartidoresActivos--;
        if (repartidoresActivos == 0) {
            escribirMensaje("\n[AVISO] Todos los pedidos han sido procesados.");
            habilitarInicioSimulacion();
        }
    }

    private void habilitarInicioSimulacion() {
        if (ventanaPrincipal != null) {
            ventanaPrincipal.setEstadoBotonSimulacion(true);
        }
    }

    public synchronized void escribirMensaje(String mensaje) {
        if (logListener != null) {
            logListener.registrarMensaje(mensaje);
        }
    }
}
