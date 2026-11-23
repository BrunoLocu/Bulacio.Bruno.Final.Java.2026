
package ui;

import java.util.Optional;

import javax.imageio.IIOException;

import java.io.IOException;
import java.util.List;

import excepciones.CodigoDuplicadoException;
import inventario.GestorInventario;
import modelo.CertificacionFuente;
import modelo.Fuente;
import modelo.InsumoPC;
import modelo.MemoriaRAM;
import modelo.PlacaVideo;
import persistencia.PersistenciaInventario;
import persistencia.PersistenciaJSON;
import persistencia.PersistenciaCSV;
import persistencia.PersistenciaTXT;
import inventario.ComparadorMarca;
import inventario.ComparadorPrecio;
import inventario.FiltroInventario;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InventarioApp extends Application {

    @Override
    public void start(Stage ventana) {

        // 1. GESTOR DEL INVENTARIO
        GestorInventario gestor = new GestorInventario();

        // 2. TABLA
        Label titulo = new Label("Inventario de insumos de PC");

        TableView<InsumoPC> tabla = new TableView<>();

        TableColumn<InsumoPC, Integer> columnaCodigo =
                new TableColumn<>("Código");

        TableColumn<InsumoPC, String> columnaMarca =
                new TableColumn<>("Marca");

        TableColumn<InsumoPC, String> columnaModelo =
                new TableColumn<>("Modelo");

        TableColumn<InsumoPC, Double> columnaPrecio =
                new TableColumn<>("Precio");

        TableColumn<InsumoPC, Integer> columnaStock =
                new TableColumn<>("Stock");

        // 3. CONFIGURACIÓN DE LAS COLUMNAS
        columnaCodigo.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(datos.getValue().getCodigo()));

        columnaMarca.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(datos.getValue().getMarca()));

        columnaModelo.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(datos.getValue().getModelo()));

        columnaPrecio.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(datos.getValue().getPrecio()));

        columnaStock.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(datos.getValue().consultarStock()));

        tabla.getColumns().addAll(
                columnaCodigo,
                columnaMarca,
                columnaModelo,
                columnaPrecio,
                columnaStock
        );

        // 4. LISTA OBSERVABLE
        ObservableList<InsumoPC> productos =
                FXCollections.observableArrayList(gestor.listar());

        tabla.setItems(productos);

        // FILTRO
        TextField campoPrecioMaximo = new TextField();
        campoPrecioMaximo.setPromptText("Precio maximo");
        Button botonFiltrar = new Button("Filtrar por precio");

        botonFiltrar.setOnAction(evento -> {
            try{
                double precioMaximo = Double.parseDouble(campoPrecioMaximo.getText());
                if (precioMaximo < 0 || !Double.isFinite(precioMaximo)) {
                    mostrarError(
                        "Precio incorrecto",
                        "El precio máximo debe ser finito y no negativo."
                    );
                    return;
                }

                List<InsumoPC> filtrados = FiltroInventario.filtrarPorPrecioMaximo(gestor.listar(), precioMaximo);
                productos.setAll(filtrados);
            }catch(NumberFormatException e){
                mostrarError("Precio maximo es menor a cero", "El precio maximo debe ser un numero de valor finito y no negativo");
            }
        });

        // QUITAR FILTRO
        Button botonQuitarFiltro = new Button("Quitar filtro");
        botonQuitarFiltro.setOnAction(evento -> productos.setAll(gestor.listar()));

        // BOTON ORDENAR POR CÓDIGO
        Button botonOrdenarCodigo = new Button("Ordenar por código");
        botonOrdenarCodigo.setOnAction(evento -> FXCollections.sort(productos)); //este usa compareTo()

        // BOTON ORDENAR POR PRECIO
        Button botonOrdenarPrecio = new Button("Ordenar por precio");
        botonOrdenarPrecio.setOnAction(evento -> FXCollections.sort(productos, new ComparadorPrecio()));

        // BOTON ORDENAR POR MARCA
        Button botonOrdenarMarca = new Button("Ordenar por marca");
        botonOrdenarMarca.setOnAction(evento -> FXCollections.sort(productos, new ComparadorMarca()));

        // BOTON GUARDAR INVENTARIO DAT
        Button botonGuardarInventarioDAT = new Button("Guardar inventario DAT");
        botonGuardarInventarioDAT.setOnAction(evento -> {
            try {
                PersistenciaInventario.guardarInventario(gestor.listar(), "inventario.dat");
            } catch (IOException e) {
                mostrarError("Error al guardar", e.getMessage());
            }
        });

        // BOTON CARGAR INVENTARIO DAT
        Button botonCargarInventarioDAT = new Button("Cargar inventario DAT");
        botonCargarInventarioDAT.setOnAction(evento -> {
            try {
                List<InsumoPC> listaProductos = PersistenciaInventario.cargarInventario("inventario.dat");

                for (InsumoPC insumo : gestor.listar()) {
                    gestor.eliminar(insumo.getCodigo());
                }
                for (InsumoPC insumoRecuperado : listaProductos) {
                    gestor.agregar(insumoRecuperado);
                }

                productos.setAll(gestor.listar());
            } catch (IOException | ClassNotFoundException e) {
                mostrarError("Error al cargar", e.getMessage());
            }
        });

        // BOTON GUARDAR INVENTARIO JSON
        Button botonGuardarInventarioJSON = new Button("Guardar inventario JSON");
        botonGuardarInventarioJSON.setOnAction(evento -> {
            try {
                PersistenciaJSON.guardarJSON(gestor.listar(), "inventario.json");
            } catch (IOException e) {
                mostrarError("Error al guardar", e.getMessage());
            }
        });

        // BOTON CARGAR INVENTARIO JSON
        Button botonCargarInventarioJSON = new Button("Cargar inventario JSON");
        botonCargarInventarioJSON.setOnAction(evento -> {
            try {
                List<InsumoPC> listaProductos = PersistenciaJSON.cargarJSON("inventario.json");

                for (InsumoPC insumo : gestor.listar()) {
                    gestor.eliminar(insumo.getCodigo());
                }
                for (InsumoPC insumoRecuperado : listaProductos) {
                    gestor.agregar(insumoRecuperado);
                }

                productos.setAll(gestor.listar());
            } catch (IOException e) {
                mostrarError("Error al cargar", e.getMessage());
            }
        });

        // BOTON GUARDAR INVENTARIO CSV
        Button botonGuardarInventarioCSV = new Button("Guardar inventario CSV");
        botonGuardarInventarioCSV.setOnAction(evento -> {
            try {
                PersistenciaCSV.guardarCSV(gestor.listar(), "inventario.csv");
            } catch (IOException e) {
                mostrarError("Error al guardar", e.getMessage());
            }
        });

        // BOTON CARGAR INVENTARIO CSV
        Button botonCargarInventarioCSV = new Button("Cargar inventario CSV");
        botonCargarInventarioCSV.setOnAction(evento -> {
            try {
                List<InsumoPC> listaProductos = PersistenciaCSV.cargarCSV("inventario.csv");

                for (InsumoPC insumo : gestor.listar()) {
                    gestor.eliminar(insumo.getCodigo());
                }
                for (InsumoPC insumoRecuperado : listaProductos) {
                    gestor.agregar(insumoRecuperado);
                }

                productos.setAll(gestor.listar());
            } catch (IOException e) {
                mostrarError("Error al cargar", e.getMessage());
            }
        });

        // BOTON EXPORTAR TXT
        Button botonExportarInventarioTXT = new Button("Exportar inventario TXT");
        botonExportarInventarioTXT.setOnAction(evento -> {
            try {
                PersistenciaTXT.exportarTXT(productos, "reporte_inventario.txt");
            } catch (IOException e) {
                mostrarError("Error al exportar", e.getMessage());
            }
        });

        // 5. BOTÓN AGREGAR
        Button botonAgregar = new Button("Agregar producto");

        botonAgregar.setOnAction(evento ->
                mostrarFormularioAgregar(gestor, productos));

        // BOTÓN ELIMINAR
        Button botonEliminar = new Button("Eliminar producto");

        botonEliminar.setOnAction(evento -> {
            InsumoPC seleccionado = tabla.getSelectionModel().getSelectedItem();

            if (seleccionado == null) {
                mostrarError(
                    "Sin selección",
                    "Seleccioná un producto de la tabla para eliminarlo."
                );
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Querés eliminar este producto?");
            confirmacion.setContentText(
                "Código: " + seleccionado.getCodigo()
                + "\nModelo: " + seleccionado.getModelo()
            );

            Optional<ButtonType> respuesta = confirmacion.showAndWait();

            if(respuesta.isPresent() && respuesta.get() == ButtonType.OK){
                gestor.eliminar(seleccionado.getCodigo());
                productos.setAll(gestor.listar());
            }

        });

        // BOTON EDITAR
        Button botonModificar = new Button("Modificar producto");

        botonModificar.setOnAction(evento -> {
            InsumoPC seleccionado = tabla.getSelectionModel().getSelectedItem();

            if(seleccionado == null){
                mostrarError("Sin elección", "Selecciona un producto para modificarlo");
                return;
            }

            mostrarFormularioModificar(seleccionado, gestor, productos);
        });

        // 6. VENTANA PRINCIPAL
        VBox contenedorPrincipal = new VBox(
            12,
            titulo,
            tabla,

            // CRUD
            botonAgregar,
            botonModificar,
            botonEliminar,

            // FILTROS
            campoPrecioMaximo,
            botonFiltrar,
            botonQuitarFiltro,

            // ORDENAMIENTOS
            botonOrdenarCodigo,
            botonOrdenarPrecio,
            botonOrdenarMarca,

            // DAT
            botonGuardarInventarioDAT,
            botonCargarInventarioDAT,

            // JSON
            botonGuardarInventarioJSON,
            botonCargarInventarioJSON,

            // CSV
            botonGuardarInventarioCSV,
            botonCargarInventarioCSV,

            // TXT
            botonExportarInventarioTXT
        );

        // 7. ORGANIZAR LA INTERFAZ

        // Liberar los controles para distribuirlos en nuevos contenedores
        contenedorPrincipal.getChildren().clear();

        // GESTIÓN DE PRODUCTOS
        HBox filaProductos = new HBox(
            10,
            botonAgregar,
            botonModificar,
            botonEliminar
        );

        // FILTROS
        campoPrecioMaximo.setPrefWidth(140);

        HBox filaFiltros = new HBox(
            10,
            campoPrecioMaximo,
            botonFiltrar,
            botonQuitarFiltro
        );

        // ORDENAMIENTOS
        HBox filaOrdenamientos = new HBox(
            10,
            botonOrdenarCodigo,
            botonOrdenarPrecio,
            botonOrdenarMarca
        );

        // ARCHIVOS DAT
        HBox filaDAT = new HBox(
            10,
            botonGuardarInventarioDAT,
            botonCargarInventarioDAT
        );

        // ARCHIVOS JSON
        HBox filaJSON = new HBox(
            10,
            botonGuardarInventarioJSON,
            botonCargarInventarioJSON
        );

        // ARCHIVOS CSV
        HBox filaCSV = new HBox(
            10,
            botonGuardarInventarioCSV,
            botonCargarInventarioCSV
        );

        // PANEL DERECHO: CONTROLES ORGANIZADOS
        VBox panelControles = new VBox(
            14,

            new Label("GESTIÓN DE PRODUCTOS"),
            filaProductos,

            new Label("FILTRAR INVENTARIO"),
            filaFiltros,

            new Label("ORDENAR INVENTARIO"),
            filaOrdenamientos,

            new Label("ARCHIVOS DAT"),
            filaDAT,

            new Label("ARCHIVOS JSON"),
            filaJSON,

            new Label("ARCHIVOS CSV"),
            filaCSV,

            new Label("EXPORTACIÓN TXT"),
            botonExportarInventarioTXT
        );

        panelControles.setPadding(new Insets(15));

        // Permite desplazarse si los controles no entran
        ScrollPane scrollControles = new ScrollPane(panelControles);
        scrollControles.setFitToWidth(true);
        scrollControles.setPrefWidth(470);
        scrollControles.setMinWidth(400);

        // TABLA A LA IZQUIERDA - CONTROLES A LA DERECHA
        HBox zonaPrincipal = new HBox(
            18,
            tabla,
            scrollControles
        );

        // La tabla ocupa el espacio horizontal disponible
        tabla.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabla, Priority.ALWAYS);

        // La zona principal ocupa el espacio vertical disponible
        VBox.setVgrow(zonaPrincipal, Priority.ALWAYS);

        // CONFIGURACIÓN FINAL
        titulo.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        contenedorPrincipal.setSpacing(15);
        contenedorPrincipal.setPadding(new Insets(20));

        contenedorPrincipal.getChildren().addAll(
            titulo,
            zonaPrincipal
        );

        // VENTANA
        Scene escena = new Scene(contenedorPrincipal, 1200, 680);

