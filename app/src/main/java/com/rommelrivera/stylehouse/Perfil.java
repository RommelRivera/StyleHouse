package com.rommelrivera.stylehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import database.Database;
import database.UsuarioController;

public class Perfil extends RecyclerView.Adapter<Perfil.PerfilHolder> {
    private Context context;
    private Main activity;
    private int idUsuario;

    public Perfil(Context context, Main activity) {
        this.context = context;
        this.activity = activity;

        idUsuario = context.getSharedPreferences("datos", Context.MODE_PRIVATE).getInt("idUsuario", 0);
    }

    @NonNull
    @Override
    public PerfilHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_perfil, parent, false);
        return new PerfilHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class PerfilHolder extends RecyclerView.ViewHolder {
        private Button btnLogout;
        private TextView lblPerfilUsername;
        private TextView lblPerfilCorreo;
        private TextView lblPerfilNombre;
        private Button btnModificar;
        private RecyclerView rcvPerfil;
        private Database database;

        public PerfilHolder(@NonNull View itemView) {
            super(itemView);

            btnLogout = itemView.findViewById(R.id.btnLogout);
            lblPerfilUsername = itemView.findViewById(R.id.lblPerfilUsername);
            lblPerfilCorreo = itemView.findViewById(R.id.lblPerfilCorreo);
            lblPerfilNombre = itemView.findViewById(R.id.lblPerfilNombre);
            btnModificar = itemView.findViewById(R.id.btnModificar);
            rcvPerfil = itemView.findViewById(R.id.rcvPerfil);
            database = new Database(context);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcvPerfil.setLayoutManager(layoutManager);
            rcvPerfil.setAdapter(new PedidoAdapter(context, activity, database.helperController.getIdPedidos(idUsuario), database.helperController.getEstadoPedidos(idUsuario)));
        }

        public void init() {
            String[] datos = database.usuarioController.getDataUsuario(idUsuario);
            lblPerfilUsername.setText(datos[0]);
            lblPerfilCorreo.setText(datos[1]);
            lblPerfilNombre.setText(datos[3]);

            // Cierra la sesión
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor edit = context.getSharedPreferences("datos", Context.MODE_PRIVATE).edit();
                    edit.putInt("idUsuario", 0);
                    edit.apply();

                    activity.changeAdapter(new Login(context, activity));
                }
            });

            // Manda al usuario a la pantalla de Modificar
            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.changeAdapter(new Modificar(context, activity, datos));
                }
            });
        }
    }
}