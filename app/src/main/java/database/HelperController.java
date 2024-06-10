package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HelperController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private Context context;

    public HelperController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.context = context;
    }

    //region Funciones Helpers
    public String[] getMarcas() {
        Cursor cursor = readDB.rawQuery("SELECT nombre FROM marcas;", null);
        String[] marcas = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            marcas[i] = cursor.getString(0);
        }

        cursor.close();

        return marcas;
    }

    public String getMarcaById(int idMarca) {
        Cursor cursor = readDB.rawQuery("SELECT nombre FROM marcas WHERE idMarca = ?;", new String[] { Integer.toString(idMarca) });
        cursor.moveToNext();

        String marca = cursor.getString(0);

        cursor.close();

        return marca;
    }

    public int getIdMarca(String marca) {
        Cursor cursor = readDB.rawQuery("SELECT idMarca FROM marcas WHERE nombre = ?;", new String[] { marca });
        cursor.moveToNext();

        int idMarca = cursor.getInt(0);

        cursor.close();

        return idMarca;
    }

    public String[] getSucursales() {
        Cursor cursor = readDB.rawQuery("SELECT nombre FROM sucursales;", null);
        String[] sucursales = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            sucursales[i] = cursor.getString(0);
        }

        cursor.close();

        return sucursales;
    }

    public String getSucursalById(int idSucursal) {
        Cursor cursor = readDB.rawQuery("SELECT nombre FROM sucursales WHERE idSucursal = ?;", new String[] { Integer.toString(idSucursal) });
        cursor.moveToNext();

        String sucursal = cursor.getString(0);

        cursor.close();

        return sucursal;
    }

    public int getIdSucursal(String sucursal) {
        Cursor cursor = readDB.rawQuery("SELECT idSucursal FROM sucursales WHERE nombre = ?;", new String[] { sucursal });
        cursor.moveToNext();

        int idSucursal = cursor.getInt(0);

        cursor.close();

        return idSucursal;
    }

    // Funciones para recuperar un objeto tipo Drawable
    public Drawable drawableFromBlob(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Drawable drawableFromIdDrawable(int idDrawable) {
        return drawableFromBlob(blobFromIdDrawable(idDrawable));
    }

    // Funciones para recuperar un objeto tipo byte[]
    public byte[] blobFromIdDrawable(int idDrawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), idDrawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public byte[] blobFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public int[] getIdPedidos(int idUsuario) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidos WHERE idUsuario = ? AND (estado = ? OR estado = ?);", new String[] { Integer.toString(idUsuario), "PENDIENTE", "FINALIZADO" });
        int[] pedidos = new int[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            pedidos[i] = cursor.getInt(0);
        }

        cursor.close();

        return pedidos;
    }

    public String[] getEstadoPedidos(int idUsuario) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidos WHERE idUsuario = ? AND (estado = ? OR estado = ?);", new String[] { Integer.toString(idUsuario), "PENDIENTE", "FINALIZADO" });
        String[] estados = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            estados[i] = cursor.getString(2);
        }

        cursor.close();

        return estados;
    }

    // Funcion para crear un nuevo Pedido
    public long newPedido(int idUsuario) {
        ContentValues values = new ContentValues();
        values.put("idUsuario", idUsuario);
        return writeDB.insert("pedidos", null, values);
    }

    public String getPedidoEstado(int idPedido) {
        Cursor cursor = readDB.rawQuery("SELECT estado FROM pedidos WHERE idPedido = ?;", new String[] { Integer.toString(idPedido) });
        cursor.moveToNext();

        String estado = cursor.getString(0);
        cursor.close();

        return estado;
    }

    public int setPedidoEstado(int idPedido, String estado) {
        ContentValues values = new ContentValues();
        values.put("estado", estado);

        return writeDB.update("pedidos", values, "idPedido = ?;", new String[] { Integer.toString(idPedido) });
    }

    public int setPedidoUsuario(int idPedido, int idUsuario) {
        ContentValues values = new ContentValues();
        values.put("idUsuario", idUsuario);

        return writeDB.update("pedidos", values, "idPedido = ?;", new String[] { Integer.toString(idPedido) });
    }

    public long insertMarca(String marca) {
        ContentValues values = new ContentValues();
        values.put("nombre", marca);

        return writeDB.insert("marcas", null, values);
    }

    public long insertSucursal(String sucursal) {
        ContentValues values = new ContentValues();
        values.put("nombre", sucursal);

        return writeDB.insert("sucursales", null, values);
    }

    // Función de apoyo para crear un objeto Producto con los datos obtenidos de la base de datos
    public Producto iteratorProducto(Cursor cursor, int tipoProducto) {
        int idProducto = cursor.getInt(0);
        String nombre = cursor.getString(2);
        String marca = getMarcaById(cursor.getInt(3));
        Double precio = cursor.getDouble(4);
        Drawable imagen = null;
        if (tipoProducto != PedidoProducto.TIPO_PRODUCTO.ACCESORIO) imagen = drawableFromBlob(cursor.getBlob(7)); else imagen = drawableFromBlob(cursor.getBlob(6));

        return new Producto(idProducto, tipoProducto, nombre, marca, precio, imagen);
    }

    // Función para seleccionar los productos de la base de datos, de acuerdo a los filtros aplicados a la búsqueda
    public ArrayList<Producto> getProductosFiltro(String tabla, int categoria, String filtroMarca, String filtroEstilo, String filtroTalla, String filtroSucursal, Double precioDesde, Double precioHasta, String buscar) {
        // Asigna el filtro de género
        String genero = null;
        if (categoria == 1) {
            genero = "M";
        } else {
            genero = "F";
        }

        // Asigna el tipo de producto a filtrar
        int tipoProducto = 0;
        if (tabla.equals("camisas")) {
            tipoProducto = PedidoProducto.TIPO_PRODUCTO.CAMISA;
        } else if (tabla.equals("pantalones")) {
            tipoProducto = PedidoProducto.TIPO_PRODUCTO.PANTALON;
        } else if (tabla.equals("zapatos")) {
            tipoProducto = PedidoProducto.TIPO_PRODUCTO.ZAPATO;
        } else {
            tipoProducto = PedidoProducto.TIPO_PRODUCTO.ACCESORIO;
        }

        // Construye el String del query para seleccionar los productos con los filtros seleccionados
        String query = "SELECT * FROM " + tabla + " WHERE";
        ArrayList<String> values = new ArrayList<>();
        // Si hay filtro de genero, se agrega al query
        if (categoria == 1 || categoria == 2) { query += " genero = ?"; values.add(genero); } else { query += " descripcion LIKE '%%'"; }
        // Si hay filtro de marca, se agrega al query
        if (!filtroMarca.equals("Seleccionar marca")) { query += " AND idMarca = ?"; values.add(Integer.toString(getIdMarca(filtroMarca))); }
        // Si hay filtro de estilo, se agrega al query
        if (!filtroEstilo.equals("Seleccionar estilo")) { query += " AND idEstilo = ?"; values.add(Integer.toString(new EstiloController(writeDB, readDB, context).getEstilosByNombre(filtroEstilo)[0].getIdEstilo())); }
        // Si hay filtro de talla, se agrega al query
        if (!filtroTalla.equals("Seleccionar talla") && !tabla.equals("accesorios")) { query += " AND talla = ?"; values.add(filtroTalla); }
        // Si hay filtro de sucursal, se agrega al query
        if (!filtroSucursal.equals("Seleccionar sucursal")) { query += " AND idSucursal = ?"; values.add(Integer.toString(getIdSucursal(filtroSucursal))); }
        // Si hay filtro de precio, se agrega al query
        if (precioDesde != -1.0) {
            if (precioHasta != -1.0) {
                query += " AND (precio BETWEEN ? AND ?)";
                values.add(Double.toString(precioDesde)); values.add(Double.toString(precioHasta));
            } else {
                query += " AND precio >= ?"; values.add(Double.toString(precioDesde));
            }
        } else if (precioHasta != -1.0) {
            query += " AND precio <= ?"; values.add(Double.toString(precioHasta));
        }
        // Si hay filtro del buscador, se agrega al query
        if (buscar != null && !buscar.trim().isEmpty()) { query += " AND nombre LIKE ?"; values.add("%" + buscar + "%"); }

        Cursor cursor = readDB.rawQuery(query, values.toArray(new String[0]));
        ArrayList<Producto> productos = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            productos.add(iteratorProducto(cursor, tipoProducto));
        }
        cursor.close();

        return productos;
    }
    //endregion
}
