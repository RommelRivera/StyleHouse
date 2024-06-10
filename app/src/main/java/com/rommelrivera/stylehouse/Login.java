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

public class Login extends RecyclerView.Adapter<Login.LoginHolder> {

    private Context context;
    private Main activity;
    public Login(Context context, Main activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LoginHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_login, parent, false);
        return new LoginHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class LoginHolder extends RecyclerView.ViewHolder {
        private int idUsuario;
        private TextView txtLoginCorreoUsername;
        private TextView txtLoginPassword;
        private Button btnIngresar;
        private Button btnLoginRegistrar;

        public LoginHolder(@NonNull View itemView) {
            super(itemView);

            txtLoginCorreoUsername = itemView.findViewById(R.id.txtLoginCorreoUsername);
            txtLoginPassword = itemView.findViewById(R.id.txtLoginPassword);
            btnIngresar = itemView.findViewById(R.id.btnIngresar);
            btnLoginRegistrar = itemView.findViewById(R.id.btnLoginRegistrar);
        }

        public void init() {
            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valid = true;
                    String correoUsername = txtLoginCorreoUsername.getText().toString();
                    String password = txtLoginPassword.getText().toString();
                    // Valida que se haya ingresado un correo
                    if (correoUsername.trim().isEmpty()) {
                        valid = false;
                        Toast.makeText(context, "Por favor, ingrese un correo, o nombre de usuario.", Toast.LENGTH_SHORT).show();
                    }
                    // Valida que se haya ingresado una contraseña
                    if (password.trim().isEmpty() && valid) {
                        valid = false;
                        Toast.makeText(context, "Por favor, ingrese la contraseña.", Toast.LENGTH_SHORT).show();
                    }
                    if (valid) {
                        Database database = new Database(context);
                        // Revisa si existe un usuario con el correo o nombre de usuario brindado
                        if (!database.usuarioController.checkUsuarioExistsCorreo(correoUsername) && !database.usuarioController.checkUsuarioExistsUsername(correoUsername)) {
                            Toast.makeText(context, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                        // Revisa si la contraseña es la correcta para el usuario que desea ingresar
                        } else if (!database.usuarioController.checkUsuarioCredentials(correoUsername, password)) {
                            Toast.makeText(context, "La contraseña es incorrecta.", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences pref = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = pref.edit();
                            // Actualiza el dato de la sesión iniciada actualmente, para que se mantenga aún después de cerrar la app
                            edit.putInt("idUsuario", database.usuarioController.getIdUsuario(correoUsername, password));
                            edit.apply();
                            // Si hay un pedido abierto de antes de iniciar sesión, se asigna este pedido al usuario recién ingresado
                            if (pref.getInt("idPedido", 0) != 0) { database.helperController.setPedidoUsuario(pref.getInt("idPedido", 0), pref.getInt("idUsuario", 0)); }
                            // Manda al usuario a la pantalla de Perfil
                            activity.changeAdapter(new Perfil(context, activity));
                        }
                    }
                }
            });

            // Manda al usuario a la pantalla de Registrar
            btnLoginRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.changeAdapter(new Registrar(context, activity));
                }
            });
        }
    }
}
