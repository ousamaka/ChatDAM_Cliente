package org.iesalandalus.chatdam.chatdam_cliente.net;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteSocket {

    private final String usuario;
    private final TextArea areaMensajes;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public ClienteSocket(String usuario, TextArea areaMensajes) {
        this.usuario = usuario;
        this.areaMensajes = areaMensajes;
    }

    public void conectar() {
        new Thread(() -> {
            try {
                // Conectamos al servidor de sockets (puerto 4444)
                socket = new Socket("localhost", 4444);

                // Usamos DataOutputStream y DataInputStream para coincidir con el servidor
                salida = new DataOutputStream(socket.getOutputStream());
                entrada = new DataInputStream(socket.getInputStream());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Bucle infinito para escuchar mensajes de OTROS usuarios
                while (true) {
                    String mensajeServidor = entrada.readUTF(); // Leemos en formato UTF
                    String fechaHora = "[" + LocalDateTime.now().format(formatter) + "] ";

                    Platform.runLater(() -> areaMensajes.appendText(fechaHora + mensajeServidor + "\n"));
                }

            } catch (Exception e) {
                Platform.runLater(() -> areaMensajes.appendText("⚠️ Conexión con el servidor de chat perdida.\n"));
            }
        }).start();
    }

    public void enviarMensaje(String texto) {
        if (salida != null && !texto.trim().isEmpty()) {
            try {
                // El servidor espera que le enviemos el formato "Nombre: Mensaje"
                String mensajeFormateado = usuario + ": " + texto;

                // Enviamos al servidor
                salida.writeUTF(mensajeFormateado);
                salida.flush();

                // Como el servidor no nos devuelve nuestro propio mensaje, lo dibujamos en nuestra pantalla
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String fechaHora = "[" + LocalDateTime.now().format(formatter) + "] ";

                Platform.runLater(() -> areaMensajes.appendText(fechaHora + mensajeFormateado + "\n"));

            } catch (Exception e) {
                System.out.println("Error al enviar el mensaje: " + e.getMessage());
            }
        }
    }
}