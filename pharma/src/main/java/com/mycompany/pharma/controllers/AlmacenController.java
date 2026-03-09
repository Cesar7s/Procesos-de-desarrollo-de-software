package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Medicamento;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lib.SQLMedicamento;
import lib.SQLUser;

/**
 * FXML Controller class.
 * Clase para manejar el control de medicamentos.
 */
public class AlmacenController {
    
    //Conexión a la base de datos
    private SQLMedicamento dbMedicamento;

    // Paneles
    @FXML private StackPane SP1;
    @FXML private AnchorPane PEliminar;
    @FXML private AnchorPane PAgregar;
    @FXML private AnchorPane PModificar;

    // Botones
    @FXML private Button btnCrearMedicamento;
    @FXML private Button btnEliminarMedicamento;
    @FXML private Button btnMNombre;
    @FXML private Button btnMCompuesto;
    @FXML private Button btnMPrecio;
    @FXML private Button btnMCantidad;
    @FXML private Button BAgregar;
    @FXML private Button BEliminar;
    @FXML private Button BEditar;
    @FXML private Button BRegreso;

    // TextFields para agregar
    @FXML private TextField txtNombre;
    @FXML private TextField txtCompuesto;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;

    // TextFields para eliminar
    @FXML private TextField txtIdEliminar;

