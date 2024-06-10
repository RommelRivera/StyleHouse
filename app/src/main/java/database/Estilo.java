package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class Estilo {
    private SQLiteDatabase database;
    private int idEstilo;
    private String nombre;
    private Drawable imagen;

    public Estilo(int idEstilo, String nombre, Drawable imagen) {
        this.idEstilo = idEstilo;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public int getIdEstilo() {
        return idEstilo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
