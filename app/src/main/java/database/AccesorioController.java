package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class AccesorioController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;
    private EstiloController estilo;

    public AccesorioController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
        this.estilo = new EstiloController(writeDB, readDB, context);
    }

    //region Funciones de Accesorio
    // Función de apoyo para crear un objeto Accesorio con los datos obtenidos de la base de datos
    public Accesorio iteratorAccesorio(Cursor cursor) {
        int _idAccesorio = cursor.getInt(0);
        Estilo _estilo = estilo.getEstiloById(cursor.getInt(1));
        String _nombre = cursor.getString(2);
        String _marca = helper.getMarcaById(cursor.getInt(3));
        double _precio = cursor.getDouble(4);
        String _sucursal = helper.getSucursalById(cursor.getInt(5));
        Drawable _imagen = helper.drawableFromBlob(cursor.getBlob(6));
        String _descripcion = cursor.getString(7);

        return new Accesorio(_idAccesorio, _estilo, _nombre, _marca, _precio, _sucursal, _imagen, _descripcion);
    }

    // Función de apoyo para automatizar la creación de un Array de tipo Accesorio
    public Accesorio[] iteratorAccesorio(Accesorio[] accesorios, Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            accesorios[i] = iteratorAccesorio(cursor);
        }

        cursor.close();

        return setAccesoriosDatabase(accesorios, readDB);
    }

    public Accesorio[] getAccesorios() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios;", null);
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio getAccesorioById(int idAccesorio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE idAccesorio = ?;", new String[] { Integer.toString(idAccesorio) });
        cursor.moveToNext();

        Accesorio accesorio = iteratorAccesorio(cursor);
        accesorio.setDatabase(readDB);

        cursor.close();

        return accesorio;
    }

    public Accesorio[] getAccesoriosByEstilo(Estilo estilo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE idEstilo = ?;", new String[] { Integer.toString(estilo.getIdEstilo()) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByNombre(String nombre) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE nombre = ?;", new String[] { nombre });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByMarca(String marca) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE idMarca = ?;", new String[] { Integer.toString(helper.getIdMarca(marca)) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByPrecio(double precio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE precio = ?;", new String[] { Double.toString(precio) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByPrecioFrom(double fromPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE precio >= ?", new String[] { Double.toString(fromPrecio) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByPrecioTo(double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE precio <= ?;", new String[] { Double.toString(toPrecio) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosByPrecioBetween(double fromPrecio, double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE precio BETWEEN ? AND ?", new String[] { Double.toString(fromPrecio), Double.toString(toPrecio)});
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] getAccesoriosBySucursal(String sucursal) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM accesorios WHERE idSucursal = ?;", new String[] { Integer.toString(helper.getIdSucursal(sucursal)) });
        Accesorio[] accesorios = new Accesorio[cursor.getCount()];

        return iteratorAccesorio(accesorios, cursor);
    }

    public Accesorio[] setAccesoriosDatabase(Accesorio[] accesorios, SQLiteDatabase database) {
        for (int i = 0; i < accesorios.length; i++) {
            accesorios[i].setDatabase(database);
        }

        return accesorios;
    }
    //endregion

    //region Funciones de INSERT, UPDATE y DELETE
    // Función de apoyo para crear una variable ContentValues con los valores a ingresar a la base de datos
    public ContentValues valuesPrep(int idEstilo, String nombre, int idMarca, double precio, int idSucursal, byte[] imagen, String descripcion) {
        ContentValues values = new ContentValues();
        values.put("idEstilo", idEstilo);
        values.put("nombre", nombre);
        values.put("idMarca", idMarca);
        values.put("precio", precio);
        values.put("idSucursal", idSucursal);
        values.put("imagen", imagen);
        values.put("descripcion", descripcion);

        return values;
    }

    public long insertAccesorio(Accesorio accesorio) {
        int idEstilo = accesorio.getEstilo().getIdEstilo();
        String nombre = accesorio.getNombre();
        int idMarca = helper.getIdMarca(accesorio.getMarca());
        double precio = accesorio.getPrecio();
        int idSucursal = helper.getIdSucursal(accesorio.getSucursal());
        byte[] imagen = helper.blobFromDrawable(accesorio.getImagen());
        String descripcion = accesorio.getDescripcion();

        return writeDB.insert("accesorios", null, valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion));
    }

    public long insertAccesorio(Estilo estilo, String nombre, String marca, double precio, String sucursal, Drawable drawable, String descripcion) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("accesorios", null, valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion));
    }

    public long insertAccesorio(Estilo estilo, String nombre, String marca, double precio, String sucursal, int idImagen, String descripcion) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("accesorios", null, valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion));
    }

    public long insertAccesorio(int idEstilo, String nombre, String marca, double precio, String sucursal, Drawable drawable, String descripcion) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("accesorios", null, valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion));
    }

    public long insertAccesorio(int idEstilo, String nombre, String marca, double precio, String sucursal, int idImagen, String descripcion) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.insert("accesorios", null, valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion));
    }

    public int updateAccesorio(Accesorio accesorio) {
        int idEstilo = accesorio.getEstilo().getIdEstilo();
        String nombre = accesorio.getNombre();
        int idMarca = helper.getIdMarca(accesorio.getMarca());
        double precio = accesorio.getPrecio();
        int idSucursal = helper.getIdSucursal(accesorio.getSucursal());
        byte[] imagen = helper.blobFromDrawable(accesorio.getImagen());
        String descripcion = accesorio.getDescripcion();

        return writeDB.update("accesorios", valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion), "idAccesorio = ?", new String[] { Integer.toString(accesorio.getIdAccesorio()) });
    }

    public int updateAccesorio(Estilo estilo, String nombre, String marca, double precio, String sucursal, Drawable drawable, String descripcion, int idAccesorio) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("accesorios", valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion), "idAccesorio = ?", new String[] { Integer.toString(idAccesorio) });
    }

    public int updateAccesorio(Estilo estilo, String nombre, String marca, double precio, String sucursal, int idImagen, String descripcion, int idAccesorio) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.update("accesorios", valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion), "idAccesorio = ?", new String[] { Integer.toString(idAccesorio) });
    }

    public int updateAccesorio(int idEstilo, String nombre, String marca, double precio, String sucursal, Drawable drawable, String descripcion, int idAccesorio) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("accesorios", valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion), "idAccesorio = ?", new String[] { Integer.toString(idAccesorio) });
    }

    public int updateAccesorio(int idEstilo, String nombre, String marca, double precio, String sucursal, int idImagen, String descripcion, int idAccesorio) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.update("accesorios", valuesPrep(idEstilo, nombre, idMarca, precio, idSucursal, imagen, descripcion), "idAccesorio = ?", new String[] { Integer.toString(idAccesorio) });
    }

    public int deleteAccesorio(int idAccesorio) {
        return writeDB.delete("accesorios", "idAccesorio = ?", new String[] { Integer.toString(idAccesorio) });
    }
    //endregion
}
