package org.iesalandalus.chatdam.chatdam_cliente.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteRest;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteSocket;

import java.util.List;

public class ChatView {

    private final String usuario;
    private final Stage stage;

    public ChatView(String usuario, Stage stage) {
        this.usuario = usuario;
        this.stage = stage;
    }

    public void mostrar() {
        TextArea areaMensajes = new TextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setPrefHeight(400);
        // Hacemos que el texto haga salto de línea automático si es muy largo
        areaMensajes.setWrapText(true);

        TextField campoTexto = new TextField();
        campoTexto.setPrefWidth(250);
        Button btnEnviar = new Button("Enviar");

        btnEnviar.getStyleClass().add("success");

        HBox cajaInferior = new HBox(10);
        cajaInferior.setAlignment(Pos.CENTER);
        cajaInferior.getChildren().addAll(campoTexto, btnEnviar);

        VBox rootChat = new VBox(10);
        rootChat.setAlignment(Pos.CENTER);
        rootChat.setStyle("-fx-padding: 15;");
        rootChat.getChildren().addAll(areaMensajes, cajaInferior);

        Scene escenaChat = new Scene(rootChat, 420, 500);
        stage.setScene(escenaChat);
        stage.setTitle("ChatDAM - Sesion de: " + usuario);

        // --- CARGAR HISTORIAL ---
        List<String> historial = ClienteRest.obtenerHistorial();
        for (String msg : historial) {
            areaMensajes.appendText(msg + "\n");
        }
        if (!historial.isEmpty()) {
            areaMensajes.appendText("--- Fin del historial ---\n\n");
        }

        // --- CONECTAR SOCKETS ---
        ClienteSocket clienteSocket = new ClienteSocket(usuario, areaMensajes);
        clienteSocket.conectar();

        // --- EVENTOS ---
        btnEnviar.setOnAction(e -> {
            String texto = campoTexto.getText();
            if (!texto.trim().isEmpty()) {
                clienteSocket.enviarMensaje(texto);
                campoTexto.clear();
            }
        });

        // Cerrar la aplicación completamente al cerrar la ventana
        stage.setOnCloseRequest(e -> System.exit(0));
    }
}