package org.iesalandalus.chatdam.chatdam_cliente.net;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteSocket {
    private final String usuario;
    private final TextArea areaMensajes;
    private MulticastSocket socket;
    private InetAddress grupo;

    public ClienteSocket(String usuario, TextArea areaMensajes) {
        this.usuario = usuario;
        this.areaMensajes = areaMensajes;
    }

    public void conectar() {
        new Thread(() -> {
            try {
                grupo = InetAddress.getByName("225.0.0.1");
                socket = new MulticastSocket(4444);
                socket.joinGroup(grupo);
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                while (true) {
                    DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(p);
                    String msj = new String(p.getData(), 0, p.getLength(), "utf-8");
                    Platform.runLater(() -> areaMensajes.appendText("[" + LocalDateTime.now().format(fmt) + "] " + msj + "\n"));
                }
            } catch (Exception e) {
                Platform.runLater(() -> areaMensajes.appendText("⚠️ Error Multicast.\n"));
            }
        }).start();
    }

    public void enviarMensaje(String texto) {
        try {
            byte[] buf = (usuario + ": " + texto).getBytes("utf-8");
            socket.send(new DatagramPacket(buf, buf.length, grupo, 4444));
        } catch (Exception e) { e.printStackTrace(); }
    }
}