ventana.setTitle("Inventario de insumos de PC");
ventana.setScene(escena);
ventana.show();
    }

    // FORMULARIO PARA AGREGAR PRODUCTOS
    private void mostrarFormularioAgregar(
            GestorInventario gestor,
            ObservableList<InsumoPC> productos) {

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Agregar producto");

        dialogo.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        // 1. CAMPOS COMUNES
        TextField campoCodigo = new TextField();
        campoCodigo.setPromptText("Código");

        TextField campoMarca = new TextField();
        campoMarca.setPromptText("Marca");

        TextField campoModelo = new TextField();
        campoModelo.setPromptText("Modelo");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Precio");

        TextField campoStock = new TextField();
        campoStock.setPromptText("Stock");

        // 2. CAMPOS DE MEMORIA RAM
        TextField campoCapacidad = new TextField();
        campoCapacidad.setPromptText("Capacidad (GB)");

        TextField campoVelocidad = new TextField();
        campoVelocidad.setPromptText("Velocidad (MT/s)");

        // 3. CAMPOS DE PLACA DE VIDEO
        TextField campoVram = new TextField();
        campoVram.setPromptText("VRAM (GB)");

        TextField campoConsumo = new TextField();
        campoConsumo.setPromptText("Consumo (W)");

        // 4. CAMPOS DE FUENTE
        TextField campoPotencia = new TextField();
        campoPotencia.setPromptText("Potencia (W)");

        ComboBox<CertificacionFuente> campoCertificacion =
                new ComboBox<>();

        campoCertificacion.getItems().addAll(
                CertificacionFuente.values()
        );

        campoCertificacion.setPromptText("Certificación");

        // 5. SELECTOR DE TIPO
        ComboBox<String> selectorTipo = new ComboBox<>();

        selectorTipo.getItems().addAll(
                "Memoria RAM",
                "Placa de video",
                "Fuente"
        );

        selectorTipo.setPromptText("Seleccioná el tipo de producto");

        // 6. CAMPOS DINÁMICOS
        VBox camposEspecificos = new VBox(10);

        selectorTipo.setOnAction(evento -> {
            String tipo = selectorTipo.getValue();

            camposEspecificos.getChildren().clear();

            if ("Memoria RAM".equals(tipo)) {
                camposEspecificos.getChildren().addAll(
                        campoCapacidad, campoVelocidad
                );

            } else if ("Placa de video".equals(tipo)) {
                camposEspecificos.getChildren().addAll(
                        campoVram, campoConsumo
                );

            } else if ("Fuente".equals(tipo)) {
                camposEspecificos.getChildren().addAll(
                        campoPotencia, campoCertificacion
                );
            }
        });

        // 7. CONTENEDOR DEL FORMULARIO
        VBox formulario = new VBox(
                10,
                selectorTipo,
                campoCodigo,
                campoMarca,
                campoModelo,
                campoPrecio,
                campoStock,
                camposEspecificos
        );

        ScrollPane scroll = new ScrollPane(formulario);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(300);

        dialogo.getDialogPane().setContent(scroll);

        // 8. VALIDAR Y GUARDAR ANTES DE CERRAR
        Node botonGuardar =
                dialogo.getDialogPane().lookupButton(ButtonType.OK);

        botonGuardar.addEventFilter(ActionEvent.ACTION, evento -> {

            try {
                String tipo = selectorTipo.getValue();

                // VALIDAR CAMPOS OBLIGATORIOS
                if (tipo == null
                        || campoCodigo.getText().isBlank()
                        || campoMarca.getText().isBlank()
                        || campoModelo.getText().isBlank()
                        || campoPrecio.getText().isBlank()
                        || campoStock.getText().isBlank()) {

                    throw new IllegalArgumentException(
                            "Todos los campos obligatorios deben completarse."
                    );
                }

                // CONVERTIR DATOS COMUNES
                int codigo = Integer.parseInt(
                        campoCodigo.getText().trim());

                String marca = campoMarca.getText().trim();
                String modelo = campoModelo.getText().trim();

                double precio = Double.parseDouble(
                        campoPrecio.getText().trim());

                int stock = Integer.parseInt(
                        campoStock.getText().trim());

                // VALIDAR VALORES COMUNES
                if (codigo <= 0 || precio < 0 || stock < 0
                        || !Double.isFinite(precio)) {

                    throw new IllegalArgumentException(
                            "El código debe ser positivo y el precio "
                            + "y stock no pueden ser negativos."
                    );
                }

                // CREAR EL PRODUCTO SEGÚN SU TIPO
                InsumoPC nuevoProducto;

                if ("Memoria RAM".equals(tipo)) {

                    int capacidad = Integer.parseInt(
                            campoCapacidad.getText().trim());

                    int velocidad = Integer.parseInt(
                            campoVelocidad.getText().trim());

                    if (capacidad <= 0 || velocidad <= 0) {
                        throw new IllegalArgumentException(
                                "La capacidad y velocidad deben ser mayores que cero."
                        );
                    }

                    nuevoProducto = new MemoriaRAM(
                            codigo, marca, modelo, precio, stock,
                            capacidad, velocidad
                    );

                } else if ("Placa de video".equals(tipo)) {

                    int vram = Integer.parseInt(
                            campoVram.getText().trim());

                    int consumo = Integer.parseInt(
                            campoConsumo.getText().trim());

                    if (vram <= 0 || consumo <= 0) {
                        throw new IllegalArgumentException(
                                "La VRAM y el consumo deben ser mayores que cero."
                        );
                    }

                    nuevoProducto = new PlacaVideo(
                            codigo, marca, modelo, precio, stock,
                            vram, consumo
                    );

                } else if ("Fuente".equals(tipo)) {

                    int potencia = Integer.parseInt(
                            campoPotencia.getText().trim());

                    CertificacionFuente certificacion =
                            campoCertificacion.getValue();

                    if (potencia <= 0 || certificacion == null) {
                        throw new IllegalArgumentException(
                                "La potencia debe ser mayor que cero "
                                + "y la certificación es obligatoria."
                        );
                    }

                    nuevoProducto = new Fuente(
                            codigo, marca, modelo, precio, stock,
                            potencia, certificacion
                    );

                } else {
                    throw new IllegalArgumentException(
                            "El tipo de producto no corresponde."
                    );
                }

                // AGREGAR AL GESTOR Y ACTUALIZAR LA TABLA
                gestor.agregar(nuevoProducto);
                productos.setAll(gestor.listar());

            } catch (NumberFormatException e) {
                evento.consume();
                mostrarError(
                        "Datos incorrectos",
                        "Los campos numéricos deben contener números válidos."
                );

            } catch (CodigoDuplicadoException e) {
                evento.consume();
                mostrarError(
                        "Código duplicado",
                        "Ya existe un producto con ese código."
                );

            } catch (IllegalArgumentException e) {
                evento.consume();
                mostrarError("Datos incorrectos", e.getMessage());
            }
        });

        // 9. MOSTRAR EL FORMULARIO
        dialogo.showAndWait();
    }

    // MÉTODO AUXILIAR PARA MOSTRAR ERRORES
    private void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // METODO PARA MOSTRAR FORMULARIO EDICIÓN
    private void mostrarFormularioModificar(InsumoPC seleccionado, GestorInventario gestor, ObservableList<InsumoPC> productos) {

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Modificar producto");

        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // CAMPOS COMUNES
        TextField campoCodigo = new TextField(String.valueOf(seleccionado.getCodigo()));
        campoCodigo.setEditable(false);
        TextField campoMarca = new TextField(seleccionado.getMarca());
        TextField campoModelo = new TextField(seleccionado.getModelo());
        TextField campoPrecio = new TextField(String.valueOf(seleccionado.getPrecio()));
        TextField campoStock = new TextField(String.valueOf(seleccionado.consultarStock()));

        // CAMPOS ESPECIFICOS
        TextField campoCapacidad = new TextField();
        TextField campoVelocidad = new TextField();

        TextField campoVram = new TextField();
        TextField campoConsumo = new TextField();

        TextField campoPotencia = new TextField();
        ComboBox<CertificacionFuente> campoCertificacion = new ComboBox<>();
        campoCertificacion.getItems().addAll(CertificacionFuente.values());

        VBox camposEspecificos = new VBox(10);

        if (seleccionado instanceof MemoriaRAM memoria) {
            campoCapacidad.setText(
                String.valueOf(memoria.getCapacidad())
            );
            campoVelocidad.setText(
                String.valueOf(memoria.getVelocidad())
            );
            camposEspecificos.getChildren().addAll(
                new Label("Capacidad (GB)"),
                campoCapacidad,
                new Label("Velocidad (MT/s)"),
                campoVelocidad
            );
        }else if (seleccionado instanceof PlacaVideo placa){
            campoVram.setText(
                String.valueOf(placa.getVram())
            );
            campoConsumo.setText(
                String.valueOf(placa.getConsumo())
            );
            camposEspecificos.getChildren().addAll(
                new Label("VRAM (GB)"),
                campoCapacidad,
                new Label("Consumo (W)"),
                campoVelocidad
            );
        }else if (seleccionado instanceof Fuente fuente){
            campoPotencia.setText(
                String.valueOf(fuente.getPotencia())
            );
            campoCertificacion.setValue(
                fuente.getCertificacion()
            );
            camposEspecificos.getChildren().addAll(
                new Label("Potencia (W)"),
                campoCapacidad,
                new Label("Certificación"),
                campoVelocidad
            );
        }

        // ORGANIZAR FORMULARIO
        VBox formulario = new VBox(
            10,
            new Label("Código"), campoCodigo,
            new Label("Marca"), campoMarca,
            new Label("Modelo"), campoModelo,
            new Label("Precio"), campoPrecio,
            new Label("Stock"), campoStock,
            camposEspecificos
        );

        ScrollPane scroll = new ScrollPane(formulario);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(300);

        dialogo.getDialogPane().setContent(scroll);

        Node botonGuardar = dialogo.getDialogPane().lookupButton(ButtonType.OK);

        botonGuardar.addEventFilter(ActionEvent.ACTION, evento -> {
            try {
                // 1. RECUPERAR ATRIBUTOS COMUNES
                int codigo = seleccionado.getCodigo();
                String marca = campoMarca.getText().trim();
                String modelo = campoModelo.getText().trim();
                double precio = Double.parseDouble(
                    campoPrecio.getText().trim()
                );
                int stock = Integer.parseInt(
                    campoStock.getText().trim()
                );

                // 2. VALIDAR ATRIBUTOS COMUNES
                if (marca.isBlank() || modelo.isBlank()) {
                    throw new IllegalArgumentException(
                        "Marca y modelo son obligatorios."
                    );
                }
                if (precio < 0 || stock < 0 || !Double.isFinite(precio)) {
                    throw new IllegalArgumentException(
                        "El precio y el stock no pueden ser negativos."
                    );
                }

                // 3. CONSTRUIR EL PRODUCTO MODIFICADO
                InsumoPC productoModificado;
                if (seleccionado instanceof MemoriaRAM) {
                    int capacidad = Integer.parseInt(
                        campoCapacidad.getText().trim()
                    );
                    int velocidad = Integer.parseInt(
                        campoVelocidad.getText().trim()
                    );
                    if (capacidad <= 0 || velocidad <= 0) {
                        throw new IllegalArgumentException(
                            "La capacidad y la velocidad deben ser mayores que cero."
                        );
                    }
                    productoModificado = new MemoriaRAM(
                        codigo, marca, modelo, precio, stock,
                        capacidad, velocidad
                    );
                } else if (seleccionado instanceof PlacaVideo) {
                    int vram = Integer.parseInt(
                        campoVram.getText().trim()
                    );
                    int consumo = Integer.parseInt(
                        campoConsumo.getText().trim()
                    );
                    if (vram <= 0 || consumo <= 0) {
                        throw new IllegalArgumentException(
                            "La VRAM y el consumo deben ser mayores que cero."
                        );
                    }
                    productoModificado = new PlacaVideo(
                        codigo, marca, modelo, precio, stock,
                        vram, consumo
                    );
                } else if (seleccionado instanceof Fuente) {
                    int potencia = Integer.parseInt(
                        campoPotencia.getText().trim()
                    );
                    CertificacionFuente certificacion =
                        campoCertificacion.getValue();
                    if (potencia <= 0 || certificacion == null) {
                        throw new IllegalArgumentException(
                            "La potencia debe ser mayor que cero "
                            + "y la certificación es obligatoria."
                        );
                    }
                    productoModificado = new Fuente(
                        codigo, marca, modelo, precio, stock,
                        potencia, certificacion
                    );
                } else {
                    throw new IllegalArgumentException(
                        "Tipo de producto no soportado."
                    );
                }

                gestor.actualizar(codigo, productoModificado);
                productos.setAll(gestor.listar());

            } catch (NumberFormatException e) {
                evento.consume();
                mostrarError(
                    "Datos incorrectos",
                    "Los campos numéricos deben contener números válidos."
                );
            } catch (IllegalArgumentException e) {
                evento.consume();
                mostrarError(
                    "Datos incorrectos",
                    e.getMessage()
                );
            }
        });

        // MOSTRAR FORMULARIO
        dialogo.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
