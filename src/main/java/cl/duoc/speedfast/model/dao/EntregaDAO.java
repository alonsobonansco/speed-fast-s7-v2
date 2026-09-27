package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.database.ConexionBD;
import cl.duoc.speedfast.model.entity.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
