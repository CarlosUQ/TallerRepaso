package model;

import java.util.ArrayList;
import java.util.List;

public class Bicicleta {
    private final Cliente dueno;
    private final String marca;
    private final TipoBicicleta tipo;
    private final String color;
    private final String numeroSerial;
    private final int anio;
    private final List<OrdenServicio> ordenesServicio;

    public Bicicleta(Cliente dueno, String marca, TipoBicicleta tipo, String color, String numeroSerial, int anio) {
        this.dueno = dueno;
        this.marca = marca;
        this.tipo = tipo;
        this.color = color;
        this.numeroSerial = numeroSerial;
        this.anio = anio;
        this.ordenesServicio = new ArrayList<>();
    }

    public Cliente getDueno() {
        return dueno;
    }

    public String getMarca() {
        return marca;
    }

    public TipoBicicleta getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }

    public String getNumeroSerial() {
        return numeroSerial;
    }

    public int getAnio() {
        return anio;
    }

    public List<OrdenServicio> getOrdenesServicio() {
        return ordenesServicio;
    }

    public void agregarOrdenServicio(OrdenServicio ordenServicio) {
        ordenesServicio.add(ordenServicio);
    }

    @Override
    public String toString() {
        return numeroSerial + " - " + marca + " (" + tipo + ")";
    }
}
