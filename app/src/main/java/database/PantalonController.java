package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class PantalonController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;
    private EstiloController estilo;

    public PantalonController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
        this.estilo = new EstiloController(writeDB, readDB, context);
    }

    //region Funciones de Pantalon
    // Función de apoyo para crear un objeto Pantalon con los datos obtenidos de la base de datos
    public Pantalon iteratorPantalon(Cursor cursor) {
        int _idPantalon = cursor.getInt(0);
        Estilo _estilo = estilo.getEstiloById(cursor.getInt(1));
        String _nombre = cursor.getString(2);
        String _marca = helper.getMarcaById(cursor.getInt(3));
        double _precio = cursor.getDouble(4);
        String _talla = cursor.getString(5);
        String _sucursal = helper.getSucursalById(cursor.getInt(6));
        Drawable _imagen = helper.drawableFromBlob(cursor.getBlob(7));
        String _descripcion = cursor.getString(8);
        String _genero = cursor.getString(9);

        return new Pantalon(_idPantalon, _estilo, _nombre, _marca, _precio, _talla, _sucursal, _imagen, _descripcion, _genero);
    }

    // Función de apoyo para automatizar la creación de un Array de tipo Pantalon
    public Pantalon[] iteratorPantalon(Pantalon[] pantalones, Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            pantalones[i] = iteratorPantalon(cursor);
        }

        cursor.close();

        return setPantalonesDatabase(pantalones, readDB);
    }

    public Pantalon[] getPantalones() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones;", null);
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon getPantalonById(int idPantalon) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE idPantalon = ?;", new String[]{Integer.toString(idPantalon)});
        cursor.moveToNext();

        Pantalon pantalon = iteratorPantalon(cursor);
        pantalon.setDatabase(readDB);

        cursor.close();

        return pantalon;
    }

    public Pantalon[] getPantalonesByEstilo(Estilo estilo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE idEstilo = ?;", new String[] { Integer.toString(estilo.getIdEstilo()) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByNombre(String nombre) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE nombre = ?;", new String[] { nombre });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByMarca(String marca) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE idMarca = ?;", new String[]{ Integer.toString(helper.getIdMarca(marca)) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByPrecio(double precio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE precio = ?;", new String[]{ Double.toString(precio) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByPrecioFrom(double fromPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE precio >= ?;", new String[]{ Double.toString(fromPrecio) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByPrecioTo(double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE precio <= ?;", new String[] { Double.toString(toPrecio) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByPrecioBetween(double fromPrecio, double toPrecio) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE precio BETWEEN ? AND ?;", new String[] { Double.toString(fromPrecio), Double.toString(toPrecio) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesByTalla(String talla) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE talla = ?;", new String[] { talla });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] getPantalonesBySucursal(String sucursal) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM pantalones WHERE idSucursal = ?;", new String[] { Integer.toString(helper.getIdSucursal(sucursal)) });
        Pantalon[] pantalones = new Pantalon[cursor.getCount()];

        return iteratorPantalon(pantalones, cursor);
    }

    public Pantalon[] setPantalonesDatabase(Pantalon[] pantalones, SQLiteDatabase database) {
        for (int i = 0; i < pantalones.length; i++) {
            pantalones[i].setDatabase(database);
        }

        return pantalones;
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

    public long insertPantalon(Pantalon pantalon) {
        int idEstilo = pantalon.getEstilo().getIdEstilo();
        String nombre = pantalon.getNombre();
        int idMarca = helper.getIdMarca(pantalon.getMarca());
        double precio = pantalon.getPrecio();
        String talla = pantalon.getTalla();
        int idSucursal = helper.getIdSucursal(pantalon.getSucursal());
        byte[] imagen = helper.blobFromDrawable(pantalon.getImagen());
        String descripcion = pantalon.getDescripcion();
        String genero = pantalon.getGenero();

        return writeDB.insert("pantalones", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertPantalon(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("pantalones", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertPantalon(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("pantalones", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertPantalon(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("pantalones", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public long insertPantalon(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.insert("pantalones", null, valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero));
    }

    public int updatePantalon(Pantalon pantalon) {
        int idEstilo = pantalon.getEstilo().getIdEstilo();
        String nombre = pantalon.getNombre();
        int idMarca = helper.getIdMarca(pantalon.getMarca());
        double precio = pantalon.getPrecio();
        String talla = pantalon.getTalla();
        int idSucursal = helper.getIdSucursal(pantalon.getSucursal());
        byte[] imagen = helper.blobFromDrawable(pantalon.getImagen());
        String descripcion = pantalon.getDescripcion();
        String genero = pantalon.getGenero();

        return writeDB.update("pantalones", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idPantalon = ?", new String[] { Integer.toString(pantalon.getIdPantalon()) });
    }

    public int updatePantalon(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero, int idPantalon) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("pantalones", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idPantalon = ?", new String[] { Integer.toString(idPantalon) });
    }

    public int updatePantalon(Estilo estilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero, int idPantalon) {
        int idEstilo = estilo.getIdEstilo();
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.update("pantalones", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idPantalon = ?", new String[] { Integer.toString(idPantalon) });
    }

    public int updatePantalon(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, Drawable drawable, String descripcion, String genero, int idPantalon) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("pantalones", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idPantalon = ?", new String[] { Integer.toString(idPantalon) });
    }

    public int updatePantalon(int idEstilo, String nombre, String marca, double precio, String talla, String sucursal, int idImagen, String descripcion, String genero, int idPantalon) {
        int idMarca = helper.getIdMarca(marca);
        int idSucursal = helper.getIdSucursal(sucursal);
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return  writeDB.update("pantalones", valuesPrep(idEstilo, nombre, idMarca, precio, talla, idSucursal, imagen, descripcion, genero), "idPantalon = ?", new String[] { Integer.toString(idPantalon) });
    }

    public int deletePantalon(int idPantalon) {
        return writeDB.delete("pantalones", "idPantalon = ?", new String[] { Integer.toString(idPantalon) });
    }
    //endregion
}
