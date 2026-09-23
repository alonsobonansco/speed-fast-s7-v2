package cl.duoc.speedfast.view;

import cl.duoc.speedfast.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private JLabel tituloLabel;
    private JTable pedidosTable;
    private DefaultTableModel tablaModel;
    private JButton atrasButton;

    public VentanaListaPedidos() {
        setTitle("SpeedFast App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 15));
        setResizable(false);

        inicializarComponentes();
        construirLayout();
    }

    private void inicializarComponentes() {
        tituloLabel = new JLabel("Lista de Pedidos Registrados", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        String[] columnas = {"ID Pedido", "Dirección Entrega", "Tipo Pedido", "Detalle", "Estado"};
        tablaModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pedidosTable = new JTable(tablaModel);
        pedidosTable.getTableHeader().setReorderingAllowed(false);

        atrasButton = new JButton("Atrás");
    }

    public void construirLayout() {
        add(tituloLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(pedidosTable);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.add(atrasButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void addVolverAtrasListener(ActionListener listener) {
        atrasButton.addActionListener(listener);
    }

    public void cerrarVentana() {
        this.dispose();
    }

    public void actualizarTabla(List<Pedido> listaPedidos) {
        tablaModel.setRowCount(0);

        for (Pedido pedido : listaPedidos) {
            Object[] fila = {
                    pedido.getIdPedido(),
                    pedido.getDireccionEntrega(),
                    pedido.getTipoPedido(),
                    pedido.getDetalleEspecifico(),
                    pedido.getEstadoPedido()
            };

            tablaModel.addRow(fila);
        }
    }
}
