package com.rommelrivera.stylehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import database.Database;

public class Main extends AppCompatActivity {

    private RecyclerView rcvMain;
    private ImageButton carrusel;
    private ImageButton catalogo;
    private ImageButton carrito;
    private ImageButton perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carrusel = findViewById(R.id.carrusel);
        catalogo = findViewById(R.id.catalogo);
        carrito = findViewById(R.id.carrito);
        perfil = findViewById(R.id.perfil);

        catalogo.setBackgroundTintList(getColorStateList(R.color.menu));
        carrito.setBackgroundTintList(getColorStateList(R.color.menu));
        perfil.setBackgroundTintList(getColorStateList(R.color.menu));

        // Desactiva la capacidad de scroll del RecyclerView principal para que
        // los RecyclerView anidados puedan hacer su scroll sin problemas
        rcvMain = findViewById(R.id.rcvMain);
        rcvMain.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        chooseAdapter();

        // Manda al usuario a la pantalla del Carrusel
        carrusel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, Carrusel.class));
                finish();
            }
        });

        // Manda al usuario a la pantalla de Catálogo
        catalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogo.setEnabled(false);
                carrito.setEnabled(true);
                perfil.setEnabled(true);

                changeAdapter(new Catalogo(Main.this, Main.this));
            }
        });

        // Manda al usuario a la pantalla de Carrito
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogo.setEnabled(true);
                carrito.setEnabled(false);
                perfil.setEnabled(true);

                changeAdapter(new Carrito(Main.this, Main.this));
            }
        });

        // Manda al usuario a la pantalla de Perfil
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogo.setEnabled(true);
                carrito.setEnabled(true);
                perfil.setEnabled(false);

                // Si no está registrado el usuario, lo manda a la pantalla de Login
                int idUsuario = getSharedPreferences("datos", MODE_PRIVATE).getInt("idUsuario", 0);
                if (idUsuario == 0) {
                    changeAdapter(new Login(Main.this, Main.this));
                } else {
                    changeAdapter(new Perfil(Main.this, Main.this));
                }
            }
        });
    }

    // Función de apoyo para cambiar el Adapter de la pantalla en base al parámetro enviado por otra pantalla
    public void chooseAdapter() {
        Bundle extras = getIntent().getExtras();
        String destino = extras.getString("destino");
        switch (destino) {
            case "catalogo":
                changeAdapter(new Catalogo(this, this)); // new Catalogo(this, this)
                catalogo.setEnabled(false);
                break;
            case "carrito":
                Database database = new Database(this);
                SharedPreferences pref = getSharedPreferences("datos", MODE_PRIVATE);
                int idPedido = pref.getInt("idPedido", 0);
                int idUsuario = pref.getInt("idUsuario", 0);
                SharedPreferences.Editor edit = pref.edit();
                // Si no hay un pedido abierto, se crea uno
                if (idPedido == 0 || !database.helperController.getPedidoEstado(idPedido).equals("ABIERTO")) {
                    idPedido = (int) database.helperController.newPedido(idUsuario);
                    edit.putInt("idPedido",0);
                    edit.apply();
                }
                changeAdapter(new Carrito(this, this));
                carrito.setEnabled(false);
                break;
            case "perfil":
                // Si el usuario no está registrado, lo manda a la pantalla de Login
                if (getSharedPreferences("datos", MODE_PRIVATE).getInt("idUsuario", 0) == 0) {
                    changeAdapter(new Login(this, this));
                } else {
                    changeAdapter(new Perfil(this, this));
                }
                perfil.setEnabled(false);
                break;
        }
    }

    // Función de apoyo para cambiar el Adapter de la pantalla actual
    public void changeAdapter(RecyclerView.Adapter adapter) {
        rcvMain.setAdapter(null);
        rcvMain.getRecycledViewPool().clear();
        rcvMain.setAdapter(adapter);
    }
}