package cl.duoc.speedfast.model.entity;

public class Pedido implements Cancelable {

    private final TipoPedido tipoPedido;
    private final int idPedido;
    private String direccionEntrega;
    private boolean pedidoActivo = true;
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    public Pedido(int idPedido, String direccionEntrega, TipoPedido tipoPedido) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID del pedido debe ser válido.");
        }

        this.tipoPedido = tipoPedido;
        this.idPedido = idPedido;
        setDireccionEntrega(direccionEntrega);
    }

    public Pedido(TipoPedido tipoPedido, String direccionEntrega) {
        this.tipoPedido = tipoPedido;
        this.idPedido = 0; // Se asignará un ID más adelante
        setDireccionEntrega(direccionEntrega);

    }

    @Override
    public void cancelar() {
        if (!pedidoActivo) {
            return;
        }

        pedidoActivo = false;
        //estadoPedido = EstadoPedido.CANCELADO;
    }

    public TipoPedido getTipoPedido() {
        return tipoPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La dirección de entrega debe ser válida.");
        }
        this.direccionEntrega = direccionEntrega;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    @Override
    public String toString () {
        return "Pedido #" + idPedido + " - " + direccionEntrega;
    }
}
