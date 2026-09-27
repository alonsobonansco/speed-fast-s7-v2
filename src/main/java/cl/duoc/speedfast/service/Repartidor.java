package cl.duoc.speedfast.service;

import cl.duoc.speedfast.controller.ControladorRepartoPedidos;
import cl.duoc.speedfast.model.entity.Pedido;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static cl.duoc.speedfast.model.entity.EstadoPedido.ENTREGADO;
import static cl.duoc.speedfast.model.entity.EstadoPedido.EN_REPARTO;

public class Repartidor implements Runnable {

    private final String nombreRepartidor;
    private int idRepartidor;
    private ControladorRepartoPedidos controladorRepartoPedidos;

    public Repartidor(String nombreRepartidor, ControladorRepartoPedidos controladorRepartoPedidos) {
        if (nombreRepartidor == null || nombreRepartidor.isEmpty()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede ser nulo o vacío");
        }
        if (controladorRepartoPedidos == null) {
            throw new IllegalArgumentException("El controlador de pedidos no puede ser nulo");
        }
        this.nombreRepartidor = nombreRepartidor;
        this.controladorRepartoPedidos = controladorRepartoPedidos;
    }

    public Repartidor(int idRepartidor, String nombreRepartidor) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
    }

    public void setControladorPedidos(ControladorRepartoPedidos controladorRepartoPedidos) {
        if (controladorRepartoPedidos == null) {
            throw new IllegalArgumentException("El controlador de pedidos no puede ser nulo");
        }
        this.controladorRepartoPedidos = controladorRepartoPedidos;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = controladorRepartoPedidos.retirarPedidoPorRepartidor(this.idRepartidor);

                if (pedido == null) break;

                try {
                    TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1000, 1000));
                    controladorRepartoPedidos.escribirMensaje("[CARGA] Repartidor [" + nombreRepartidor + "] retirando pedido #" + pedido.getIdPedido());

                    pedido.setEstadoPedido(EN_REPARTO);


                    TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1500, 1500));

                    controladorRepartoPedidos.escribirMensaje("[RUTA] Pedido #" + pedido.getIdPedido() + " se encuentra en reparto");

                    TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1000, 1000));

                    pedido.setEstadoPedido(ENTREGADO);

                    controladorRepartoPedidos.escribirMensaje("[ENTREGA] Pedido #" + pedido.getIdPedido() + " ha sido entregado por [" + nombreRepartidor + "]");

                    controladorRepartoPedidos.registrarEntregaEnBD(pedido);

                } catch (InterruptedException e) {
                    controladorRepartoPedidos.escribirMensaje("Entrega interrumpida");

                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (RuntimeException e) {
            controladorRepartoPedidos.escribirMensaje("[ERROR] Error en el hilo del repartidor [" + nombreRepartidor + "]: " + e.getMessage());

        } finally {
            controladorRepartoPedidos.finalizarSimulacion();
        }
    }

    private int calcularTiempoAleatorio(int baseMilisegundos, int rangoAleatorio) {
        return baseMilisegundos + ThreadLocalRandom.current().nextInt(rangoAleatorio);
    }

    @Override
    public String toString() {
        return this.nombreRepartidor;
    }
}
