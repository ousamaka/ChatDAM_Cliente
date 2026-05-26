package org.iesalandalus.chatdam.chatdam_cliente.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteRest;
import org.iesalandalus.chatdam.chatdam_cliente.util.Seguridad;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    @FXML
    private void procesarLogin(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Rellena todos los campos.");
            return;
        }

        String passwordCifrada = Seguridad.hashearPassword(password);
        String rol = ClienteRest.login(usuario, passwordCifrada);

        if (rol != null) {
            System.out.println("Login exitoso. Rol: " + rol);
            abrirChat(usuario, rol);
        } else {
            mostrarError("Credenciales incorrectas.");
        }
    }

    private void abrirChat(String usuario, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/iesalandalus/chatdam/chatdam_cliente/view/chat.fxml"));
            Parent root = loader.load();

            ChatController chatController = loader.getController();
            chatController.inicializarDatos(usuario, rol);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 450, 500));
            stage.setTitle("Chat Corporativo - " + usuario);
            stage.setOnCloseRequest(e -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar el chat.");
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}