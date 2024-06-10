package com.rommelrivera.stylehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import database.Accesorio;
import database.Camisa;
import database.Database;
import database.Estilo;
import database.Pantalon;
import database.PedidoProducto;
import database.Zapato;

public class Carrusel extends AppCompatActivity {

    RecyclerView rcvCarrusel;
    Button btnManual, btnAnterior, btnSiguiente;
    RecyclerView.Adapter currentAdapter;
    Estilo estilo;
    Estilo emptyEstilo;
    Camisa camisa;
    Camisa emptyCamisa;
    Pantalon pantalon;
    Pantalon emptyPantalon;
    Zapato zapato;
    Zapato emptyZapato;
    String tallacamisa = "S";
    String tallapantalon = "S";
    String tallazapato = "S";
    ArrayList<Accesorio> accesorios = new ArrayList<>();
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrusel);

        database = new Database(this);

        // Inserta los datos predeterminados a la base de datos, solo si es la primera vez que inicializa la app
        SharedPreferences pref = getSharedPreferences("datos", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if (pref.getInt("firstTime", 0) == 0) {
            Prueba.datosPrueba(this);
            edit.putInt("firstTime", 1);
            edit.apply();
        }

        // Crea objetos vacíos para inicializar el carrusel de selección de outfits
        emptyEstilo = new Estilo(0, "", database.helperController.drawableFromIdDrawable(R.drawable.ojo));
        emptyCamisa = new Camisa(0, emptyEstilo, "", "", 0.0, "", "", database.helperController.drawableFromIdDrawable(R.drawable.ojo), "", "M");
        emptyPantalon = new Pantalon(0, emptyEstilo, "", "", 0.0, "", "", database.helperController.drawableFromIdDrawable(R.drawable.ojo), "", "M");
        emptyZapato = new Zapato(0, emptyEstilo, "", "", 0.0, "", "", database.helperController.drawableFromIdDrawable(R.drawable.ojo), "");

        // Asigna a los elementos seleccionados por el usuario, al inicializarse, se escogen los elementos vacíos creados anteriormente, a excepción del estilo
        estilo = database.estiloController.getEstiloById(1);
        camisa = emptyCamisa;
        pantalon = emptyPantalon;
        zapato = emptyZapato;

        // Inicialización de los elementos del Layout
        rcvCarrusel = findViewById(R.id.rcvCarrusel);
        btnManual = findViewById(R.id.btnManual);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        
        // Asignar un GridLayout al RecyclerView para mostrar la lista en forma de cuadrícula
        rcvCarrusel.setLayoutManager(new GridLayoutManager(this, 2));

        // Asigna el Adapter de estilos al Carrusel
        changeAdapter(new EstilosAdapter(this, this, database.estiloController.getEstilos(), estilo));

        // Este botón manda al usuario a la pantalla de Catálogo, para escoger ítems de forma manual
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(Carrusel.this, Main.class);
                newIntent.putExtra("destino", "catalogo");
                startActivity(newIntent);
            }
        });

        // Los botones de Anterior y Siguiente cambian su comportamiento basados en el Adapter actualmente
        // asignado al RecyclerView, este comportamiento se asigna con la función de apoyo chooseAdapter()
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAdapter(false);
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAdapter(true);
            }
        });
    }

    // La función de ayuda chooseAdapter() revisa el tipo de Adapter que está asignado actualmente, y en base al Adapter,
    // y a si el botón es Anterior (false) o Siguiente (true), cambia el Adapter asignado al RecyclerView.
    public void chooseAdapter(boolean siguiente) {
        if (currentAdapter instanceof EstilosAdapter) {
            if (siguiente) {
                changeAdapter(new CamisasAdapter(Carrusel.this, Carrusel.this, database.camisaController.getCamisasByEstilo(estilo), camisa, tallacamisa));
            }
            return;
        }
        if (currentAdapter instanceof CamisasAdapter) {
            if (siguiente) {
                changeAdapter(new PantalonesAdapter(Carrusel.this, Carrusel.this, database.pantalonController.getPantalonesByEstilo(estilo), pantalon, tallapantalon));
            } else {
                changeAdapter(new EstilosAdapter(Carrusel.this, Carrusel.this, database.estiloController.getEstilos(), estilo));
            }
            return;
        }
        if (currentAdapter instanceof PantalonesAdapter) {
            if (siguiente) {
                changeAdapter(new ZapatosAdapter(Carrusel.this, Carrusel.this, database.zapatoController.getZapatosByEstilo(estilo), zapato, tallazapato));
            } else {
                changeAdapter(new CamisasAdapter(Carrusel.this, Carrusel.this, database.camisaController.getCamisasByEstilo(estilo), camisa, tallacamisa));
            }
            return;
        }
        if (currentAdapter instanceof ZapatosAdapter) {
            if (siguiente) {
                changeAdapter(new AccesoriosAdapter(Carrusel.this, Carrusel.this, database.accesorioController.getAccesoriosByEstilo(estilo), accesorios));
            } else {
                changeAdapter(new PantalonesAdapter(Carrusel.this, Carrusel.this, database.pantalonController.getPantalonesByEstilo(estilo), pantalon, tallapantalon));
            }
            return;
        }
        if (currentAdapter instanceof AccesoriosAdapter) {
            if (!siguiente) {
                changeAdapter(new ZapatosAdapter(Carrusel.this, Carrusel.this, database.zapatoController.getZapatosByEstilo(estilo), zapato, tallazapato));
            }
        }
    }

    // La función de apoyo changeAdapter() le asigna un Adapter nuevo al RecyclerView,
    public void changeAdapter(RecyclerView.Adapter adapter) {
        rcvCarrusel.setAdapter(null);
        rcvCarrusel.getRecycledViewPool().clear();
        rcvCarrusel.setAdapter(adapter);
        currentAdapter = adapter;

        if (!(adapter instanceof EstilosAdapter)) {
            btnAnterior.setVisibility(View.VISIBLE);
        } else {
            btnAnterior.setVisibility(View.INVISIBLE);
        }
        
        if (!(adapter instanceof AccesoriosAdapter)) {
            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseAdapter(true);
                }
            });
        } else {
            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Añadir todos los ítems seleccionados al carrito.
                    SharedPreferences pref = getSharedPreferences("datos", MODE_PRIVATE);
                    int idPedido = pref.getInt("idPedido", 0);
                    int idUsuario = pref.getInt("idUsuario", 0);
                    SharedPreferences.Editor edit = pref.edit();

                    // Si no hay un pedido abierto, se crea uno
                    if (idPedido == 0 || !database.helperController.getPedidoEstado(idPedido).equals("ABIERTO")) {
                        idPedido = (int) database.helperController.newPedido(idUsuario);
                        edit.putInt("idPedido", idPedido);
                        edit.apply();
                    }

                    // Inserta los ítems en base a lo seleccionado en el Carrusel
                    if (camisa.getIdCamisa() != 0 && !database.pedidoProductoController.exists(idPedido, camisa)) database.pedidoProductoController.insertPedidoProducto(idPedido, camisa.getIdCamisa(), PedidoProducto.TIPO_PRODUCTO.CAMISA);
                    if (pantalon.getIdPantalon() != 0 && !database.pedidoProductoController.exists(idPedido, pantalon)) database.pedidoProductoController.insertPedidoProducto(idPedido, pantalon.getIdPantalon(), PedidoProducto.TIPO_PRODUCTO.PANTALON);
                    if (zapato.getIdZapato() != 0 && !database.pedidoProductoController.exists(idPedido, zapato)) database.pedidoProductoController.insertPedidoProducto(idPedido, zapato.getIdZapato(), PedidoProducto.TIPO_PRODUCTO.ZAPATO);
                    for (Accesorio accesorio : accesorios) {
                        if (!database.pedidoProductoController.exists(idPedido, accesorio)) database.pedidoProductoController.insertPedidoProducto(idPedido, accesorio.getIdAccesorio(), PedidoProducto.TIPO_PRODUCTO.ACCESORIO);
                    }

                    // Manda al usuario a la pantalla de Carrito
                    Intent newIntent = new Intent(Carrusel.this, Main.class);
                    newIntent.putExtra("destino", "carrito");
                    startActivity(newIntent);
                }
            });
        }
    }

    // Funciones de apoyo para determinar los ítems seleccionados por el usuario, estos ítems se utilizan
    // para ser insertados al carrito de compras, y para mantener su selección si deciden ir a una pantalla anterior.
    public void checkedEstilo(Estilo estilo) { //Estilo estilo
        this.estilo = estilo;
    }

    public void checkedCamisa(Camisa camisa, boolean selected, String tallacamisa) {
        if (selected) {
            this.camisa = camisa;
            this.tallacamisa = tallacamisa;
        } else {
            this.camisa = emptyCamisa;
            this.tallacamisa = "S";
        }
    }

    public void checkedPantalon(Pantalon pantalon, boolean selected, String tallapantalon) {
        if (selected) {
            this.pantalon = pantalon;
            this.tallapantalon = tallapantalon;
        } else {
            this.pantalon = emptyPantalon;
            this.tallapantalon = "S";
        }
    }

    public void checkedZapato(Zapato zapato, boolean selected, String tallazapato) {
        if (selected) {
            this.zapato = zapato;
            this.tallazapato = tallazapato;
        } else {
            this.zapato = emptyZapato;
            this.tallazapato = "S";
        }
    }

    public void checkedAccesorio(Accesorio accesorio, boolean selected) {
        if (selected) this.accesorios.add(accesorio); else this.accesorios.remove(accesorio);
    }
}