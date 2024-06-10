package database;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;

public class Accesorio {
    private SQLiteDatabase database;
    private int idAccesorio;
    private Estilo estilo;
    private String nombre;
    private String marca;
    private double precio;
    private String sucursal;
    private Drawable imagen;
    private String descripcion;

    public Accesorio(int idAccesorio, Estilo estilo, String nombre, String marca, double precio, String sucursal, Drawable imagen, String descripcion) {
        this.idAccesorio = idAccesorio;
        this.estilo = estilo;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.sucursal = sucursal;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public int getIdAccesorio() {
        return idAccesorio;
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
