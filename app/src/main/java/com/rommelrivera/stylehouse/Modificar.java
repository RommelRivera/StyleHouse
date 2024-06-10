package com.rommelrivera.stylehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import database.Database;

public class Modificar extends RecyclerView.Adapter<Modificar.ModificarHolder> {
    private Context context;
    private Main activity;
    private String[] datos;

    public Modificar(Context context, Main activity, String[] datos) {
        this.context = context;
        this.activity = activity;
        this.datos = datos;
    }

    @NonNull
    @Override
    public ModificarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_modificar, parent, false);
        return new ModificarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModificarHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ModificarHolder extends RecyclerView.ViewHolder {

        private TextView txtModificarUsername;
        private TextView txtModificarCorreo;
        private TextView txtModificarPassword;
        private TextView txtModificarNombre;
        private Button btnGuardar;
        private Button btnRegresar;
        private Database database;

        public ModificarHolder(@NonNull View itemView) {
            super(itemView);

            txtModificarUsername = itemView.findViewById(R.id.txtModificarUsername);
            txtModificarCorreo = itemView.findViewById(R.id.txtModificarCorreo);
            txtModificarPassword = itemView.findViewById(R.id.txtModificarPassword);
            txtModificarNombre = itemView.findViewById(R.id.txtModificarNombre);
            btnRegresar = itemView.findViewById(R.id.btnModificarRegresar);
            btnGuardar = itemView.findViewById(R.id.btnGuardar);
            database = new Database(context);
        }

        public void init() {
            txtModificarUsername.setText(datos[0]);
            txtModificarCorreo.setText(datos[1]);
            txtModificarPassword.setText(datos[2]);
            txtModificarNombre.setText(datos[3]);

            int idUsuario = context.getSharedPreferences("datos", Context.MODE_PRIVATE).getInt("idUsuario", 0);

            // Manda al usuario a la pantalla de Perfil
            btnRegresar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   activity.changeAdapter(new Perfil(context, activity));
               }
            });

            // Actualiza los datos del usuario con la función de apoyo updateUsuario()
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.usuarioController.updateUsuario(idUsuario, txtModificarUsername.getText().toString(), txtModificarCorreo.getText().toString(), txtModificarPassword.getText().toString(), txtModificarNombre.getText().toString());
                    activity.changeAdapter(new Perfil(context, activity));
                }
            });
        }
    }
}