package org.iesalandalus.chatdam.chatdam_cliente.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClienteRest {

    private static final String URL_BASE = "http://localhost:8080/api";

    public static String login(String usuario, String passwordCifrada) {
        try {
            URL url = new URL(URL_BASE + "/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            String jsonInputString = "{\"usuario\":\"" + usuario + "\", \"password\":\"" + passwordCifrada + "\"}";
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInputString.getBytes("utf-8"));
            }

            if (con.getResponseCode() == 200) {
                // Leemos la respuesta tal cual viene del servidor
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();

                // Si la respuesta es {"rol":"ADMINISTRADOR"}, esto extrae "ADMINISTRADOR"
                String res = response.toString();
                if (res.contains("ADMINISTRADOR")) return "ADMINISTRADOR";
                if (res.contains("EMPLEADO")) return "EMPLEADO";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registrarEmpleado(String usuario, String passwordCifrada) {
        try {
            URL url = new URL(URL_BASE + "/empleados");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            String jsonInputString = "{\"usuario\":\"" + usuario + "\", \"password\":\"" + passwordCifrada + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            return code == 200; // Si devuelve 200, el registro fue un éxito

        } catch (Exception e) {
            System.out.println("Error en el registro: " + e.getMessage());
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

                    //Capturamos la fecha y la formateamos
                    String fechaStr = "";
                    if (obj.has("fecha") && !obj.get("fecha").isJsonNull()) {
                        fechaStr = obj.get("fecha").getAsString();
                        fechaStr = "[" + fechaStr.replace("T", " ").split("\\.")[0] + "] ";
                    }

                    mensajes.add(fechaStr + autor + ": " + texto);
                }
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo el historial: " + e.getMessage());
        }
        return mensajes;
    }

    public static void guardarMensaje(String autor, String texto) {
        try {
            URL url = new URL(URL_BASE + "/mensajes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            String jsonInputString = "{\"autor\":\"" + autor + "\", \"texto\":\"" + texto + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            con.getResponseCode(); // Ejecuta la petición
        } catch (Exception e) {
            System.out.println("Error al guardar mensaje en BD: " + e.getMessage());
        }
    }
}