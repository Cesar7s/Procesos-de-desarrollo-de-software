package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Session;
import com.mycompany.pharma.model.Venta;
import com.mycompany.pharma.model.DetalleV;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lib.SQLVenta;

/**
 * Controlador de la ventana Ventas.
 */
public class VentasController {

    private SQLVenta dbVenta;

    // =========================
    // CAMPOS PRODUCTOS
    // =========================
    @FXML private TextField txtIdProducto;
    @FXML private TextField txtCantidad;

    @FXML private Button btnAgregarProd;
    @FXML private Button btnEliminarProd;

    // =========================
    // TABLA VENTAS
    // =========================
    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, Integer> columnaId;
    @FXML private TableColumn<Venta, Double> columnaTotal;
    @FXML private TableColumn<Venta, LocalDate> columnaFecha;
    @FXML private TableColumn<Venta, String> columnaEstado;

    // =========================
    // TABLA PRODUCTOS
    // =========================
    @FXML private TableView<DetalleV> tablaProductos;
    @FXML private TableColumn<DetalleV, Integer> columnaIdProd;
    @FXML private TableColumn<DetalleV, Integer> columnaCantidad;
    @FXML private TableColumn<DetalleV, Double> columnaSubtotal;

    // =========================
    // BOTONES GENERALES
    // =========================
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;
    @FXML private Button btnVerVenta;
    @FXML private Button btnRegreso;
    
    // =========================
    // ETIQUETA
    // =========================

    @FXML private Label et_total;

    @FXML
    public void initialize() {

        dbVenta = new SQLVenta();

        // Configurar tabla ventas
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configurar tabla productos
        columnaIdProd.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));


        cargarDatos();
    }

    // ===================================
    // AGREGAR PRODUCTO A LA LISTA
    // ===================================
    @FXML
    private void agregarProducto() {
        try {
            int idProducto = Integer.parseInt(txtIdProducto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (idProducto <= 0 || cantidad <= 0)
                throw new IllegalArgumentException("Valores deben ser mayores a 0");

            Double subtotal = dbVenta.obtenerSubtotal(idProducto, cantidad);
            
            // Subtotal temporal en 0 (lo calcula la BD al registrar)
            DetalleV detalle = new DetalleV(0, idProducto, cantidad, subtotal);

            List<DetalleV> lista = this.tablaProductos.getItems();
            
            for (DetalleV detalle_aux : lista){
                if (detalle_aux.getIdProducto() == detalle.getIdProducto()){
                    throw new Exception ("El producto ya esta agregado");
                }
            }

            tablaProductos.getItems().add(detalle);
            Double total = 0.0;
            for (DetalleV detalle_aux : lista){
                total += detalle_aux.getSubtotal();
            }
            this.et_total.setText(String.valueOf(total));
            
            txtIdProducto.clear();
            txtCantidad.clear();

        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }

    // ===================================
    // ELIMINAR PRODUCTO
    // ===================================
    @FXML
    private void eliminarProducto() {
        DetalleV seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            tablaProductos.getItems().remove(seleccionado);
        } else {
            mostrarAlerta("Seleccione un producto para eliminar");
        }
    }

    // ===================================
    // REGISTRAR VENTA
    // ===================================
    @FXML
    private void agregarVenta() {
        if (this.tablaProductos.getItems().isEmpty()) {
            mostrarAlerta("Debe agregar al menos un producto");
            return;
        }

        try {
            if (tablaProductos.getItems().isEmpty()) {
                mostrarAlerta("No existen productos agregados");
            }

            Double total = Double.valueOf(this.et_total.getText());

            if (this.dbVenta.agregarVenta(total, tablaProductos.getItems())) {

                mostrarAlerta("Venta registrada correctamente");
                
                tablaProductos.getItems().clear();
                cargarDatos();
            }

        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }

    // ===================================
    // CANCELAR VENTA
    // ===================================
    @FXML
    private void cancelarVenta() {
        Venta ventaSeleccionada = tablaVentas.getSelectionModel().getSelectedItem();

        if (ventaSeleccionada == null) {
            mostrarAlerta("Seleccione una venta");
        }

        try {
            if (dbVenta.cancelarVenta(ventaSeleccionada.getId())) {
                cargarDatos();
            } 

        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }

    }

    // ===================================
    // VER DETALLES
    // ===================================
    @FXML
    private void mostrarDetalles() throws IOException {

        Venta ventaSeleccionada = tablaVentas.getSelectionModel().getSelectedItem();

        if (ventaSeleccionada == null) {
            mostrarAlerta("Seleccione una venta");
            return;
        }

        // Cargar FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/scenes/ventaInfo.fxml")
        );

        Session.setId_Venta(ventaSeleccionada.getId());
        Session.setConnection(this.dbVenta.getConnection());
        Parent root = loader.load();

        // Crear nueva ventana
        Stage nuevaVentana = new Stage();
        nuevaVentana.setTitle("Detalle de Venta");

        nuevaVentana.setScene(new Scene(root));
        nuevaVentana.setResizable(false);

        // Hace que se abra encima de la actual
        nuevaVentana.initOwner(btnRegreso.getScene().getWindow());

        //  Bloquea la ventana anterior hasta cerrar esta
        nuevaVentana.initModality(Modality.WINDOW_MODAL);

        nuevaVentana.show();
    }

    // ===================================
    // REGRESO
    // ===================================
    @FXML
    private void regreso() throws IOException {

        String fxml = Session.getRol();
        // Cargar el archivo FXML desde recursos
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/scenes/" + fxml + ".fxml")
        );
        Parent root = loader.load();

        Stage stage = (Stage) btnRegreso.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ===================================
    // CARGAR DATOS VENTAS
    // ===================================
    private void cargarDatos() {
        try {
            List<Venta> ventas = dbVenta.cargarVentas();
            tablaVentas.setItems(FXCollections.observableArrayList(ventas));
        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }

    // ===================================
    // ALERTA
    // ===================================
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("AVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
