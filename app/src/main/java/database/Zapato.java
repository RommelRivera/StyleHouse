package database;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class Zapato {
    private SQLiteDatabase database;
    private int idZapato;
    private Estilo estilo;
    private String nombre;
    private String marca;
    private double precio;
    private String talla;
    private String sucursal;
    private Drawable imagen;
    private String descripcion;

    public Zapato(int idZapato, Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable imagen, String descripcion) {
        this.idZapato = idZapato;
        this.estilo = estilo;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.talla = talla;
        this.sucursal = sucursal;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public int getIdZapato() {
        return idZapato;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
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

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getSucursal() {
        return sucursal;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}