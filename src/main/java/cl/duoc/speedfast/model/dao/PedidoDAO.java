package cl.duoc.speedfast.model.dao;

import cl.duoc.speedfast.database.ConexionBD;
import cl.duoc.speedfast.model.entity.EstadoPedido;
import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.model.entity.TipoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String textoTipo = rs.getString("tipo");
                String textoEstado = rs.getString("estado");

                TipoPedido tipoPedido = TipoPedido.valueOf(textoTipo.toUpperCase());
                EstadoPedido estadoPedido = EstadoPedido.valueOf(textoEstado.toUpperCase());

                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        tipoPedido);

                pedido.setEstadoPedido(estadoPedido);
                listaPedidos.add(pedido);
            }
        }

        return listaPedidos;
    }

    public List<Pedido> listarPendientes() throws SQLException {
        List<Pedido> listaPendientes = new ArrayList<>();

        String sql = "SELECT * FROM pedido WHERE estado = 'PENDIENTE'";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String textoTipo = rs.getString("tipo");
                String textoEstado = rs.getString("estado");

                TipoPedido tipoPedido = TipoPedido.valueOf(textoTipo.toUpperCase());
                EstadoPedido estadoPedido = EstadoPedido.valueOf(textoEstado.toUpperCase());

                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        tipoPedido);

                pedido.setEstadoPedido(estadoPedido);
                listaPendientes.add(pedido);
            }
        }

        return listaPendientes;
    }

    public void actualizarEstado(int idPedido, EstadoPedido nuevoEstado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado.name());
            pstmt.setInt(2, idPedido);

            pstmt.executeUpdate();
        }
    }
}
