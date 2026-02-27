package model;

import java.util.ArrayList;
import java.util.List;

public class Mecanico {
    private final String nombre;
    private final EspecialidadMecanico especialidad;
    private final int numeroCertificacion;
    private final List<OrdenServicio> ordenes;

    public Mecanico(String nombre, EspecialidadMecanico especialidad, int numeroCertificacion) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.numeroCertificacion = numeroCertificacion;
        this.ordenes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public EspecialidadMecanico getEspecialidad() {
        return especialidad;
    }

    public int getNumeroCertificacion() {
        return numeroCertificacion;
    }

    public List<OrdenServicio> getOrdenes() {
        return ordenes;
    }

    public void agregarOrden(OrdenServicio ordenServicio) {
        ordenes.add(ordenServicio);
    }

    @Override
    public String toString() {
        return numeroCertificacion + " - " + nombre + " (" + especialidad + ")";
    }
}
