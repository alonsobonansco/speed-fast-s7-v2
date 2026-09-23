package cl.duoc.speedfast.model;

public class PedidoComida extends Pedido {

    private final boolean comidaEnBuenEstado;

    public PedidoComida(int idPedido, String direccionEntrega, boolean comidaEnBuenEstado) {
        super(TipoPedido.COMIDA, idPedido, direccionEntrega);
        this.comidaEnBuenEstado = comidaEnBuenEstado;
    }

    @Override
    public boolean validarPedido() {
        if (!comidaEnBuenEstado) {
            this.cancelar();
            return false;
        }
        return true;
    }

    @Override
    public String getDetalleEspecifico() {
        return comidaEnBuenEstado ? "Buen estado" : "Mal estado";
    }
}
