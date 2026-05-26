package org.iesalandalus.chatdam.chatdam_cliente;

import javafx.application.Application;
import javafx.stage.Stage;
import org.iesalandalus.chatdam.chatdam_cliente.view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Aplicamos el tema de AtlantaFX basado en el diseño de GitHub
        Application.setUserAgentStylesheet(new atlantafx.base.theme.PrimerLight().getUserAgentStylesheet());

        // Instanciamos y mostramos la vista de Login
        LoginView loginView = new LoginView(primaryStage);
        loginView.mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}