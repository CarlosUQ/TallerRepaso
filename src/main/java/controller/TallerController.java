package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TallerController {

    private final Taller taller = new Taller("Taller PGII");

    @FXML
    private TextField tfClienteNombre;
    @FXML
    private TextField tfClienteId;
    @FXML
    private TextField tfClienteTelefono;
    @FXML
    private TextField tfClienteDireccion;
    @FXML
    private Button btnRegistrarCliente;

    @FXML
    private TextField tfBiciIdCliente;
    @FXML
    private TextField tfBiciMarca;
    @FXML
    private ComboBox<TipoBicicleta> cbBiciTipo;
    @FXML
    private TextField tfBiciColor;
    @FXML
    private TextField tfBiciSerial;
    @FXML
    private TextField tfBiciAnio;
    @FXML
    private Button btnRegistrarBici;

    @FXML
    private TextField tfMecanicoNombre;
    @FXML
    private ComboBox<EspecialidadMecanico> cbMecanicoEspecialidad;
    @FXML
    private TextField tfMecanicoCertificacion;
    @FXML
    private Button btnRegistrarMecanico;

    @FXML
    private TextField tfRepuestoId;
    @FXML
    private TextField tfRepuestoNombre;
    @FXML
    private TextField tfRepuestoCantidad;
    @FXML
    private TextField tfRepuestoStockMin;
    @FXML
    private Button btnRegistrarRepuesto;

    @FXML
    private ComboBox<Bicicleta> cbBicicletaOrden;
    @FXML
    private ComboBox<Mecanico> cbMecanicoOrden;
    @FXML
    private TextField tfOrdenMotivo;
    @FXML
    private TextField tfOrdenDiagnostico;
    @FXML
    private TextField tfOrdenTrabajos;
    @FXML
    private TextField tfOrdenCosto;
    @FXML
    private DatePicker dpOrdenFecha;
    @FXML
    private TextField tfOrdenHora;
    @FXML
    private Button btnCrearOrden;

    @FXML
    private ComboBox<Repuesto> cbRepuestoOrden;
    @FXML
    private TextField tfOrdenRepuestoCantidad;

    @FXML
    private TextField tfConsultaSerial;
    @FXML
    private Button btnVerHistorial;
    @FXML
    private DatePicker dpConsultaFecha;
    @FXML
    private Button btnConsultarFecha;

    @FXML
    private TextArea taClientes;
    @FXML
    private TextArea taMecanicosRepuestos;
    @FXML
    private TextArea taOrdenes;
    @FXML
    private TextArea taHistorial;
    @FXML
    private TextArea taConsultaFecha;
    @FXML
    private TextArea taAlertasStock;

    @FXML
    private void initialize() {
        cbBiciTipo.getItems().setAll(TipoBicicleta.values());
        cbMecanicoEspecialidad.getItems().setAll(EspecialidadMecanico.values());

        if (dpOrdenFecha.getValue() == null) {
            dpOrdenFecha.setValue(LocalDate.now());
        }
        if (dpConsultaFecha.getValue() == null) {
            dpConsultaFecha.setValue(LocalDate.now());
        }
        if (tfOrdenHora.getText() == null || tfOrdenHora.getText().isBlank()) {
            tfOrdenHora.setText(LocalTime.now().withSecond(0).withNano(0).toString());
        }

        configurarEventos();
        refrescarDatos();
    }

    private void configurarEventos() {
        btnRegistrarCliente.setOnAction(e -> ejecutarSeguro(this::registrarCliente));
        btnRegistrarBici.setOnAction(e -> ejecutarSeguro(this::registrarBicicleta));
        btnRegistrarMecanico.setOnAction(e -> ejecutarSeguro(this::registrarMecanico));
        btnRegistrarRepuesto.setOnAction(e -> ejecutarSeguro(this::registrarRepuesto));
        btnCrearOrden.setOnAction(e -> ejecutarSeguro(this::crearOrden));
        btnVerHistorial.setOnAction(e -> ejecutarSeguro(this::verHistorial));
        btnConsultarFecha.setOnAction(e -> ejecutarSeguro(this::consultarPorFecha));
    }

    private void registrarCliente() {
        taller.registrarCliente(
                tfClienteNombre.getText(),
                tfClienteId.getText(),
                tfClienteTelefono.getText(),
                tfClienteDireccion.getText()
        );

        limpiarCampos(tfClienteNombre, tfClienteId, tfClienteTelefono, tfClienteDireccion);
        refrescarDatos();
    }

    private void registrarBicicleta() {
        int anio = Integer.parseInt(tfBiciAnio.getText().trim());

        taller.registrarBicicleta(
                tfBiciIdCliente.getText(),
                tfBiciMarca.getText(),
                cbBiciTipo.getValue(),
                tfBiciColor.getText(),
                tfBiciSerial.getText(),
                anio
        );

        limpiarCampos(tfBiciIdCliente, tfBiciMarca, tfBiciColor, tfBiciSerial, tfBiciAnio);
        cbBiciTipo.getSelectionModel().clearSelection();
        refrescarDatos();
    }

    private void registrarMecanico() {
        int cert = Integer.parseInt(tfMecanicoCertificacion.getText().trim());

        taller.registrarMecanico(
                tfMecanicoNombre.getText(),
                cbMecanicoEspecialidad.getValue(),
                cert
        );

        limpiarCampos(tfMecanicoNombre, tfMecanicoCertificacion);
        cbMecanicoEspecialidad.getSelectionModel().clearSelection();
        refrescarDatos();
    }

    private void registrarRepuesto() {
        int id = Integer.parseInt(tfRepuestoId.getText().trim());
        int cantidad = Integer.parseInt(tfRepuestoCantidad.getText().trim());
        int stockMin = Integer.parseInt(tfRepuestoStockMin.getText().trim());

        taller.registrarRepuesto(id, tfRepuestoNombre.getText(), cantidad, stockMin);

        limpiarCampos(tfRepuestoId, tfRepuestoNombre, tfRepuestoCantidad, tfRepuestoStockMin);
        refrescarDatos();
    }

    private void crearOrden() {
        Bicicleta bicicleta = cbBicicletaOrden.getValue();
        Mecanico mecanico = cbMecanicoOrden.getValue();
        Repuesto repuesto = cbRepuestoOrden.getValue();

        if (bicicleta == null || mecanico == null || repuesto == null) {
            throw new IllegalArgumentException("Debes seleccionar bicicleta, mecanico y repuesto");
        }

        double costo = Double.parseDouble(tfOrdenCosto.getText().trim());
        int cantidadRepuesto = Integer.parseInt(tfOrdenRepuestoCantidad.getText().trim());
        LocalTime hora = parseHora(tfOrdenHora.getText());

        taller.crearOrden(
                bicicleta.getNumeroSerial(),
                mecanico.getNumeroCertificacion(),
                tfOrdenMotivo.getText(),
                dpOrdenFecha.getValue(),
                hora,
                tfOrdenDiagnostico.getText(),
                tfOrdenTrabajos.getText(),
                costo,
                repuesto.getId(),
                cantidadRepuesto
        );

        limpiarCampos(tfOrdenMotivo, tfOrdenDiagnostico, tfOrdenTrabajos, tfOrdenCosto, tfOrdenRepuestoCantidad);
        cbRepuestoOrden.getSelectionModel().clearSelection();
        refrescarDatos();
    }

    private void verHistorial() {
        List<OrdenServicio> historial = taller.verHistorialServiciosBicicleta(tfConsultaSerial.getText());
        taHistorial.setText(formatearOrdenes(historial));
    }

    private void consultarPorFecha() {
        List<OrdenServicio> ordenesFecha = taller.consultarOrden(dpConsultaFecha.getValue());
        taConsultaFecha.setText(formatearOrdenes(ordenesFecha));
    }

    private void refrescarDatos() {
        cbBicicletaOrden.getItems().setAll(obtenerBicicletas());
        cbMecanicoOrden.getItems().setAll(taller.getMecanicos());
        cbRepuestoOrden.getItems().setAll(taller.getRepuestos());

        refrescarResumenClientes();
        refrescarResumenMecanicosRepuestos();

        taOrdenes.setText(formatearOrdenes(taller.getOrdenesServicio()));
        taAlertasStock.setText(taller.mostrarAlertasStock());
    }

    private List<Bicicleta> obtenerBicicletas() {
        List<Bicicleta> lista = new ArrayList<>();
        for (Cliente cliente : taller.getClientes()) {
            lista.addAll(cliente.getBicicletas());
        }
        return lista;
    }

    private void refrescarResumenClientes() {
        StringBuilder sb = new StringBuilder();

        for (Cliente cliente : taller.getClientes()) {
            sb.append(cliente).append(System.lineSeparator());
            for (Bicicleta bicicleta : cliente.getBicicletas()) {
                sb.append("  - ").append(bicicleta).append(System.lineSeparator());
            }
        }

        if (sb.length() == 0) {
            sb.append("Sin clientes registrados.");
        }

        taClientes.setText(sb.toString());
    }

    private void refrescarResumenMecanicosRepuestos() {
        StringBuilder sb = new StringBuilder();
        sb.append("MECANICOS").append(System.lineSeparator());

        if (taller.getMecanicos().isEmpty()) {
            sb.append("Sin mecanicos registrados.").append(System.lineSeparator());
        } else {
            for (Mecanico mecanico : taller.getMecanicos()) {
                sb.append("- ").append(mecanico).append(System.lineSeparator());
            }
        }

        sb.append(System.lineSeparator()).append("REPUESTOS").append(System.lineSeparator());

        if (taller.getRepuestos().isEmpty()) {
            sb.append("Sin repuestos registrados.");
        } else {
            for (Repuesto repuesto : taller.getRepuestos()) {
                sb.append("- ").append(repuesto).append(System.lineSeparator());
            }
        }

        taMecanicosRepuestos.setText(sb.toString());
    }

    private String formatearOrdenes(List<OrdenServicio> ordenes) {
        if (ordenes == null || ordenes.isEmpty()) {
            return "Sin ordenes.";
        }

        StringBuilder sb = new StringBuilder();
        for (OrdenServicio orden : ordenes) {
            sb.append(orden.detalleBasico()).append(System.lineSeparator());

            if (!orden.getRepuestosUsados().isEmpty()) {
                sb.append("  Repuestos: ");
                for (Repuesto repuesto : orden.getRepuestosUsados().keySet()) {
                    int cantidad = orden.getRepuestosUsados().get(repuesto);
                    sb.append(repuesto.getNombre()).append(" x").append(cantidad).append("  ");
                }
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }

    private void ejecutarSeguro(Runnable accion) {
        try {
            accion.run();
        } catch (NumberFormatException ex) {
            mostrarError("Formato numerico invalido.");
        } catch (DateTimeParseException ex) {
            mostrarError("Formato de hora invalido. Usa HH:mm o HH:mm:ss");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private LocalTime parseHora(String horaTexto) {
        if (horaTexto == null || horaTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("La hora es obligatoria");
        }
        return LocalTime.parse(horaTexto.trim());
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No fue posible completar la operacion");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }
}
