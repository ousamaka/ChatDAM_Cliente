package org.iesalandalus.chatdam.chatdam_cliente.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteRest;
import org.iesalandalus.chatdam.chatdam_cliente.util.Seguridad;

public class LoginView {

    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        Label lblUsuario = new Label("Usuario:");
        TextField txtUsuario = new TextField();

        Label lblPassword = new Label("Contraseña:");
        PasswordField txtPassword = new PasswordField();

        Button btnLogin = new Button("Iniciar Sesión");
        Button btnSalir = new Button("Salir");

        // Clase de estilo de AtlantaFX para destacar el botón principal
        btnLogin.getStyleClass().add("accent");

        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText();
            String password = txtPassword.getText();
            procesarLogin(usuario, password);
        });

        btnSalir.setOnAction(e -> stage.close());

        root.getChildren().addAll(lblUsuario, txtUsuario, lblPassword, txtPassword, btnLogin, btnSalir);

        Scene scene = new Scene(root, 300, 280);
        stage.setTitle("Chat Corporativo - Login");
        stage.setScene(scene);
        stage.show();
    }

    private void procesarLogin(String usuario, String password) {
        String passwordCifrada = Seguridad.hashearPassword(password);
        boolean loginCorrecto = ClienteRest.login(usuario, passwordCifrada);

        if (loginCorrecto) {
            System.out.println("Login exitoso. Cambiando a ventana de chat.");
            ChatView chatView = new ChatView(usuario, stage);
            chatView.mostrar();
        } else {
            System.out.println("Error de credenciales o servidor desconectado.");
        }
    }
}