/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gestion_achats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;


public class Gestion_achats extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
            getClass().getResource("/com/mycompany/gestion_achats/view/main_layout.fxml")
        );
        Font.loadFont(getClass().getResourceAsStream("/com/mycompany/gestion_achats/font/PlusJakartaSans-SemiBold.ttf"),14);
        Font.loadFont(getClass().getResourceAsStream("/com/mycompany/gestion_achats/font/PlusJakartaSans-Regular.ttf"), 14);
        Scene scene = new Scene(root, 1266, 689);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        scene.getStylesheets().add(getClass().getResource("/com/mycompany/gestion_achats/view/style.css").toExternalForm());
        primaryStage.setTitle("Gestion des achats");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 

    public static void main(String[] args) {
        launch(args);
    }
}