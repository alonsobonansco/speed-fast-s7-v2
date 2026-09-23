package cl.duoc.speedfast.model;

public class PedidoEncomienda extends Pedido {

    private static final double CAPACIDAD_MAXIMA_KG = 40.0;
    private final double pesoEncomienda;

    public PedidoEncomienda(int idPedido, String direccionEntrega, double pesoEncomienda) {
        super(TipoPedido.ENCOMIENDA, idPedido, direccionEntrega);

        if (pesoEncomienda <= 0) {
            throw new IllegalArgumentException("El peso de la encomienda debe ser válido.");
        }
        this.pesoEncomienda = pesoEncomienda;
    }

    public static double getCapacidadMaximaKg() {
        return CAPACIDAD_MAXIMA_KG;
    }

    @Override
    public boolean validarPedido() {
        if (pesoEncomienda > CAPACIDAD_MAXIMA_KG) {
            this.cancelar();
            return false;
        }

        return true;
    }

    @Override
    public String getDetalleEspecifico() {
        return pesoEncomienda + " kg";
    }
}
