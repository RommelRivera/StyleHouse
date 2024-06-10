package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class CamisaController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;
    private EstiloController estilo;

    public CamisaController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
        this.estilo = new EstiloController(writeDB, readDB, context);
    }

    //region Funciones de Camisa
    // Función de apoyo para crear un objeto Camisa con los datos obtenidos de la base de datos
    public Camisa iteratorCamisa(Cursor cursor) {
        int _idCamisa = cursor.getInt(0);
        Estilo _estilo = estilo.getEstiloById(cursor.getInt(1));
        String _nombre = cursor.getString(2);
        String _marca = helper.getMarcaById(cursor.getInt(3));
        double _precio = cursor.getDouble(4);
        String _talla = cursor.getString(5);
        String _sucursal = helper.getSucursalById(cursor.getInt(6));
        Drawable _imagen = helper.drawableFromBlob(cursor.getBlob(7));
        String _descripcion = cursor.getString(8);
        String _genero = cursor.getString(9);

        return new Camisa(_idCamisa, _estilo, _nombre, _marca, _precio, _talla, _sucursal, _imagen, _descripcion, _genero);
    }

    // Función de apoyo para automatizar la creación de un Array de tipo Camisa
    public Camisa[] iteratorCamisa(Camisa[] camisas, Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            camisas[i] = iteratorCamisa(cursor);
        }

        cursor.close();

        return setCamisasDatabase(camisas, readDB);
    }

    public Camisa[] getCamisas() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas;", null);
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa getCamisaById(int idCamisa) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE idCamisa = ?;", new String[] { Integer.toString(idCamisa) });
        cursor.moveToNext();

        Camisa camisa = iteratorCamisa(cursor);
        camisa.setDatabase(readDB);

        cursor.close();

        return camisa;
    }

    public Camisa[] getCamisasByEstilo(Estilo estilo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE idEstilo = ?;", new String[] { Integer.toString(estilo.getIdEstilo()) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByNombre(String nombre) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE nombre = ?;", new String[] { nombre });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByMarca(String marca) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE idMarca = ?;", new String[] { Integer.toString(helper.getIdMarca(marca)) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByPrecio(double precio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE precio = ?;", new String[] { Double.toString(precio) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByPrecioFrom(double fromPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE precio >= ?;", new String[] { Double.toString(fromPrecio) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByPrecioTo(double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE precio <= ?;", new String[] { Double.toString(toPrecio) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByPrecioBetween(double fromPrecio, double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE precio BETWEEN ? AND ?;", new String[] { Double.toString(fromPrecio), Double.toString(toPrecio) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByTalla(String talla) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE talla = ?;", new String[] { talla });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasBySucursal(String sucursal) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE idSucursal = ?;", new String[] { Integer.toString(helper.getIdSucursal(sucursal)) });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] getCamisasByGenero(String genero) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM camisas WHERE genero = ?;", new String[] { genero });
        Camisa[] camisas = new Camisa[cursor.getCount()];

        return iteratorCamisa(camisas, cursor);
    }

    public Camisa[] setCamisasDatabase(Camisa[] camisas, SQLiteDatabase database) {
        for (int i = 0; i < camisas.length; i++) {
            camisas[i].setDatabase(database);
        }

        return camisas;
    }
    //endregion

    //region Funciones de INSERT, UPDATE y DELETE
    // Función de apoyo para crear una variable ContentValues con los valores a ingresar a la base de datos
    public ContentValues valuesPrep(int idEstilo, String nombre, int idMarca, double precio, String talla, int idSucursal, byte[] imagen, String descripcion, String genero) {
        ContentValues values = new ContentValues();
        values.put("idEstilo", idEstilo);
        values.put("nombre", nombre);
        values.put("idMarca", idMarca);
        values.put("precio", precio);
        values.put("talla", talla);
        values.put("idSucursal", idSucursal);
        values.put("imagen", imagen);
        values.put("descripcion", descripcion);
        values.put("genero", genero);

        return values;
    }

    public long insertCamisa(Camisa camisa) {
        int idEstilo = camisa.getEstilo().getIdEstilo();
        String nombre = camisa.getNombre();
        int idMarca = helper.getIdMarca(camisa.getMarca());
        double precio = camisa.getPrecio();
        String talla = camisa.getTalla();
        int idSucursal = helper.getIdSucursal(camisa.getSucursal());
        byte[] imagen = helper.blobFromDrawable(camisa.getImagen());
        String descripcion = camisa.getDescripcion();
        String genero = camisa.getGenero();

        return writeDB.insert("camisas", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertCamisa(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("camisas", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertCamisa(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("camisas", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertCamisa(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("camisas", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertCamisa(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("camisas", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public int updateCamisa(Camisa camisa) {
        int idEstilo = camisa.getEstilo().getIdEstilo();
        String nombre = camisa.getNombre();
        int idMarca = helper.getIdMarca(camisa.getMarca());
        double precio = camisa.getPrecio();
        String talla = camisa.getTalla();
        int idSucursal = helper.getIdSucursal(camisa.getSucursal());
        byte[] imagen = helper.blobFromDrawable(camisa.getImagen());
        String descripcion = camisa.getDescripcion();
        String genero = camisa.getGenero();

        return writeDB.update("camisas", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idCamisa = ?", new String[] { Integer.toString(camisa.getIdCamisa()) });
    }

    public int updateCamisa(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero, int idCamisa) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("camisas", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idCamisa = ?", new String[] { Integer.toString(idCamisa) });
    }

    public int updateCamisa(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero, int idCamisa) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.update("camisas", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idCamisa = ?", new String[] { Integer.toString(idCamisa) });
    }

    public int updateCamisa(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero, int idCamisa) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("camisas", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idCamisa = ?", new String[] { Integer.toString(idCamisa) });
    }

    public int updateCamisa(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero, int idCamisa) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.update("camisas", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idCamisa = ?", new String[] { Integer.toString(idCamisa) });
    }

    public int deleteCamisa(int idCamisa) {
        return writeDB.delete("camisas", "idCamisa = ?", new String[] { Integer.toString(idCamisa) });
    }
    //endregion
}
