package cl.duoc.speedfast.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaRegistroPedido extends JFrame {

    private JLabel tituloLabel;
    private JTextField direccionTextField;
    private JComboBox<String> tipoComboBox;
    private JButton guardarButton;
    private JButton atrasButton;

    public VentanaRegistroPedido() {
        setTitle("SpeedFast App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        inicializarComponentes();
        construirLayout();
    }

    public void inicializarComponentes() {
        tituloLabel = new JLabel("Formulario de Registros de Pedidos", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        direccionTextField = new JTextField(15);

        String[] tipos = {"Comida", "Encomienda", "Express"};
        tipoComboBox = new JComboBox<>(tipos);

        guardarButton = new JButton("Guardar");
        atrasButton = new JButton("Atrás");
    }

    public void construirLayout() {
        add(tituloLabel, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fuenteCampos = new Font("Arial", Font.PLAIN, 14);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel direccionLabel = new JLabel("Dirección de Entrega:");
        direccionLabel.setFont(fuenteCampos);
        panelFormulario.add(direccionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        direccionTextField.setFont(fuenteCampos);
        panelFormulario.add(direccionTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel tipoLabel = new JLabel("Tipo de Pedido:");
        tipoLabel.setFont(fuenteCampos);
        panelFormulario.add(tipoLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        tipoComboBox.setFont(fuenteCampos);
        panelFormulario.add(tipoComboBox, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.add(guardarButton);
        panelBotones.add(atrasButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public String getDireccionEntrega() {
        return direccionTextField.getText().trim();
    }

    public String getTipoPedido() {
        return (String) tipoComboBox.getSelectedItem();
    }

    public void cerrarVentana() {
        this.dispose();
    }

    public void addGuardarListener(ActionListener listener) {
        guardarButton.addActionListener(listener);
    }

    public void addVolverAtrasListener(ActionListener listener) {
        atrasButton.addActionListener(listener);
    }

    public void mostrarMensajeConfirmacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Pedido registrado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarFormulario() {
        direccionTextField.setText("");
    }
}
