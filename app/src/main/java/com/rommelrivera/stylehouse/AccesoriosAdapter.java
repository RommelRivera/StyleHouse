package com.rommelrivera.stylehouse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import database.Accesorio;
import database.PedidoProducto;

public class AccesoriosAdapter extends RecyclerView.Adapter<AccesoriosAdapter.AccesoriosHolder> {

    private Context context;
    private Carrusel activity;
    private Accesorio[] accesorios;
    private ArrayList<Accesorio> prevSelected;

    public AccesoriosAdapter(Context context, Carrusel activity, Accesorio[] accesorios, ArrayList<Accesorio> prevSelected) {
        this.context = context;
        this.activity = activity;
        this.accesorios = accesorios;
        this.prevSelected = prevSelected;
    }

    @NonNull
    @Override
    public AccesoriosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accesorios_selector, parent, false);
        return new AccesoriosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccesoriosHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return accesorios.length;
    }

    class AccesoriosHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private ToggleButton button;
        private TextView accesorio;
        private TextView marca;
        private TextView precio;
        private ImageView imagen;
        private Button descripcion;

        public AccesoriosHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.lblIdAccesorio);
            button = itemView.findViewById(R.id.btnAccesorio);
            accesorio = itemView.findViewById(R.id.lblAccesorio);
            marca = itemView.findViewById(R.id.lblMarcaAccesorio);
            precio = itemView.findViewById(R.id.lblPrecioAccesorio);
            imagen = itemView.findViewById(R.id.imgAccesorio);
            descripcion = itemView.findViewById(R.id.btnDescripcionAccesorio);
        }

        public void init(int position) {
            id.setText(Integer.toString(accesorios[position].getIdAccesorio())); //getIdAccesorio()
            accesorio.setText(accesorios[position].getNombre());
            marca.setText(accesorios[position].getMarca());
            precio.setText("$" + accesorios[position].getPrecio());
            imagen.setImageDrawable(accesorios[position].getImagen());

            button.setBackgroundTintList(activity.getColorStateList(R.color.boton));
            boolean contains = false;
            for (Accesorio accesorio : accesorios) {
                if (contains) break;
                for (Accesorio selected : prevSelected) {
                    if (accesorio.getIdAccesorio() == selected.getIdAccesorio()) contains = true; break;
                }
            }
            button.setChecked(contains);
            if (button.isChecked()) activity.checkedAccesorio(accesorios[position], button.isChecked());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToggleButton button = (ToggleButton) v;
                    activity.checkedAccesorio(accesorios[position], button.isChecked());
                }
            });

            descripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(context, Descripcion.class);
                    newIntent.putExtra("tipoProducto", PedidoProducto.TIPO_PRODUCTO.ACCESORIO);
                    newIntent.putExtra("idProducto", Integer.parseInt(id.getText().toString()));
                    newIntent.putExtra("agregar", false);
                    context.startActivity(newIntent);
                }
            });
        }
    }
}
