package org.iesalandalus.chatdam.chatdam_cliente.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteRest;
import org.iesalandalus.chatdam.chatdam_cliente.util.Seguridad;

public class AdminController {

    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private Label lblMensaje;

    @FXML
    private void registrar(ActionEvent event) {
        String usuario = txtNuevoUsuario.getText();
        String password = txtNuevaPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Rellena todos los campos.", false);
            return;
        }

        // Ciframos la contraseña y enviamos
        String passwordCifrada = Seguridad.hashearPassword(password);
        boolean exito = ClienteRest.registrarEmpleado(usuario, passwordCifrada);

        if (exito) {
            mostrarMensaje("Empleado registrado con éxito.", true);
            txtNuevoUsuario.clear();
            txtNuevaPassword.clear();
        } else {
            mostrarMensaje("Error: El usuario ya existe o falló la red.", false);
        }
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) txtNuevoUsuario.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String texto, boolean exito) {
        lblMensaje.setText(texto);
        lblMensaje.setTextFill(exito ? javafx.scene.paint.Color.web("#2da44e") : javafx.scene.paint.Color.web("#cf222e"));
        lblMensaje.setVisible(true);
    }
}