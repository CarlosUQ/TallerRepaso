package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private final String nombre;
    private final String id;
    private final String telefono;
    private final String direccion;
    private final List<Bicicleta> bicicletas;

    public Cliente(String nombre, String id, String telefono, String direccion) {
        this.nombre = nombre;
        this.id = id;
        this.telefono = telefono;
        this.direccion = direccion;
        this.bicicletas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Bicicleta> getBicicletas() {
        return bicicletas;
    }

    public void agregarBicicleta(Bicicleta bicicleta) {
        bicicletas.add(bicicleta);
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
