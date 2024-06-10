package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class EstiloController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private HelperController helper;

    public EstiloController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.helper = new HelperController(writeDB, readDB, context);
    }

    //region Funciones de SELECT
    // Función de apoyo para crear un objeto Estilo con los datos obtenidos de la base de datos
    public Estilo iteratorEstilo(Cursor cursor) {
        int _idEstilo = cursor.getInt(0);
        String _nombre = cursor.getString(1);
        Drawable _imagen = helper.drawableFromBlob(cursor.getBlob(2));

        return new Estilo(_idEstilo, _nombre, _imagen);
    }

    // Función de apoyo para automatizar la creación de un Array de tipo Estilo
    public Estilo[] iteratorEstilo(Estilo[] estilos, Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            estilos[i] = iteratorEstilo(cursor);
        }

        cursor.close();

        return setEstilosDatabase(estilos, readDB);
    }

    public String[] getEstiloNombres() {
        Cursor cursor = readDB.rawQuery("SELECT nombre FROM estilos;", null);
        String[] estilos = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            estilos[i] = cursor.getString(0);
        }

        cursor.close();

        return estilos;
    }

    public Estilo[] getEstilos() {
        Cursor cursor = readDB.rawQuery("SELECT * FROM estilos;", null);
        Estilo[] estilos = new Estilo[cursor.getCount()];

        return iteratorEstilo(estilos, cursor);
    }

    public Estilo getEstiloById(int idEstilo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM estilos WHERE idEstilo = ?;", new String[] { Integer.toString(idEstilo) });
        cursor.moveToNext();

        Estilo estilo = iteratorEstilo(cursor);
        estilo.setDatabase(readDB);

        cursor.close();

        return estilo;
    }

    public Estilo[] getEstilosByNombre(String nombre) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM estilos WHERE nombre = ?;", new String[] { nombre });
        Estilo[] estilos = new Estilo[cursor.getCount()];

        return iteratorEstilo(estilos, cursor);
    }

    public Estilo[] setEstilosDatabase(Estilo[] estilos, SQLiteDatabase database) {
        for (int i = 0; i < estilos.length; i++) {
            estilos[i].setDatabase(database);
        }

        return estilos;
    }
    //endregion

    //region Funciones de INSERT, UPDATE y DELETE
    // Función de apoyo para crear una variable ContentValues con los valores a ingresar a la base de datos
    public ContentValues valuesPrep(String nombre, byte[] imagen) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("imagen", imagen);

        return values;
    }

    public long insertEstilo(Estilo estilo){
        String nombre = estilo.getNombre();
        byte[] imagen = helper.blobFromDrawable(estilo.getImagen());

        return writeDB.insert("estilos", null, valuesPrep(nombre, imagen));
    }

    public long insertEstilo(String nombre, Drawable drawable) {
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.insert("estilos", null, valuesPrep(nombre, imagen));
    }

    public long insertEstilo(String nombre, int idImagen) {
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.insert("estilos", null, valuesPrep(nombre, imagen));
    }

    public int updateEstilo(Estilo estilo, int idEstilo){
        String nombre = estilo.getNombre();
        byte[] imagen = helper.blobFromDrawable(estilo.getImagen());

        return writeDB.update("estilos", valuesPrep(nombre, imagen), "idEstilo = ?", new String[] { Integer.toString(idEstilo) });
    }

    public int updateEstilo(String nombre, Drawable drawable, int idEstilo) {
        byte[] imagen = helper.blobFromDrawable(drawable);

        return writeDB.update("estilos", valuesPrep(nombre, imagen), "idEstilo = ?", new String[] { Integer.toString(idEstilo) });
    }

    public int updateEstilo(String nombre, int idImagen, int idEstilo) {
        byte[] imagen = helper.blobFromIdDrawable(idImagen);

        return writeDB.update("estilos", valuesPrep(nombre, imagen), "idEstilos = ?", new String[] { Integer.toString(idEstilo) });
    }

    public int deleteEstilo(int idEstilo) {
        return writeDB.delete("estilos", "idEstilo = ?", new String[] { Integer.toString(idEstilo) });
    }
    //endregion
}
