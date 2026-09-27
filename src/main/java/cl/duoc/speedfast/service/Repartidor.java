package cl.duoc.speedfast.service;

import cl.duoc.speedfast.controller.ControladorPedidos;
import cl.duoc.speedfast.model.entity.Pedido;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static cl.duoc.speedfast.model.entity.EstadoPedido.ENTREGADO;
import static cl.duoc.speedfast.model.entity.EstadoPedido.EN_REPARTO;

public class Repartidor implements Runnable {

    private final String nombreRepartidor;
    private int idRepartidor;
    private ControladorPedidos controladorPedidos;

    public Repartidor(String nombreRepartidor, ControladorPedidos controladorPedidos) {
        if (nombreRepartidor == null || nombreRepartidor.isEmpty()) {
            throw new IllegalArgumentException("El nombre del repartidor no puede ser nulo o vacío");
        }
        if (controladorPedidos == null) {
            throw new IllegalArgumentException("El controlador de pedidos no puede ser nulo");
        }
        this.nombreRepartidor = nombreRepartidor;
        this.controladorPedidos = controladorPedidos;
    }

    public Repartidor(int idRepartidor, String nombreRepartidor) {
        this.idRepartidor = idRepartidor;
        this.nombreRepartidor = nombreRepartidor;
    }

    public void setControladorPedidos(ControladorPedidos controladorPedidos) {
        if (controladorPedidos == null) {
            throw new IllegalArgumentException("El controlador de pedidos no puede ser nulo");
        }
        this.controladorPedidos = controladorPedidos;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = controladorPedidos.retirarPedido();

            if (pedido == null) {
                break;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1000, 1000));
                controladorPedidos.escribirMensaje("[CARGA] Repartidor [" + nombreRepartidor + "] retirando pedido #" + pedido.getIdPedido());

                pedido.setEstadoPedido(EN_REPARTO);


                TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1500, 1500));

                controladorPedidos.escribirMensaje("[RUTA] Pedido #" + pedido.getIdPedido() + " se encuentra en reparto");

                TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1000, 1000));

                pedido.setEstadoPedido(ENTREGADO);

                controladorPedidos.escribirMensaje("[ENTREGA] Pedido #" + pedido.getIdPedido() + " ha sido entregado por [" + nombreRepartidor + "]");

                controladorPedidos.registrarEntregaEnBD(pedido);

            } catch (InterruptedException e) {
                controladorPedidos.escribirMensaje("Entrega interrumpida");

                Thread.currentThread().interrupt();
                break;
            }
        }

        controladorPedidos.finalizarSimulacion();
    }

    private int calcularTiempoAleatorio(int baseMilisegundos, int rangoAleatorio) {
        return baseMilisegundos + ThreadLocalRandom.current().nextInt(rangoAleatorio);
    }

    @Override
    public String toString() {
        return this.nombreRepartidor;
    }
}
