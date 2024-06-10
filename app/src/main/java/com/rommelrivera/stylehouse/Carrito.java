package com.rommelrivera.stylehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import database.PedidoProducto;

public class Carrito extends RecyclerView.Adapter<Carrito.CarritoHolder> {
    private Context context;
    private Main activity;
    private int idPedido;
    private boolean perfil = false;

    public Carrito(Context context, Main activity) {
        this.context = context;
        this.activity = activity;

        SharedPreferences pref = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        this.idPedido = pref.getInt("idPedido", 0);
        int idUsuario = pref.getInt("idUsuario", 0);
        SharedPreferences.Editor edit = pref.edit();
        Database database = new Database(context);

        // Si no hay un pedido abierto, se crea uno
        if (idPedido == 0 || !database.helperController.getPedidoEstado(idPedido).equals("ABIERTO")) {
            idPedido = (int) database.helperController.newPedido(idUsuario);
            edit.putInt("idPedido", idPedido);
            edit.apply();
        }
    }

    public Carrito(Context context, Main activity, int idPedido) {
        this.context = context;
        this.activity = activity;
        this.idPedido = idPedido;
        this.perfil = true;
    }

    @NonNull
    @Override
    public CarritoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_carrito, parent, false);
        return new CarritoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class CarritoHolder extends RecyclerView.ViewHolder {
        private RecyclerView rcvCarrito;
        private Button btnCarritoRegresar;
        private TextView lblTotalCompra;
        private Button btnPedir;
        private TextView lblEstado;
        private Database database;

        public CarritoHolder(View itemView) {
            super(itemView);

            // Inicializar los elementos del Layout
            database = new Database(context);
            rcvCarrito = itemView.findViewById(R.id.rcvCarrito);
            btnCarritoRegresar = itemView.findViewById(R.id.btnCarritoRegresar);
            lblTotalCompra = itemView.findViewById(R.id.lblTotalCompra);
            btnPedir = itemView.findViewById(R.id.btnPedir);
            lblEstado = itemView.findViewById(R.id.lblEstado);
            String estado = database.helperController.getPedidoEstado(idPedido);

            // Si es un pedido ya realizado, muestra el estado actual del pedido
            if (estado.equals("PENDIENTE")) {
                lblEstado.setBackgroundColor(context.getColor(R.color.pedido_pendiente));
                lblEstado.setText("PENDIENTE");
            } else if (estado.equals("FINALIZADO")){
                lblEstado.setBackgroundColor(context.getColor(R.color.pedido_finalizado));
                lblEstado.setText("FINALIZADO");
            }
        }

        public void init() {
            // Muestra o esconde elementos dependiendo si es un pedido abierto o uno ya realizado
            if (perfil) {
                btnCarritoRegresar.setVisibility(View.VISIBLE);
                btnPedir.setVisibility(View.GONE);
                lblEstado.setVisibility(View.VISIBLE);
            }

            // Asigna el Adapter con los productos seleccionados al RecyclerView
            rcvCarrito.setLayoutManager(new LinearLayoutManager(context));
            PedidosProductosAdapter adapter = new PedidosProductosAdapter(context, this, database.pedidoProductoController.getPedidosProductosByPedido(idPedido), database.pedidoProductoController.getTotalPedido(idPedido));
            rcvCarrito.setAdapter(adapter);

            // Si se está viendo a un pedido realizado, manda al usuario a la pantalla de Perfil
            btnCarritoRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                    Intent newIntent = new Intent(context, Main.class);
                    newIntent.putExtra("destino", "perfil");
                    context.startActivity(newIntent);
                }
            });

            // Asignar el estilo al botón
            btnPedir.setEnabled(false);
            btnPedir.setBackgroundTintList(context.getColorStateList(R.color.menu));
            btnPedir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Si quiere realizar un pedido sin iniciar sesión, se manda al usuario a la pantalla Login
                    if (context.getSharedPreferences("datos", Context.MODE_PRIVATE).getInt("idUsuario", 0) == 0) {
                        activity.finish();
                        Intent newIntent = new Intent(context, Main.class);
                        newIntent.putExtra("destino", "perfil");
                        context.startActivity(newIntent);
                    // Si no, se manda al usuario a ver el pedido realizado
                    } else {
                        database.helperController.setPedidoEstado(idPedido, "PENDIENTE");
                        activity.changeAdapter(new Carrito(context, activity, idPedido));
                    }
                }
            });
        }

        // Función de apoyo para determinar el total del pedido de forma dinámica
        public void setTotal(double total) {
            lblTotalCompra.setText("$" + total);

            if (total == 0) {
                btnPedir.setEnabled(false);
            } else {
                btnPedir.setEnabled(true);
            }
        }
    }
}