package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioController {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    private Context context;

    public UsuarioController(SQLiteDatabase writeDB, SQLiteDatabase readDB, Context context) {
        this.writeDB = writeDB;
        this.readDB = readDB;
        this.context = context;
    }

    // Funciones para verificar los datos del usuario
    public boolean checkUsuarioExistsCorreo(String correo) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM usuarios WHERE correo = ?;", new String[] { correo });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public boolean checkUsuarioExistsUsername(String username) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM usuarios WHERE username = ?;", new String[] { username });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public boolean checkUsuarioCredentials(String correoUsername, String password) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM usuarios WHERE correo = ? AND password = ?;", new String[] { correoUsername, password });
        boolean exists = cursor.moveToNext();
        cursor.close();

        return exists;
    }

    public int getIdUsuario(String correoUsername, String password) {
        Cursor cursor = readDB.rawQuery("SELECT idUsuario FROM usuarios WHERE (correo = ? OR username = ?) AND password = ?;", new String[] { correoUsername, correoUsername, password });
        cursor.moveToNext();
        int idUsuario = cursor.getInt(0);
        cursor.close();

        return idUsuario;
    }

    // Obtener los datos del usuario
    public String[] getDataUsuario(int idUsuario) {
        Cursor cursor = readDB.rawQuery("SELECT * FROM usuarios WHERE idUsuario = ?;", new String[] { Integer.toString(idUsuario) });
        String[] datos = new String[4];
        cursor.moveToNext();

        datos[0] = cursor.getString(1);
        datos[1] = cursor.getString(2);
        datos[2] = cursor.getString(3);
        datos[3] = cursor.getString(4);

        cursor.close();

        return datos;
    }

    public long insertUsuario(String username, String correo, String password, String nombre) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("correo", correo);
        values.put("password", password);
        values.put("nombre", nombre);

        return writeDB.insert("usuarios", null, values);
    }

    // Insertar un usuario de con el rol de ADMINISTRADOR
    public long insertAdmin(String username, String correo, String password, String nombre) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("correo", correo);
        values.put("password", password);
        values.put("nombre", nombre);
        values.put("rol", "ADMINISTRADOR");

        return writeDB.insert("usuarios", null, values);
    }

    public int updateUsuario(int idUsuario, String username, String correo, String password, String nombre) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("correo", correo);
        values.put("password", password);
        values.put("nombre", nombre);

        return writeDB.update("usuarios", values, "idUsuario = ?;", new String[] { Integer.toString(idUsuario) });
    }
}
