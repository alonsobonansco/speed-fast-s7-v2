package cl.duoc.speedfast;

import cl.duoc.speedfast.controller.ControladorPrincipal;
import cl.duoc.speedfast.view.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
            new ControladorPrincipal(ventanaPrincipal);
            ventanaPrincipal.setVisible(true);
        });
    }
}
