package model;

public class Repuesto {
    private final int id;
    private final String nombre;
    private int cantidadDisponible;
    private final int stockMinimo;

    //Constructor
    public Repuesto(int id, String nombre, int cantidadDisponible, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
    }

    //gets
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    //Metodo que resta la cantidad de stock que se usa
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (cantidad > cantidadDisponible) {
            throw new IllegalArgumentException("Stock insuficiente para " + nombre);
        }
        cantidadDisponible -= cantidad;
    }

    public boolean stockBajo() {
        return cantidadDisponible <= stockMinimo;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (Stock: " + cantidadDisponible + ")";
    }

}
