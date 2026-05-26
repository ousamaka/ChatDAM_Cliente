package org.iesalandalus.chatdam.chatdam_cliente.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ClienteRest {

    private static final String URL_BASE = "http://localhost:8080/api";

    public static boolean login(String usuario, String passwordCifrada) {
        try {
            String json = "{\"usuario\":\"" + usuario + "\", \"password\":\"" + passwordCifrada + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error en la conexión REST: " + e.getMessage());
            return false;
        }
    }

    public static List<String> obtenerHistorial() {
        List<String> mensajes = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/mensajes"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parseamos el JSON que nos devuelve el servidor usando Gson
                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
                for (JsonElement elemento : jsonArray) {
                    JsonObject obj = elemento.getAsJsonObject();
                    String autor = obj.get("autor").getAsString();
                    String texto = obj.get("texto").getAsString();
                    mensajes.add(autor + ": " + texto);
                }
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo el historial: " + e.getMessage());
        }
        return mensajes;
    }
}