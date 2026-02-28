package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Taller {

    private final String nombre;
    private final List<Mecanico> mecanicos;
    private final List<Cliente> clientes;
    private final List<OrdenServicio> ordenesServicio;
    private final List<Repuesto> repuestos;
    private int siguienteIdOrden;
    // El constructor
    public Taller(String nombre) {
        this.nombre = nombre == null ? "" : nombre.trim();
        this.mecanicos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.ordenesServicio = new ArrayList<>();
        this.repuestos = new ArrayList<>();
        this.siguienteIdOrden = 1;
    }
    //Los gets
    public String getNombre() {
        return nombre;
    }

    public List<Mecanico> getMecanicos() {
        return mecanicos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<OrdenServicio> getOrdenesServicio() {
        return ordenesServicio;
    }

    public List<Repuesto> getRepuestos() {
        return repuestos;
    }
    //Metodo que registra a los clientes
    public void registrarCliente(String nombre, String id, String telefono, String direccion) {
        if (buscarClientePorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con ID " + id);
        }
        clientes.add(new Cliente(nombre, id, telefono, direccion));
    }
    //Metodo que registra a las bicicletas
    public void registrarBicicleta(String idCliente, String marca,
                                   TipoBicicleta tipo, String color,
                                   String numeroSerial, int anio) {

        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe el cliente con ID " + idCliente);
        }

        if (buscarBicicletaPorSerial(numeroSerial) != null) {
            throw new IllegalArgumentException("Ya existe una bicicleta con serial " + numeroSerial);
        }

        Bicicleta bicicleta = new Bicicleta(cliente, marca, tipo, color, numeroSerial, anio);
        cliente.agregarBicicleta(bicicleta);
    }

    //Metodo que registra a los mecanicos
    public void registrarMecanico(String nombre, EspecialidadMecanico especialidad, int numeroCertificacion) {
        if (buscarMecanicoPorCertificacion(numeroCertificacion) != null) {
            throw new IllegalArgumentException("Ya existe un mecanico con certificacion " + numeroCertificacion);
        }
        mecanicos.add(new Mecanico(nombre, especialidad, numeroCertificacion));
    }

    //Metodo que registra los repuestos
    public void registrarRepuesto(int id, String nombre, int cantidadDisponible, int stockMinimo) {
        if (buscarRepuestoPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un repuesto con ID " + id);
        }
        repuestos.add(new Repuesto(id, nombre, cantidadDisponible, stockMinimo));
    }

    //Metodo que crea una orden
    public void crearOrden(String serialBicicleta,
                           int certificacionMecanico,
                           String motivoServicio,
                           LocalDate fechaIngreso,
                           LocalTime hora,
                           String diagnostico,
                           String trabajosRealizados,
                           double costoTotal,
                           int idRepuesto,
                           int cantidadRepuesto) {

        if (fechaIngreso == null || hora == null) {
            throw new IllegalArgumentException("Fecha y hora obligatorias");
        }

        Bicicleta bicicleta = buscarBicicletaPorSerial(serialBicicleta);
        if (bicicleta == null) {
            throw new IllegalArgumentException("No existe bicicleta con serial " + serialBicicleta);
        }

        Mecanico mecanico = buscarMecanicoPorCertificacion(certificacionMecanico);
        if (mecanico == null) {
            throw new IllegalArgumentException("No existe mecanico con certificacion " + certificacionMecanico);
        }

        Repuesto repuesto = buscarRepuestoPorId(idRepuesto);
        if (repuesto == null) {
            throw new IllegalArgumentException("No existe el repuesto ID " + idRepuesto);
        }
        if (cantidadRepuesto <= 0) {
            throw new IllegalArgumentException("La cantidad de repuesto debe ser mayor a 0");
        }
        if (repuesto.getCantidadDisponible() < cantidadRepuesto) {
            throw new IllegalArgumentException("Stock insuficiente para " + repuesto.getNombre());
        }

        OrdenServicio orden = new OrdenServicio(
                siguienteIdOrden++,
                fechaIngreso,
                hora,
                bicicleta,
                mecanico,
                motivoServicio
        );

        orden.setDiagnostico(diagnostico);
        orden.setTrabajosRealizados(trabajosRealizados);
        orden.setCostoTotal(costoTotal);
        orden.agregarRepuesto(repuesto, cantidadRepuesto);

        ordenesServicio.add(orden);
        bicicleta.agregarOrdenServicio(orden);
        mecanico.agregarOrden(orden);
    }

    //Metodo que permite ver las bicicletas que hay en el inventario
    public List<OrdenServicio> verHistorialServiciosBicicleta(String numeroSerial) {
        Bicicleta bicicleta = buscarBicicletaPorSerial(numeroSerial);
        if (bicicleta == null) {
            throw new IllegalArgumentException("No existe bicicleta con serial " + numeroSerial);
        }
        return bicicleta.getOrdenesServicio();
    }

    //Metodo paa consultar la fecha que se hizo la orden
    public List<OrdenServicio> consultarOrden(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null");
        }

        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio orden : ordenesServicio) {
            if (orden.getFechaIngreso().equals(fecha)) {
                resultado.add(orden);
            }
        }
        return resultado;
    }

    //Metodo que verifica si hay poco stock en el taller
    public List<Repuesto> verificarStockBajo() {
        List<Repuesto> resultado = new ArrayList<>();
        for (Repuesto repuesto : repuestos) {
            if (repuesto.stockBajo()) {
                resultado.add(repuesto);
            }
        }
        return resultado;
    }

    //Metodo que avisa cuando hay poco stock
    public String mostrarAlertasStock() {
        List<Repuesto> bajos = verificarStockBajo();

        if (bajos.isEmpty()) {
            return "Sin alertas de stock";
        }

        String resultado = "";
        for (Repuesto repuesto : bajos) {
            resultado += "Alerta: " + repuesto.getNombre() +
                    " con stock " + repuesto.getCantidadDisponible() + "\n";
        }

        return resultado.trim();
    }

    //Metodo que busca a un cliente por si ID
    public Cliente buscarClientePorId(String id) {
        if (id == null) return null;

        for (Cliente cliente : clientes) {
            if (cliente.getId().equalsIgnoreCase(id.trim())) {
                return cliente;
            }
        }
        return null;
    }

    //Metodo que busca a una bicicleta por serial
    public Bicicleta buscarBicicletaPorSerial(String serial) {
        if (serial == null) return null;

        for (Cliente cliente : clientes) {
            for (Bicicleta bicicleta : cliente.getBicicletas()) {
                if (bicicleta.getNumeroSerial().equalsIgnoreCase(serial.trim())) {
                    return bicicleta;
                }
            }
        }
        return null;
    }

    //Metodo que busca a un mecanico por certificacion
    public Mecanico buscarMecanicoPorCertificacion(int certificacion) {
        for (Mecanico mecanico : mecanicos) {
            if (mecanico.getNumeroCertificacion() == certificacion) {
                return mecanico;
            }
        }
        return null;
    }

    //Metodo que busca un repuesto por ID
    public Repuesto buscarRepuestoPorId(int id) {
        for (Repuesto repuesto : repuestos) {
            if (repuesto.getId() == id) {
                return repuesto;
            }
        }
        return null;
    }

    //Metodo que busca una orden por ID
    public OrdenServicio buscarOrdenPorId(int idOrden) {
        for (OrdenServicio orden : ordenesServicio) {
            if (orden.getId() == idOrden) {
                return orden;
            }
        }
        return null;
    }
}