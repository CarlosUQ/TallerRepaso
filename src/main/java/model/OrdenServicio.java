package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrdenServicio {

    private final int id;
    private final LocalDate fechaIngreso;
    private final LocalTime hora;
    private final Bicicleta bicicletaAtendida;
    private final Mecanico mecanicoResponsable;
    private final String motivoServicio;

    private String diagnostico;
    private String trabajosRealizados;
    private double costoTotal;

    private final Map<Repuesto, Integer> repuestosUsados;

    public OrdenServicio(int id, LocalDate fechaIngreso, LocalTime hora,
                         Bicicleta bicicletaAtendida,
                         Mecanico mecanicoResponsable,
                         String motivoServicio) {

        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.hora = hora;
        this.bicicletaAtendida = bicicletaAtendida;
        this.mecanicoResponsable = mecanicoResponsable;
        this.motivoServicio = motivoServicio;

        this.diagnostico = "";
        this.trabajosRealizados = "";
        this.costoTotal = 0;

        this.repuestosUsados = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public LocalTime getHora() {
        return hora;
    }

    public Bicicleta getBicicletaAtendida() {
        return bicicletaAtendida;
    }

    public Mecanico getMecanicoResponsable() {
        return mecanicoResponsable;
    }

    public String getMotivoServicio() {
        return motivoServicio;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = Objects.requireNonNullElse(diagnostico, "").trim();
    }

    public String getTrabajosRealizados() {
        return trabajosRealizados;
    }

    public void setTrabajosRealizados(String trabajosRealizados) {
        this.trabajosRealizados = Objects.requireNonNullElse(trabajosRealizados, "").trim();
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        if (costoTotal < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        this.costoTotal = costoTotal;
    }

    public Map<Repuesto, Integer> getRepuestosUsados() {
        return repuestosUsados;
    }

    public void agregarRepuesto(Repuesto repuesto, int cantidad) {

        if (repuesto == null || cantidad <= 0) {
            throw new IllegalArgumentException("Datos inválidos");
        }

        repuesto.descontarStock(cantidad);

        if (repuestosUsados.containsKey(repuesto)) {
            int cantidadActual = repuestosUsados.get(repuesto);
            repuestosUsados.put(repuesto, cantidadActual + cantidad);
        } else {
            repuestosUsados.put(repuesto, cantidad);
        }
    }

    public String detalleBasico() {
        return "Orden #" + id
                + " | Fecha: " + fechaIngreso
                + " " + hora
                + " | Bici: " + bicicletaAtendida.getNumeroSerial()
                + " | Mecanico: " + mecanicoResponsable.getNombre()
                + " | Motivo: " + motivoServicio
                + " | Costo: " + costoTotal;
    }

    @Override
    public String toString() {
        return "Orden #" + id
                + " - " + bicicletaAtendida.getNumeroSerial()
                + " (" + fechaIngreso + ")";
    }
}