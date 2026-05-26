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
            lblMensaje.setText("Rellena todos los campos.");
            lblMensaje.setVisible(true);
            return;
        }

        String passwordCifrada = Seguridad.hashearPassword(password);
        if (ClienteRest.registrarEmpleado(usuario, passwordCifrada)) {
            lblMensaje.setText("¡Empleado registrado!");
            lblMensaje.setVisible(true);
            txtNuevoUsuario.clear();
            txtNuevaPassword.clear();
        } else {
            lblMensaje.setText("Error en el registro.");
            lblMensaje.setVisible(true);
        }
    }

    @FXML
    private void cerrar(ActionEvent event) {
        ((Stage) txtNuevoUsuario.getScene().getWindow()).close();
    }
}