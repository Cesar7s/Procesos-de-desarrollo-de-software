package com.mycompany.pharma.controllers;


import com.mycompany.pharma.model.Session;
import com.mycompany.pharma.model.Venta;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lib.SQLVenta;


public class VentasController {
    
    //Conexión a la base de datos
    private SQLVenta dbVenta;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtCantidad;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnRegreso;

    @FXML
    private TableView<Venta> tabla;

    @FXML
    private TableColumn<Venta, Integer> columnaId;

    @FXML
    private TableColumn<Venta, Integer> columnaIdProducto;

    @FXML
    private TableColumn<Venta, Integer> columnaCantidad;

    @FXML
    private TableColumn<Venta, Double> columnaPrecio;

    @FXML
    private TableColumn<Venta, Double> columnaTotal;

    @FXML
    private TableColumn<Venta, LocalDate> columnaFecha;

    @FXML
    private TableColumn<Venta, String> columnaEstado;


       
    /**
     * Inicializa el controlador.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    @FXML
    public void initialize() throws SQLException {
        dbVenta = new SQLVenta();

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaIdProducto.setCellValueFactory(new PropertyValueFactory<>("productoId"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnidad"));
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        this.cargarDatos();
    }

    @FXML
    private void agregarVenta() {
        try {
            if (!this.validarCamposIngreso()) {
                throw new Exception("Todos los campos deben estar llenos");
            }
            
            int idProducto = this.validarNumeroPositivoEntero(txtIdProducto.getText().trim());
            int cantidad = this.validarNumeroPositivoEntero(txtCantidad.getText().trim());

            if (this.dbVenta.agregarVenta(idProducto, cantidad)) {
                this.mostrarAlerta("Venta Exitosa");

                txtIdProducto.clear();
                txtCantidad.clear();
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo realizar la Venta");
            }

        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void cancelarVenta() {
        // Obtiene el item seleccionado
        Venta ventaSeleccionada = tabla.getSelectionModel().getSelectedItem();

        if (ventaSeleccionada != null) {
            int idVenta = ventaSeleccionada.getId();

            // Confirmación antes de borrar
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Seguro que quieres cancelar esta venta?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                try {
                    if (this.dbVenta.cancelarVenta(idVenta)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "Venta cancelada correctamente", ButtonType.OK);
                        alert.showAndWait();
                        this.cargarDatos();
                    } else {
                        throw new Exception("Error no se pudo Cancelar");
                    }

                } catch (Exception e) {
                    this.mostrarAlerta(e.getMessage());
                }
            }
        } else {
            this.mostrarAlerta("Seleccione una venta para cancelar");
        }
    }

    @FXML
    private void regreso() throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = Session.getRol(); 

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

    // =======================
    // MÉTODOS DE VALIDACION
    // =======================
    /**
     * Verifica si los campos para ingresar un medicamento están llenos.
     *
     * @return {@code true} si todos campos contienen texto, {@code false} en
     * caso contrario.
     */
    private boolean validarCamposIngreso() {
        return !this.txtIdProducto.getText().trim().isEmpty()
                && !this.txtCantidad.getText().trim().isEmpty();
    }

    /**
     * Convierte un texto en un número de tipo Entero y verifica que sea
     * positivo. Lanza excepciones específicas según el tipo de error:
     *
     * NumberFormatException si el texto no es un número válido.
     * IllegalArgumentException si el número es menor o igual a cero.
     *
     * @param texto El texto que se desea validar y convertir.
     * @return El valor numérico convertido a Entero si es mayor que cero.
     * @throws NumberFormatException si el texto no representa un número válido.
     * @throws IllegalArgumentException si el número es menor o igual a cero.
     */
    private Integer validarNumeroPositivoEntero(String texto) {
        Integer valor;
        try {
            valor = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ingrese un número válido");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException("El número debe ser mayor que cero");
        }

        return valor;
    }

    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("AVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) this.txtIdProducto.getScene().getWindow();
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();
    }

    private void cargarDatos() {
        try {
            // 1. Obtener los medicamentos desde la BD
            List<Venta> ventas = this.dbVenta.cargarVentas();

            // 3. Crear un ObservableList y asignarlo a la tabla
            tabla.setItems(FXCollections.observableArrayList(ventas));

        } catch (Exception e) {
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }
}
