package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.database.ConexionBD;
import cl.duoc.speedfast.model.entity.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public void guardar(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, repartidor.getNombreRepartidor());

            pstmt.executeUpdate();
        }

    }

    public List<Repartidor> listarTodos() throws SQLException {
        List<Repartidor> listaRepartidores = new ArrayList<>();
        String sql = "SELECT * FROM repartidor";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaRepartidores.add(new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")));
            }
        }

        return listaRepartidores;
    }
}
