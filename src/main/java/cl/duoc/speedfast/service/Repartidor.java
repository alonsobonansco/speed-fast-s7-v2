package cl.duoc.speedfast.service;

import cl.duoc.speedfast.controller.ControladorPedidos;
import cl.duoc.speedfast.model.Pedido;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static cl.duoc.speedfast.model.EstadoPedido.ENTREGADO;

public class Repartidor implements Runnable {

    private final String nombreRepartidor;
    private final ControladorPedidos controladorPedidos;

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

                TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1500, 1500));

                controladorPedidos.escribirMensaje("[RUTA] Pedido #" + pedido.getIdPedido() + " se encuentra en reparto");

                TimeUnit.MILLISECONDS.sleep(calcularTiempoAleatorio(1000, 1000));

                pedido.setEstadoPedido(ENTREGADO);

                controladorPedidos.escribirMensaje("[ENTREGA] Pedido #" + pedido.getIdPedido() + " ha sido entregado por [" + nombreRepartidor + "]");

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
}
