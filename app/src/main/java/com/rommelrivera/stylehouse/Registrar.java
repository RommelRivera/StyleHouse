package com.rommelrivera.stylehouse;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.Database;

public class Registrar extends RecyclerView.Adapter<Registrar.RegistrarHolder> {

    private Context context;
    private Main activity;

    public Registrar(Context context, Main activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RegistrarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_registrar, parent, false);
        return new RegistrarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrarHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class RegistrarHolder extends RecyclerView.ViewHolder {
        private TextView txtRegistrarUsername;
        private TextView txtRegistrarCorreo;
        private TextView txtRegistrarPassword;
        private TextView txtRegistrarNombre;
        private Button btnRegresar;
        private Button btnRegistrar;

        public RegistrarHolder(@NonNull View itemView) {
            super(itemView);

            txtRegistrarUsername = itemView.findViewById(R.id.txtRegistrarUsername);
            txtRegistrarCorreo = itemView.findViewById(R.id.txtRegistrarCorreo);
            txtRegistrarPassword = itemView.findViewById(R.id.txtRegistrarPassword);
            txtRegistrarNombre = itemView.findViewById(R.id.txtRegistrarNombre);
            btnRegresar = itemView.findViewById(R.id.btnRegistrarRegresar);
            btnRegistrar = itemView.findViewById(R.id.btnRegistrar);
        }

        public void init() {
            btnRegresar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   activity.changeAdapter(new Login(context, activity));
               }
            });

            btnRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valid = true;
                    String usuario = txtRegistrarUsername.getText().toString();
                    String correo = txtRegistrarCorreo.getText().toString();
                    String password = txtRegistrarPassword.getText().toString();
                    String nombre = txtRegistrarNombre.getText().toString();
                    // Valida que se haya ingresado un nombre de usuario
                    if (usuario.trim().isEmpty()) {
                        valid = false;
                        Toast.makeText(context, "Por favor ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                    // Valida que se haya ingresado un correo
                    if (correo.trim().isEmpty() && valid) {
                        valid = false;
                        Toast.makeText(context, "Por favor ingrese un correo.", Toast.LENGTH_SHORT).show();
                    }
                    // Valida que se haya ingresado una contraseña
                    if (password.trim().isEmpty() && valid) {
                        valid = false;
                        Toast.makeText(context, "Por favor ingrese una contraseña.", Toast.LENGTH_SHORT).show();
                    }
                    // Valida que se haya ingresado un nombre
                    if (nombre.trim().isEmpty() && valid) {
                        valid = false;
                        Toast.makeText(context, "Por favor ingrese su nombre completo.", Toast.LENGTH_SHORT).show();
                    }
                    if (valid) {
                        Database database = new Database(context);
                        // Valida que no exista un usuario con el correo brindado
                        if (database.usuarioController.checkUsuarioExistsCorreo(correo)) {
                            Toast.makeText(context, "Ya existe un usuario con ese correo.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Inserta al usuario nuevo a la base de datos e inicia sesión, y manda al usuario a la pantalla de Perfil
                            database.usuarioController.insertUsuario(usuario, correo, password, nombre);
                            SharedPreferences pref = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putInt("idUsuario", database.usuarioController.getIdUsuario(correo, password));
                            edit.apply();
                            if (pref.getInt("idPedido", 0) != 0) { database.helperController.setPedidoUsuario(pref.getInt("idPedido", 0), pref.getInt("idUsuario", 0)); }
                            activity.changeAdapter(new Perfil(context, activity));
                        }
                    }
                }
            });
        }
    }
}