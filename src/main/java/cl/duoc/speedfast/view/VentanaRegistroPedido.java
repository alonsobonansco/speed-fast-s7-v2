package cl.duoc.speedfast.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaRegistroPedido extends JFrame {

    private JLabel tituloLabel;
    private JTextField idTextField;
    private JTextField direccionTextField;
    private JComboBox<String> tipoComboBox;
    private JButton guardarButton;
    private JButton atrasButton;

    private JPanel panelDinamico;
    private JLabel etiquetaDinamica;
    private JTextField campoDinamicoText;
    private JCheckBox campoDinamicoCheck;

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

        idTextField = new JTextField(15);
        direccionTextField = new JTextField(15);

        String[] tipos = {"Comida", "Encomienda", "Express"};
        tipoComboBox = new JComboBox<>(tipos);

        guardarButton = new JButton("Guardar");
        atrasButton = new JButton("Atrás");

        panelDinamico = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        etiquetaDinamica = new JLabel();
        campoDinamicoText = new JTextField(15);
        campoDinamicoCheck = new JCheckBox("¿Comida en buen estado?");

        tipoComboBox.addActionListener(e -> actualizarFormularioDinamico());
    }

    public void construirLayout() {
        add(tituloLabel, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 20));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelFormulario.add(new JLabel("ID del Pedido:"));
        panelFormulario.add(idTextField);

        panelFormulario.add(new JLabel("Dirección de Entrega:"));
        panelFormulario.add(direccionTextField);

        panelFormulario.add(new JLabel("Tipo de Pedido:"));
        panelFormulario.add(tipoComboBox);

        actualizarFormularioDinamico();

        JPanel contenedorCamposCompactos = new JPanel(new GridLayout(2, 1, 0, 10));
        contenedorCamposCompactos.add(panelFormulario);
        contenedorCamposCompactos.add(panelDinamico);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        panelCentro.add(contenedorCamposCompactos);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.add(guardarButton);
        panelBotones.add(atrasButton);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarFormularioDinamico() {
        String seleccion = (String) tipoComboBox.getSelectedItem();

        panelDinamico.removeAll();
        campoDinamicoText.setText("");
        campoDinamicoCheck.setSelected(true);

        if (seleccion != null) {
            switch (seleccion.toUpperCase()) {
                case "COMIDA" -> {
                    panelDinamico.add(campoDinamicoCheck);
                }
                case "EXPRESS" -> {
                    etiquetaDinamica.setText("Distancia del envío (Km):");
                    panelDinamico.add(etiquetaDinamica);
                    panelDinamico.add(campoDinamicoText);
                }
                case "ENCOMIENDA" -> {
                    etiquetaDinamica.setText("Peso del paquete (kg):");
                    panelDinamico.add(etiquetaDinamica);
                    panelDinamico.add(campoDinamicoText);
                }
            }
        }

        panelDinamico.revalidate();
        panelDinamico.repaint();
    }

    public String getIdPedido() {
        return idTextField.getText().trim();
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

    public String getInputDinamicoTexto() {
        return campoDinamicoText.getText().trim();
    }

    public boolean getInputDinamicoCheck() {
        return campoDinamicoCheck.isSelected();
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
        idTextField.setText("");
        direccionTextField.setText("");
        campoDinamicoText.setText("");
        idTextField.requestFocus();
    }
}
