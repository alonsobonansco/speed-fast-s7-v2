package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.database.ConexionBD;
import cl.duoc.speedfast.model.entity.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public void guardar(Entrega entrega) throws SQLException {
        String sqlEntrega = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        String sqlPedido = "UPDATE pedido SET estado = 'EN_REPARTO' WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtEntrega = conn.prepareStatement(sqlEntrega);
                 PreparedStatement pstmtPedido = conn.prepareStatement(sqlPedido)) {

                pstmtEntrega.setInt(1, entrega.getIdPedido());
                pstmtEntrega.setInt(2, entrega.getIdRepartidor());
                pstmtEntrega.setDate(3, Date.valueOf(entrega.getFecha()));
                pstmtEntrega.setTime(4, Time.valueOf(entrega.getHora()));

                pstmtEntrega.executeUpdate();

                pstmtPedido.setInt(1, entrega.getIdPedido());
                pstmtPedido.executeUpdate();

                conn.commit();
            }

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }

            throw ex;

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<Entrega> listarTodos() throws SQLException {
        List<Entrega> listaEntregas = new ArrayList<>();
        String sql = "SELECT * FROM entrega";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Entrega entrega = new Entrega(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );
                entrega.setIdEntrega(rs.getInt("id"));
                listaEntregas.add(entrega);
            }
        }

        return listaEntregas;
    }
}
