package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Base64;

public class PedidoProductoController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;

    public PedidoProductoController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
    }

    // Función para retornar el tipo del producto
    public int tipoProducto(int idCamisa, int idPantalon, int idZapato, int idAccesorio) {
        if (idCamisa != 0) return PedidoProducto.TIPO_PRODUCTO.CAMISA;
        if (idPantalon != 0) return PedidoProducto.TIPO_PRODUCTO.PANTALON;
        if (idZapato != 0) return PedidoProducto.TIPO_PRODUCTO.ZAPATO;
        if (idAccesorio != 0) return PedidoProducto.TIPO_PRODUCTO.ACCESORIO;

        return -1;
    }

    // Función para retornar los datos del producto en un Array de tipo String
    public String[] datosProducto(int idCamisa, int idPantalon, int idZapato, int idAccesorio, int tipoProducto) {
        String[] datos = new String[5];
        Cursor cursor;
        switch (tipoProducto) {
            case PedidoProducto.TIPO_PRODUCTO.CAMISA:
                cursor = readDB.rawQuery("SELECT * FROM camisas WHERE idCamisa = ?;", new String[] { Integer.toString(idCamisa) });
                cursor.moveToNext();
                datos[0] = Integer.toString(cursor.getInt(0));
                datos[1] = cursor.getString(2);
                datos[2] = cursor.getString(3);
                datos[3] = Double.toString(cursor.getDouble(4));
                datos[4] = Base64.getEncoder().encodeToString(cursor.getBlob(7));
                cursor.close();
                break;
            case PedidoProducto.TIPO_PRODUCTO.PANTALON:
                cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE idPantalon = ?;", new String[] { Integer.toString(idPantalon) });
                cursor.moveToNext();
                datos[0] = Integer.toString(cursor.getInt(0));
                datos[1] = cursor.getString(2);
                datos[2] = cursor.getString(3);
                datos[3] = Double.toString(cursor.getDouble(4));
                datos[4] = Base64.getEncoder().encodeToString(cursor.getBlob(7));
                cursor.close();
                break;
            case PedidoProducto.TIPO_PRODUCTO.ZAPATO:
                cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE idZapato = ?;", new String[] { Integer.toString(idZapato) });
                cursor.moveToNext();
                datos[0] = Integer.toString(cursor.getInt(0));
                datos[1] = cursor.getString(2);
                datos[2] = cursor.getString(3);
                datos[3] = Double.toString(cursor.getDouble(4));
                datos[4] = Base64.getEncoder().encodeToString(cursor.getBlob(7));
                cursor.close();
                break;
            case PedidoProducto.TIPO_PRODUCTO.ACCESORIO:
                cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE idAccesorio = ?;", new String[] { Integer.toString(idAccesorio) });
                cursor.moveToNext();
                datos[0] = Integer.toString(cursor.getInt(0));
                datos[1] = cursor.getString(2);
                datos[2] = cursor.getString(3);
                datos[3] = Double.toString(cursor.getDouble(4));
                datos[4] = Base64.getEncoder().encodeToString(cursor.getBlob(6));
                cursor.close();
                break;
        }

        return datos;
    }

    // Función de apoyo para crear un objeto PedidoProducto con los datos obtenidos de la base de datos
    public PedidoProducto iteratorPedidoProducto(Cursor cursor) {
        int idPedidoProducto = cursor.getInt(0);
        int idPedido = cursor.getInt(1);
        int tipoProducto = tipoProducto(cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
        String[] datos = datosProducto(cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), tipoProducto);
        int idProducto = Integer.parseInt(datos[0]);
        String nombre = datos[1];
        String marca = datos[2];
        double precio = Double.parseDouble(datos[3]);
        Drawable imagen = helper.drawableFromBlob(Base64.getDecoder().decode(datos[4]));

        return new PedidoProducto(idPedidoProducto, idPedido, tipoProducto, idProducto, nombre, marca, precio, imagen);
    }

    // Función de apoyo para automatizar la creación de un ArrayList de tipo PedidoProducto
    public ArrayList<PedidoProducto> iteratorPedidoProducto(Cursor cursor, ArrayList<PedidoProducto> pedidosProductos) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            pedidosProductos.add(iteratorPedidoProducto(cursor));
        }

        cursor.close();

        return setPedidosProductosDatabase(pedidosProductos, readDB);
    }

    public ArrayList<PedidoProducto> getPedidosProductos() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos;", null);
        ArrayList<PedidoProducto> pedidosProductos = new ArrayList<>(cursor.getCount());

        return iteratorPedidoProducto(cursor, pedidosProductos);
    }

    public ArrayList<PedidoProducto> getPedidosProductosByPedido(int idPedido) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos WHERE idPedido = ?;", new String[] { Integer.toString(idPedido) });
        ArrayList<PedidoProducto> pedidosProductos = new ArrayList<>(cursor.getCount());

        return iteratorPedidoProducto(cursor, pedidosProductos);
    }

    // Función para obtener el total del pedido
    public double getTotalPedido(int idPedido) {
        ArrayList<PedidoProducto> productos = getPedidosProductosByPedido(idPedido);
        String tipo = "";
        String id = "";
        double total = 0.0;

        for (PedidoProducto producto : productos) {
            if (producto.getTipoProducto() == PedidoProducto.TIPO_PRODUCTO.CAMISA) { tipo = "camisas"; id = "idCamisa"; }
            if (producto.getTipoProducto() == PedidoProducto.TIPO_PRODUCTO.PANTALON) { tipo = "pantalones"; id = "idPantalon"; }
            if (producto.getTipoProducto() == PedidoProducto.TIPO_PRODUCTO.ZAPATO) { tipo = "zapatos"; id = "idZapato"; }
            if (producto.getTipoProducto() == PedidoProducto.TIPO_PRODUCTO.ACCESORIO) { tipo = "accesorios"; id = "idAccesorio"; }
            Cursor cursor = readDB.rawQuery("SELECT precio FROM " + tipo + " WHERE " + id + " = ?;", new String[] { Integer.toString(producto.getIdProducto()) });
            cursor.moveToNext();
            total += cursor.getDouble(0);
            cursor.close();
        }

        return total;
    }

    // Funciones para revisar si un producto existe dentro del pedido
    public boolean exists(int idPedido, Camisa camisa) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos WHERE idPedido = ? AND idCamisa = ?", new String[] { Integer.toString(idPedido), Integer.toString(camisa.getIdCamisa()) });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public boolean exists(int idPedido, Pantalon pantalon) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos WHERE idPedido = ? AND idPantalon = ?", new String[] { Integer.toString(idPedido), Integer.toString(pantalon.getIdPantalon()) });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public boolean exists(int idPedido, Zapato zapato) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos WHERE idPedido = ? AND idZapato = ?", new String[] { Integer.toString(idPedido), Integer.toString(zapato.getIdZapato()) });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public boolean exists(int idPedido, Accesorio accesorio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pedidosProductos WHERE idPedido = ? AND idAccesorio = ?", new String[] { Integer.toString(idPedido), Integer.toString(accesorio.getIdAccesorio()) });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public ArrayList<PedidoProducto> setPedidosProductosDatabase(ArrayList<PedidoProducto> pedidosProductos, SQLiteDatabase database) {
        for (PedidoProducto pedidoProducto : pedidosProductos) {
            pedidoProducto.setDatabase(database);
        }

        return pedidosProductos;
    }

    public long insertPedidoProducto(int idPedido, int idProducto, int tipoProducto) {
        ContentValues values = new ContentValues();
        values.put("idPedido", idPedido);
        String producto = "";
        switch (tipoProducto) {
            case PedidoProducto.TIPO_PRODUCTO.CAMISA:
                producto = "idCamisa";
                break;
            case PedidoProducto.TIPO_PRODUCTO.PANTALON:
                producto = "idPantalon";
                break;
            case PedidoProducto.TIPO_PRODUCTO.ZAPATO:
                producto = "idZapato";
                break;
            case PedidoProducto.TIPO_PRODUCTO.ACCESORIO:
                producto = "idAccesorio";
                break;
        }
        values.put(producto, idProducto);

        return writeDB.insert("pedidosProductos", null, values);
    }

    // Función para asignar una cantidad nueva al producto del pedido
    public int setCantidadPedidoProducto(int idPedidoProducto, int cantidad) {
        ContentValues values = new ContentValues();
        values.put("cantidad", cantidad);

        return writeDB.update("pedidosProductos", values, "idPedidoProducto = ?;", new String[] { Integer.toString(idPedidoProducto) });
    }

    public int deletePedidoProducto(int idPedidoProducto) {
        return writeDB.delete("pedidosProductos","idPedidoProducto = ?;", new String[] { Integer.toString(idPedidoProducto) });
    }
}
