package database;

import android.graphics.drawable.Drawable;

public class Producto {
    private int idProducto;
    private int tipoProducto;
    private String nombre;
    private String marca;
    private double precio;
    private Drawable imagen;

    public Producto(int idProducto, int tipoProducto, String nombre, String marca, double precio, Drawable imagen) {
        this.idProducto = idProducto;
        this.tipoProducto = tipoProducto;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
