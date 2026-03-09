package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Medicamento;
import com.mycompany.pharma.model.Session;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SQLMedicamento;


public class MedicamentosController {
    
//Conexión a la base de datos
    private SQLMedicamento dbMedicamento;
    
    @FXML 
    private Button btnRegreso;

    // TableView y columnas
    @FXML
    private TableView<Medicamento> tabla;
    @FXML
    private TableColumn<Medicamento, Integer> columnaId;
    @FXML
    private TableColumn<Medicamento, String> columnaNombre;
    @FXML
    private TableColumn<Medicamento, String> columnaCompuesto;
    @FXML
    private TableColumn<Medicamento, Double> columnaPrecio;
    @FXML
    private TableColumn<Medicamento, Integer> columnaCantidad;

    // ListView
    @FXML private ListView<?> LV1;
    
    /**
     * Inicializa el controlador.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    @FXML
    private void initialize() throws SQLException {
        dbMedicamento = new SQLMedicamento();
        
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCompuesto.setCellValueFactory(new PropertyValueFactory<>("compuesto"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        this.cargarDatos();
    }



    @FXML
    private void regreso() throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = Session.getRol(); 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());

        // Obtener la ventana actual
        Stage stage = (Stage) this.tabla.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }




    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("EVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) this.tabla.getScene().getWindow();
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();
    }
    
    
    private void cargarDatos() {
        try {
            // 1. Obtener los medicamentos desde la BD
            List<Medicamento> medicamentos = this.dbMedicamento.cargarMedicamentos();

            // 3. Crear un ObservableList y asignarlo a la tabla
            tabla.setItems(FXCollections.observableArrayList(medicamentos));

        } catch (Exception e) {
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }
}
