package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class ZapatoController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;
    private EstiloController estilo;

    public ZapatoController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
        this.estilo = new EstiloController(writeDB, readDB, context);
    }

    //region Funciones de Zapato
    // Función de apoyo para crear un objeto Zapato con los datos obtenidos de la base de datos
    public Zapato iteratorZapato(Cursor cursor) {
        int _idZapato = cursor.getInt(0);
        Estilo _estilo = estilo.getEstiloById(cursor.getInt(1));
        String _nombre = cursor.getString(2);
        String _marca = helper.getMarcaById(cursor.getInt(3));
        double _precio = cursor.getDouble(4);
        String _talla = cursor.getString(5);
        String _sucursal = helper.getSucursalById(cursor.getInt(6));
        Drawable _imagen = helper.drawableFromBlob(cursor.getBlob(7));
        String _descripcion = cursor.getString(8);

        return new Zapato(_idZapato, _estilo, _nombre, _marca, _precio, _talla, _sucursal, _imagen, _descripcion);
    }

    // Función de apoyo para automatizar la creación de un Array de tipo Zapato
    public Zapato[] iteratorZapato(Zapato[] zapatos, Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            zapatos[i] = iteratorZapato(cursor);
        }

        cursor.close();

        return setZapatosDatabase(zapatos, readDB);
    }

    public Zapato[] getZapatos() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos;", null);
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato getZapatoById(int idZapato) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE idZapato = ?;", new String[] { Integer.toString(idZapato) });
        cursor.moveToNext();

        Zapato zapato = iteratorZapato(cursor);
        zapato.setDatabase(readDB);

        cursor.close();

        return zapato;
    }

    public Zapato[] getZapatosByEstilo(Estilo estilo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE idEstilo = ?;", new String[] { Integer.toString(estilo.getIdEstilo()) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByNombre(String nombre) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE nombre = ?;", new String[] { nombre });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByMarca(String marca) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE idMarca = ?;", new String[] { Integer.toString(helper.getIdMarca(marca)) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByPrecio(double precio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE precio = ?;", new String[] { Double.toString(precio) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByPrecioFrom(double fromPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE precio >= ?;", new String[] { Double.toString(fromPrecio) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByPrecioTo(double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE precio <= ?;", new String[] { Double.toString(toPrecio) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByPrecioBetween(double fromPrecio, double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE precio BETWEEN ? AND ?;", new String[] { Double.toString(fromPrecio), Double.toString(toPrecio) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosByTalla(String talla) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE talla = ?;", new String[] { talla });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] getZapatosBySucursal(String sucursal) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM zapatos WHERE idSucursal = ?;", new String[] { Integer.toString(helper.getIdSucursal(sucursal)) });
        Zapato[] zapatos = new Zapato[cursor.getCount()];

        return iteratorZapato(zapatos, cursor);
    }

    public Zapato[] setZapatosDatabase(Zapato[] zapatos, SQLiteDatabase database) {
        for (int i = 0; i < zapatos.length; i++) {
            zapatos[i].setDatabase(database);
        }

        return zapatos;
    }
    //endregion

    //region Funciones de INSERT, UPDATE y DELETE
    // Función de apoyo para crear una variable ContentValues con los valores a ingresar a la base de datos
    public ContentValues valuesPrep(int idEstilo, String nombre, int idMarca, double precio, String talla, int idSucursal, byte[] imagen, String descripcion) {
        ContentValues values = new ContentValues();
        values.put("idEstilo", idEstilo);
        values.put("nombre", nombre);
        values.put("idMarca", idMarca);
        values.put("precio", precio);
        values.put("talla", talla);
        values.put("idSucursal", idSucursal);
        values.put("imagen", imagen);
        values.put("descripcion", descripcion);

        return values;
    }

    public long insertZapato(Zapato zapato) {
        int idEstilo = zapato.getEstilo().getIdEstilo();
        String nombre = zapato.getNombre();
        int idMarca = helper.getIdMarca(zapato.getMarca());
        double precio = zapato.getPrecio();
        String talla = zapato.getTalla();
        int idSucursal = helper.getIdSucursal(zapato.getSucursal());
        byte[] imagen = helper.blobFromDrawable(zapato.getImagen());
        String descripcion = zapato.getDescripcion();

        return writeDB.insert("zapatos", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion));
    }

    public long insertZapato(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("zapatos", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion));
    }

    public long insertZapato(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("zapatos", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion));
    }

    public long insertZapato(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("zapatos", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion));
    }

    public long insertZapato(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.insert("zapatos", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion));
    }

    public int updateZapato(Zapato zapato) {
        int idEstilo = zapato.getEstilo().getIdEstilo();
        String nombre = zapato.getNombre();
        int idMarca = helper.getIdMarca(zapato.getMarca());
        double precio = zapato.getPrecio();
        String talla = zapato.getTalla();
        int idSucursal = helper.getIdSucursal(zapato.getSucursal());
        byte[] imagen = helper.blobFromDrawable(zapato.getImagen());
        String descripcion = zapato.getDescripcion();

        return writeDB.update("zapatos", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion), "idZapato = ?", new String[] { Integer.toString(zapato.getIdZapato()) });
    }

    public int updateZapato(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, int idZapato) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("zapatos", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion), "idZapato = ?", new String[] { Integer.toString(idZapato) });
    }

    public int updateZapato(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, int idZapato) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.update("zapatos", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion), "idZapato = ?", new String[] { Integer.toString(idZapato) });
    }

    public int updateZapato(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, int idZapato) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("zapatos", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion), "idZapato = ?", new String[] { Integer.toString(idZapato) });
    }

    public int updateZapato(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, int idZapato) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.update("zapatos", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion), "idZapato = ?", new String[] { Integer.toString(idZapato) });
    }

    public int deleteZapato(int idZapato) {
        return writeDB.delete("zapatos", "idZapato = ?", new String[] { Integer.toString(idZapato) });
    }
    //endregion
}
