package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.config.ConexionBD;
import cl.duoc.speedfast.service.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public void guardar(Repartidor repartidor) throws SQLException {

    }

    public List<Repartidor> listarTodos() throws SQLException {
        List<Repartidor> listaRepartidores = new ArrayList<>();
        String sql = "SELECT * FROM repartidor";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaRepartidores.add(
                        new Repartidor(
                                rs.getInt("id"),
                                rs.getString("nombre")));
            }
        }

        return listaRepartidores;
    }
}
