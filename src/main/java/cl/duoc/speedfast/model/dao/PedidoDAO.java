package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.config.ConexionBD;
import cl.duoc.speedfast.model.entity.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PedidoDAO {

    public void guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getDireccionEntrega());
            pstmt.setString(2, pedido.getTipoPedido().name());
            pstmt.setString(3, pedido.getEstadoPedido().name());

            pstmt.executeUpdate();
        }
    }
}
