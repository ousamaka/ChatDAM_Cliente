package org.iesalandalus.chatdam.chatdam_cliente.net;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClienteSocket {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String usuario;
    private TextArea areaMensajes;

    public ClienteSocket(String usuario, TextArea areaMensajes) {
        this.usuario = usuario;
        this.areaMensajes = areaMensajes;
    }

    public void conectar() {
        try {
            socket = new Socket("localhost", 4444);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread hiloEscucha = new Thread(() -> {
                try {
                    while (true) {
                        String mensajeRecibido = in.readUTF();
                        // Platform.runLater es obligatorio en JavaFX para actualizar la interfaz desde otro hilo
                        Platform.runLater(() -> areaMensajes.appendText(mensajeRecibido + "\n"));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> areaMensajes.appendText("Desconectado del servidor.\n"));
                }
            });
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();

        } catch (Exception e) {
            Platform.runLater(() -> areaMensajes.appendText("Error: No se pudo conectar al servidor TCP.\n"));
        }
    }

    public void enviarMensaje(String texto) {
        try {
            if (socket != null && !socket.isClosed()) {
                String mensajeCompleto = usuario + ": " + texto;
                out.writeUTF(mensajeCompleto);
                out.flush();
                // Escribimos nuestro propio mensaje en nuestra pantalla
                Platform.runLater(() -> areaMensajes.appendText("Tú: " + texto + "\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}