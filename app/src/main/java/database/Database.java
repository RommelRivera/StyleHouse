package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Database extends SQLiteOpenHelper {
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;
    public HelperController helperController;
    public UsuarioController usuarioController;
    public EstiloController estiloController;
    public CamisaController camisaController;
    public PantalonController pantalonController;
    public ZapatoController zapatoController;
    public AccesorioController accesorioController;
    public PedidoProductoController pedidoProductoController;

    // Constructor del objeto Database, con referencias a los controladores que dependen de él
    public Database(Context context) {
        super(context, "stylehouse", null, 1);
        this.writeDB = getWritableDatabase();
        this.readDB = getReadableDatabase();

        this.helperController = new HelperController(writeDB, readDB, context);
        this.usuarioController = new UsuarioController(writeDB, readDB, context);
        this.estiloController = new EstiloController(writeDB, readDB, context);
        this.camisaController = new CamisaController(writeDB, readDB, context);
        this.pantalonController = new PantalonController(writeDB, readDB, context);
        this.zapatoController = new ZapatoController(writeDB, readDB, context);
        this.accesorioController = new AccesorioController(writeDB, readDB, context);
        this.pedidoProductoController = new PedidoProductoController(writeDB, readDB, context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL("CREATE TABLE IF NOT EXISTS estilos (" +
                        "idEstilo INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT," +
                        "imagen BLOB" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS marcas (" +
                        "idMarca INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS sucursales (" +
                        "idSucursal INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS camisas (" +
                        "idCamisa INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idEstilo INTEGER REFERENCES estilos(idEstilos)," +
                        "nombre TEXT," +
                        "idMarca INTEGER REFERENCES marcas(idMarca)," +
                        "precio REAL," +
                        "talla TEXT," +
                        "idSucursal INTEGER REFERENCES sucursales(idSucursal)," +
                        "imagen BLOB," +
                        "descripcion TEXT," +
                        "genero TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS pantalones (" +
                        "idPantalon INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idEstilo INTEGER REFERENCES estilos(idEstilos)," +
                        "nombre TEXT," +
                        "idMarca INTEGER REFERENCES marcas(idMarca)," +
                        "precio REAL," +
                        "talla TEXT," +
                        "idSucursal INTEGER REFERENCES sucursales(idSucursal)," +
                        "imagen BLOB," +
                        "descripcion TEXT," +
                        "genero TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS zapatos (" +
                        "idZapato INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idEstilo INTEGER REFERENCES estilos(idEstilos)," +
                        "nombre TEXT," +
                        "idMarca INTEGER REFERENCES marcas(idMarca)," +
                        "precio REAL," +
                        "talla TEXT," +
                        "idSucursal INTEGER REFERENCES sucursales(idSucursal)," +
                        "imagen BLOB," +
                        "descripcion TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS accesorios (" +
                        "idAccesorio INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idEstilo INTEGER REFERENCES estilos(idEstilos)," +
                        "nombre TEXT," +
                        "idMarca INTEGER REFERENCES marcas(idMarca)," +
                        "precio REAL," +
                        "idSucursal INTEGER REFERENCES sucursales(idSucursal)," +
                        "imagen BLOB," +
                        "descripcion TEXT" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                        "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "correo TEXT," +
                        "password TEXT," +
                        "nombre TEXT," +
                        "rol TEXT DEFAULT 'CLIENTE'" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS pedidos (" +
                        "idPedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idUsuario INTEGER REFERENCES usuarios(idUsuario) DEFAULT 0," +
                        "estado TEXT DEFAULT 'ABIERTO'" +
                    ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS pedidosProductos (" +
                        "idPedidoProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idPedido INTEGER REFERENCES pedidos(idPedidos)," +
                        "cantidad INTEGER DEFAULT 1," +
                        "idCamisa INTEGER REFERENCES camisas(idCamisa) DEFAULT 0," +
                        "idPantalon INTEGER REFERENCES pantalones(idPantalon) DEFAULT 0," +
                        "idZapato INTEGER REFERENCES zapatos(idZapato) DEFAULT 0," +
                        "idAccesorio INTEGER REFERENCES accesorios(idAccesorio) DEFAULT 0" +
                    ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE estilos;");
        db.execSQL("DROP TABLE marcas;");
        db.execSQL("DROP TABLE sucursales;");
        db.execSQL("DROP TABLE camisas;");
        db.execSQL("DROP TABLE pantalones;");
        db.execSQL("DROP TABLE zapatos;");
        db.execSQL("DROP TABLE accesorios;");
        db.execSQL("DROP TABLE usuarios;");
        db.execSQL("DROP TABLE pedidos;");
        db.execSQL("DROP TABLE pedidosProductos;");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE estilos;");
        db.execSQL("DROP TABLE marcas;");
        db.execSQL("DROP TABLE sucursales;");
        db.execSQL("DROP TABLE camisas;");
        db.execSQL("DROP TABLE pantalones;");
        db.execSQL("DROP TABLE zapatos;");
        db.execSQL("DROP TABLE accesorios;");
        db.execSQL("DROP TABLE usuarios;");
        db.execSQL("DROP TABLE pedidos;");
        db.execSQL("DROP TABLE pedidosProductos;");
        onCreate(db);
    }
}
