package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.config.ConexionBD;
import cl.duoc.speedfast.model.entity.Entrega;

import java.sql.*;

public class EntregaDAO {

    public void guardar(Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entrega.getIdPedido());
            pstmt.setInt(2, entrega.getIdRepartidor());
            pstmt.setDate(3, Date.valueOf(entrega.getFecha()));
            pstmt.setTime(4, Time.valueOf(entrega.getHora()));

            pstmt.executeUpdate();
        }
    }
}
