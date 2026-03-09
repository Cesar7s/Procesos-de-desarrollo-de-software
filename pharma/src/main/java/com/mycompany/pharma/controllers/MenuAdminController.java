package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Session;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class MenuAdminController {
    
    @FXML
    private Button btnUsuarios; // Botón para ingresar a Ventana Usuarios
    
    @FXML
    private Button btnVentas; // Botón para ingresar a Ventana Ventas
    
    @FXML
    private Button btnAlmacen; // Botón para ingresar a Ventana Almacen
    
     @FXML
    private Button btnRegreso; // Botón para regresar a la Ventana Anterior

    @FXML 
    private Label etUsuario; // Etiqueta Nombre de Usuario que ingreso


    /**
     * Inicializa el controlador.
     *
     */
    @FXML
    private void initialize() {
        this.setNombreEtiqueta(Session.getNombre());
    }
    
    @FXML
    private void switchUsuarios() throws IOException {
        // Nombre del FXML de la ventana nueva
        String fxml = "usuarios"; 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());
        

        // Obtener la ventana actual
        Stage stage = (Stage) btnRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void switchVentas() throws IOException {
        // Nombre del FXML nuevo
        String fxml = "ventas"; 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());
        

        // Obtener la ventana actual
        Stage stage = (Stage) btnRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void switchAlmacen() throws IOException{
        // Nombre del FXML de la ventana nueva
        String fxml = "almacen"; 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());
        

        // Obtener la ventana actual
        Stage stage = (Stage) btnRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void regreso() throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = "login"; 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());

        // Obtener la ventana actual
        Stage stage = (Stage) btnRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
    
    /*
     Ingresa el nombre del usuario que ingreso, para la bienvenida.
    */
    public void setNombreEtiqueta(String nombre){
        this.etUsuario.setText(nombre);
    }

}
