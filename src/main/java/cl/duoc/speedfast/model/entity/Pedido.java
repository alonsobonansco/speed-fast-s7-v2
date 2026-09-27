package cl.duoc.speedfast.model.entity;

public class Pedido {

    private final TipoPedido tipoPedido;
    private final int idPedido;
    private String direccionEntrega;
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    public Pedido(int idPedido, String direccionEntrega, TipoPedido tipoPedido) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("El ID del pedido debe ser válido.");
        }

        this.tipoPedido = validarTipoPedido(tipoPedido);
        this.idPedido = idPedido;
        setDireccionEntrega(direccionEntrega);
    }

    public Pedido(TipoPedido tipoPedido, String direccionEntrega) {
        this.tipoPedido = validarTipoPedido(tipoPedido);
        this.idPedido = 0;
        setDireccionEntrega(direccionEntrega);
    }

    private TipoPedido validarTipoPedido(TipoPedido tipoPedido) {
        if (tipoPedido == null) {
            throw new IllegalArgumentException("El tipo de pedido no puede ser nulo.");
        }

        return tipoPedido;
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

        this.direccionEntrega = direccionEntrega.trim();
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        if (estadoPedido == null) {
            throw new IllegalArgumentException("El estado del pedido no puede ser nulo.");
        }

        this.estadoPedido = estadoPedido;
    }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + direccionEntrega;
    }
}
