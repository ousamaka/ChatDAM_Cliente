package org.iesalandalus.chatdam.chatdam_cliente.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteRest;
import org.iesalandalus.chatdam.chatdam_cliente.net.ClienteSocket;
import java.util.List;

public class ChatController {

    @FXML private Label lblUsuario;
    @FXML private Button btnAdmin;
    @FXML private TextArea areaMensajes;
    @FXML private TextField campoTexto;

    private String usuario;
    private ClienteSocket clienteSocket;

    // Asegúrate de que tu ChatController tenga esta estructura
    public void inicializarDatos(String usuario, String rol) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario: " + usuario + " [" + rol + "]");

        // Lógica de visibilidad del botón
        boolean esAdmin = "ADMINISTRADOR".equalsIgnoreCase(rol);
        btnAdmin.setVisible(esAdmin);
        btnAdmin.setManaged(esAdmin);

        //Cargar Historial
        cargarHistorial();

        //Inicializamos el shocket nada mas empezar
        this.clienteSocket = new ClienteSocket(usuario, areaMensajes);
        this.clienteSocket.conectar();
    }

    private void cargarHistorial() {
        List<String> historial = ClienteRest.obtenerHistorial();
        for (String msg : historial) areaMensajes.appendText(msg + "\n");
        if (!historial.isEmpty()) areaMensajes.appendText("--- Fin del historial ---\n\n");
    }

    private void conectarSocket() {
        clienteSocket = new ClienteSocket(usuario, areaMensajes);
        clienteSocket.conectar();
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String texto = campoTexto.getText();
        if (!texto.trim().isEmpty()) {
            // 1. Envia el mensaje por red a los demás (Multicast como decia el profe)
            clienteSocket.enviarMensaje(texto);

            // 2. Guarda el mensaje en la base de datos (REST) usando la variable exacta 'usuario'
            ClienteRest.guardarMensaje(usuario, texto);

            // 3. Limpia el campo
            campoTexto.clear();
        }
    }

    @FXML
    private void abrirGestionEmpleados(ActionEvent event) {
        try {
            // ruta de los recursos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/iesalandalus/chatdam/chatdam_cliente/view/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestión de Empleados");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void salirAplicacion(ActionEvent event) {
        if (clienteSocket != null) {
            // Cerramos el socket si está abierto antes de salir
            try {

            } catch (Exception e) {}
        }
        System.exit(0);
    }
}