package database;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class PedidoProducto {
    public static class TIPO_PRODUCTO {
        public static final int CAMISA = 1;
        public static final int PANTALON = 2;
        public static final int ZAPATO = 3;
        public static final int ACCESORIO = 4;
    }

    private SQLiteDatabase database;
    private int idPedidoProducto;
    private int idPedido;
    private int idProducto;
    private String nombre;
    private String marca;
    private double precio;
    private Drawable imagen;
    private int tipoProducto;

    public PedidoProducto(int idPedidoProducto, int idPedido, int tipoProducto, int idProducto, String nombre, String marca, double precio, Drawable imagen) {
        this.idPedidoProducto = idPedidoProducto;
        this.idPedido = idPedido;
        this.tipoProducto = tipoProducto;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.imagen = imagen;
    }

    public PedidoProducto(int idPedidoProducto, int idPedido, Camisa camisa) {
        this.idPedidoProducto = idPedidoProducto;
        this.idPedido = idPedido;
        this.tipoProducto = TIPO_PRODUCTO.CAMISA;
        this.idProducto = camisa.getIdCamisa();
        this.nombre = camisa.getNombre();
        this.marca = camisa.getMarca();
        this.precio = camisa.getPrecio();
        this.imagen = camisa.getImagen();
    }

    public PedidoProducto(int idPedidoProducto, int idPedido, Pantalon pantalon) {
        this.idPedidoProducto = idPedidoProducto;
        this.idPedido = idPedido;
        this.tipoProducto = TIPO_PRODUCTO.PANTALON;
        this.idProducto = pantalon.getIdPantalon();
        this.nombre = pantalon.getNombre();
        this.marca = pantalon.getMarca();
        this.precio = pantalon.getPrecio();
        this.imagen = pantalon.getImagen();
    }

    public PedidoProducto(int idPedidoProducto, int idPedido, Zapato zapato) {
        this.idPedidoProducto = idPedidoProducto;
        this.idPedido = idPedido;
        this.tipoProducto = TIPO_PRODUCTO.ZAPATO;
        this.idProducto = zapato.getIdZapato();
        this.nombre = zapato.getNombre();
        this.marca = zapato.getMarca();
        this.precio = zapato.getPrecio();
        this.imagen = zapato.getImagen();
    }

    public PedidoProducto(int idPedidoProducto, int idPedido, Accesorio accesorio) {
        this.idPedidoProducto = idPedidoProducto;
        this.idPedido = idPedido;
        this.tipoProducto = TIPO_PRODUCTO.ACCESORIO;
        this.idProducto = accesorio.getIdAccesorio();
        this.nombre = accesorio.getNombre();
        this.marca = accesorio.getMarca();
        this.precio = accesorio.getPrecio();
        this.imagen = accesorio.getImagen();
    }

    public void setDatabase(SQLiteDatabase database) { this.database = database; }

    public int getIdPedidoProducto() { return idPedidoProducto; }

    public int getIdPedido() { return idPedido; }

    public int getIdProducto() { return idProducto; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public double getPrecio() { return precio; }

    public Drawable getImagen() { return imagen; }

    public void setImagen(Drawable imagen) { this.imagen = imagen; }

    public void setPrecio(double precio) { this.precio = precio; }

    public int getTipoProducto() { return tipoProducto; }

    public void setTipoProducto(int tipoProducto) { this.tipoProducto = tipoProducto; }
}