    // TextFields para modificar
    @FXML private TextField txtId;
    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoCompuesto;
    @FXML private TextField txtNuevoPrecio;
    @FXML private TextField txtNuevaCantidad;

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
     * Aqui se inicializan las propiedades de la tabla 
     * para una mejor manipulacion.
     *
     */
    @FXML
    private void initialize() {
        dbMedicamento = new SQLMedicamento();
        
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCompuesto.setCellValueFactory(new PropertyValueFactory<>("compuesto"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        this.cargarDatos();
    }

    // =======================
    // MÉTODOS DE PANEL
    // =======================
    
    /**
     * Cambia a el panel para agregar medicamentos.
     */
    @FXML
    private void cambiarAgregar() {
        PAgregar.setVisible(true);
        PEliminar.setVisible(false);
        PModificar.setVisible(false);
    }

    /**
     * Cambia a el panel para eliminar medicamentos.
     */
    @FXML
    private void cambiarEliminar() {
        PAgregar.setVisible(false);
        PEliminar.setVisible(true);
        PModificar.setVisible(false);
    }

    /**
     * Cambia a el panel para editar medicamentos.
     */
    @FXML
    private void cambiarEditar() {
        PAgregar.setVisible(false);
        PEliminar.setVisible(false);
        PModificar.setVisible(true);
    }

    /**
     * Permite regresar a la ventana anterior a esta.
     * @throws IOException Si ocurre un erro en la lectura de los fxml.
     */
    @FXML
    private void regreso() throws IOException {

        // Cargar el archivo FXML desde recursos
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/scenes/admin.fxml")
        );
        Parent root = loader.load();

        // Obtener la ventana actual
        Stage stage = (Stage) BRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    // =======================
    // MÉTODOS CRUD
    // =======================
    
    /**
     * Metodo que se activa cuando se hace click en el boton de agregar.
     */
    @FXML
    private void crearMedicamento() {
        try {//Verifica los campos de ingreso
            if (!this.validarCamposIngreso()) {
                //Si alguno esta vacio
                throw new Exception("Todos los campos deben estar llenos");
            }
            
            //Almacena los datos en variables
            String nombre = txtNombre.getText();
            String compuesto = txtCompuesto.getText();
            String precio = txtPrecio.getText();
            String cantidad = txtCantidad.getText();
            
            //Valida que los datos numericos sean consistentes
            Double precioNum = this.validarNumeroPositivoDouble(precio);
            Integer cantidadNum = this.validarNumeroPositivoEntero(cantidad);
            
            //Se almacenan los datos como un nuevo medicamento
            if(this.dbMedicamento.agregarMedicamento(nombre, compuesto, precioNum, cantidadNum)){
                //Si se agrego correctamente
                this.mostrarAlerta( "Medicamento agregada correctamente");
                //Limpia los campos de ingreso de datos
                txtNombre.setText("");
                txtCompuesto.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
                //Carga los datos de la BD en la tabla
                this.cargarDatos();
            } else {//Si no se agrego correctamente
                this.mostrarAlerta( "ERROR- No se pudo agregar");
            }
        } catch (Exception e) { //Si ocurre alguna excepcion.
            this.mostrarAlerta(e.getMessage());
        }
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de eliminar.
     */
    @FXML
    private void eliminarMedicamento() {
        //Almacena el dato en variable
        String id = txtIdEliminar.getText().trim();
        
        //Verifica que no este vacio el campo de Id
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo");
        }

        try {//Intenta convertir el String a un Integer
            Integer idNum = this.validarNumeroPositivoEntero(id);

            //Se elimina el medicamento en la BD
            if (this.dbMedicamento.eliminarMedicamento(idNum)) {
                //Si se elimino correctamente
                this.mostrarAlerta("Eliminado correctamente");
                
                //Limpia el campo de texto
                txtIdEliminar.setText("");
                
                //Carga los nuevos datos de la BD en la tabla
                this.cargarDatos();
            } else {
                //Si no se elimino correctamente
                this.mostrarAlerta("ERROR- No se pudo eliminar");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta(e.getMessage());
        }
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de modificar para nombre.
     */
    @FXML
    private void modificarNombre() {
        //Almacena los datos de los campos en variables
        String id = txtId.getText().trim();
        String nuevoNombre = txtNuevoNombre.getText().trim();

        //Verifica que ambos no esten vacios
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoNombre.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Nombre");
        }

        try {//Intenta convertir el String a un Integer
            Integer idNum = this.validarNumeroPositivoEntero(id);

            //Se modifica el nombre en la BD
            if (this.dbMedicamento.cambiarNombre(idNum, nuevoNombre)) {
                //Si si se modifico correctamente
                this.mostrarAlerta("Modificado correctamente");
                //Limpia los campos
                txtIdEliminar.setText("");
                this.txtNuevoNombre.setText("");
                //Actualiza los datos de la tabla con la nueva info en la BD
                this.cargarDatos();
            } else {//Si no se modifico correctamente
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta(e.getMessage());
        }
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de modificar para compuesto.
     */
    @FXML
    private void modificarCompuesto() {
        //Almacena los datos de los campos en variables
        String id = txtId.getText().trim();
        String nuevoCompuesto = txtNuevoCompuesto.getText().trim();

        //Verifica que ambos no esten vacios
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoCompuesto.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Compuesto");
        }

        try {//Intenta convertir el String a un Integer
            Integer idNum = this.validarNumeroPositivoEntero(id);

            //Se modifica el compuesto en la BD
            if (this.dbMedicamento.cambiarCompuesto(idNum, nuevoCompuesto)) {
                //Si si se modifico correctamente
                this.mostrarAlerta("Modificado correctamente");
                //Limpia los campos
                txtIdEliminar.setText("");
                this.txtNuevoCompuesto.setText("");
                //Actualiza los datos de la tabla con la nueva info en la BD
                this.cargarDatos();
            } else {//Si no se modifico correctamente
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta(e.getMessage());
        }
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de modificar para precio.
     */
    @FXML
    private void modificarPrecio() {
        //Almacena los datos de los campos en variables
        String id = txtId.getText().trim();
        String nuevoPrecio = txtNuevoPrecio.getText().trim();
        //Verifica que ambos no esten vacios
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoPrecio.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Precio");
        }

        try {//Intenta convertir los String a Integer y Double respectivamente
            Integer idNum = this.validarNumeroPositivoEntero(id);
            Double precioNum = this.validarNumeroPositivoDouble(nuevoPrecio);
            //Se modifica el precio en la BD
            if (this.dbMedicamento.cambiarPrecio(idNum, precioNum)) {
                //Si no se modifico correctamente
                this.mostrarAlerta("Modificado correctamente");
                //Limpia los campos
                txtIdEliminar.setText("");
                this.txtNuevoPrecio.setText("");
                //Actualiza los datos de la tabla con la nueva info en la BD
                this.cargarDatos();
            } else {//Si no se modifico correctamente
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta(e.getMessage());
        }
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de modificar para cantidad/Stock.
     */
    @FXML
    private void modificarCantidad() {
        //Almacena los datos de los campos en variables
        String id = txtId.getText().trim();
        String nuevaCantidad = txtNuevaCantidad.getText().trim();

        //Verifica que ambos no esten vacios
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevaCantidad.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Cantidad");
        }

        try {//Intenta convertir los String a Integer y Double respectivamente
            Integer idNum = this.validarNumeroPositivoEntero(id);
            Integer cantidadNum = this.validarNumeroPositivoEntero(nuevaCantidad);

            //Se modifica la cantidad/Stock en la BD
            if (this.dbMedicamento.cambiarCantidad(idNum, cantidadNum)) {
                //Si si se modifico correctamente
                this.mostrarAlerta("Modificado correctamente");
                //Limpia los campos
                txtIdEliminar.setText("");
                this.txtNuevaCantidad.setText("");
                //Actualiza los datos de la tabla con la nueva info en la BD
                this.cargarDatos();
            } else {//Si no se modifico correctamente
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta(e.getMessage());
        }
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
        return !this.txtNombre.getText().trim().isEmpty() 
                && !this.txtCompuesto.getText().trim().isEmpty()
                && !this.txtCantidad.getText().trim().isEmpty() 
                && !this.txtPrecio.getText().trim().isEmpty();
    }
    
    /**
     * Convierte un texto en un número de tipo Double y verifica que sea
     * positivo.
     * Lanza excepciones específicas según el tipo de error:
     *
     * @param texto El texto que se desea validar y convertir.
     * @return El valor numérico convertido a Double si es mayor que cero.
     * @throws NumberFormatException si el texto no representa un número válido.
     * @throws IllegalArgumentException si el número es menor o igual a cero.
     */
    private Double validarNumeroPositivoDouble(String texto) {
        double valor;
        try {
            valor = Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {//Si el texto no es un número válido
            throw new NumberFormatException("Ingrese un número válido");
        }

        if (valor <= 0) {//Si el número es menor o igual a cero
            throw new IllegalArgumentException("El número debe ser mayor que cero");
        }

        return valor;
    }

    
    /**
     * Convierte un texto en un número de tipo Entero y verifica que sea
     * positivo.
     * Lanza excepciones específicas según el tipo de error:
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
        } catch (NumberFormatException e) {//Si el texto no es un número válido
            throw new NumberFormatException("Ingrese un número válido");
        }

        if (valor <= 0) {//Si el número es menor o igual a cero
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
        Stage stage = (Stage) this.txtNombre.getScene().getWindow();//Obtener la ventana
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();//No continua el programa si el usuario no da click en aceptar.
    }
    
    /**
     * Carga los medicamentos desde la BD a la tabla.
     */
    private void cargarDatos() {
        try {
            // 1. Obtener los medicamentos desde la BD
            List<Medicamento> medicamentos = this.dbMedicamento.cargarMedicamentos();

            // 3. Crear un ObservableList y asignarlo a la tabla
            tabla.setItems(FXCollections.observableArrayList(medicamentos));

        } catch (Exception e) {//Si ocurre alguna excepcion
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }
}
