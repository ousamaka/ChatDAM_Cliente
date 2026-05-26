package org.iesalandalus.chatdam.chatdam_cliente;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Aplicamos el tema Primer Light (estilo GitHub) de AtlantaFX
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // 2. Cargamos el diseño desde el FXML
        URL fxmlLocation = getClass().getResource("/org/iesalandalus/chatdam/chatdam_cliente/view/login.fxml");
        if (fxmlLocation == null) {
            System.err.println("¡ERROR CRÍTICO! No se ha encontrado login.fxml en la carpeta resources.");
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // 3. Preparamos y mostramos la ventana
        Scene scene = new Scene(root);
        primaryStage.setTitle("Chat Corporativo - Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Evitamos que cambien el tamaño de la ventana de login
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}