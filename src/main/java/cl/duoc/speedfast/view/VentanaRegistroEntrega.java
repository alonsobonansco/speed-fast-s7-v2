package cl.duoc.speedfast.view;

import cl.duoc.speedfast.model.entity.Pedido;
import cl.duoc.speedfast.service.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaRegistroEntrega extends JFrame {

    private JComboBox<Pedido> pedidoJComboBox;
    private JComboBox<Repartidor> repartidorJComboBox;
    private JButton guardarButton;
    private JButton atrasButton;

    public VentanaRegistroEntrega() {
        setTitle("Asignar Entrega Manual - SpeedFast");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 20));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pedidoJComboBox = new JComboBox<>();
        repartidorJComboBox = new JComboBox<>();
        guardarButton = new JButton("Asignar Entrega");
        atrasButton = new JButton("Atrás");

        add(new JLabel("Seleccionar Pedido:")); add(pedidoJComboBox);
        add(new JLabel("Seleccionar Repartidor:")); add(repartidorJComboBox);
        add(guardarButton); add(atrasButton);
    }

    public JComboBox<Pedido> getComboPedidos() { return pedidoJComboBox; }
    public JComboBox<Repartidor> getComboRepartidores() { return repartidorJComboBox; }
    public void addGuardarListener(ActionListener l) { guardarButton.addActionListener(l); }
    public void addVolverListener(ActionListener l) { atrasButton.addActionListener(l); }
    public void cerrarVentana() { this.dispose(); }

    // Métodos para llenar los combos de forma fácil desde el controlador
    public void cargarPedidos(List<Pedido> pedidos) {
        pedidoJComboBox.removeAllItems();
        for (Pedido p : pedidos) pedidoJComboBox.addItem(p);
    }

    public void cargarRepartidores(List<Repartidor> repartidores) {
        repartidorJComboBox.removeAllItems();
        for (Repartidor r : repartidores) repartidorJComboBox.addItem(r);
    }
}
