package cl.duoc.speedfast.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JButton registrarPedidoButton;
    private JButton listarPedidosButton;
    private JButton iniciarEntregasButton;
    private JLabel tituloLabel;
    private JTextArea logTextArea;

    public VentanaPrincipal() {
        setTitle("SpeedFast App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        inicializarComponentes();
        construirLayout();
    }

    private void inicializarComponentes() {
        tituloLabel = new JLabel("Panel de Control SpeedFast", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        registrarPedidoButton = new JButton("Registrar Pedido");
        listarPedidosButton = new JButton("Listar Pedidos");
        iniciarEntregasButton = new JButton("Iniciar Entregas");

        Font fuenteBotones = new Font("Arial", Font.BOLD, 15);
        registrarPedidoButton.setFont(fuenteBotones);
        listarPedidosButton.setFont(fuenteBotones);
        iniciarEntregasButton.setFont(fuenteBotones);

        Dimension botonDimension = new Dimension(200, 40);
        registrarPedidoButton.setPreferredSize(botonDimension);
        listarPedidosButton.setPreferredSize(botonDimension);
        iniciarEntregasButton.setPreferredSize(botonDimension);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }

    private void construirLayout() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridBagLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;

        gbc.gridx = 0;
        panelBotones.add(registrarPedidoButton, gbc);

        gbc.gridx = 1;
        panelBotones.add(listarPedidosButton, gbc);

        gbc.gridx = 2;
        panelBotones.add(iniciarEntregasButton, gbc);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(tituloLabel, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setPreferredSize(new Dimension(0, 320));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    public void addRegistrarPedidoListener(ActionListener listener) {
        registrarPedidoButton.addActionListener(listener);
    }

    public void addListarPedidosListener(ActionListener listener) {
        listarPedidosButton.addActionListener(listener);
    }

    public void addIniciarEntregasListener(ActionListener listener) {
        iniciarEntregasButton.addActionListener(listener);
    }

    public void appendLog(String mensaje) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            logTextArea.append(mensaje + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }

    public void clearLog() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            logTextArea.setText("");
        });
    }
}
