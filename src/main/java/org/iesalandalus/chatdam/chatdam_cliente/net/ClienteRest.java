package org.iesalandalus.chatdam.chatdam_cliente.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClienteRest {

    private static final String URL_BASE = "http://localhost:8080/api";

    public static boolean login(String usuario, String passwordCifrada) {
        try {
            URL url = new URL(URL_BASE + "/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"usuario\":\"" + usuario + "\", \"password\":\"" + passwordCifrada + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            return code == 200;

        } catch (Exception e) {
            System.out.println("Error en la conexión REST: " + e.getMessage());
            return false;
        }
    }

    public static List<String> obtenerHistorial() {
        List<String> mensajes = new ArrayList<>();
        try {
            URL url = new URL(URL_BASE + "/mensajes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int code = con.getResponseCode();
            if (code == 200) {
                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement elemento : jsonArray) {
                    JsonObject obj = elemento.getAsJsonObject();
                    String autor = obj.get("autor").getAsString();
                    String texto = obj.get("texto").getAsString();
                    mensajes.add(autor + ": " + texto);
                }
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo el historial: " + e.getMessage());
        }
        return mensajes;
    }
}