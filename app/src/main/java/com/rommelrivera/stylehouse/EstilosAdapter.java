package com.rommelrivera.stylehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import database.Estilo;

public class EstilosAdapter extends RecyclerView.Adapter<EstilosAdapter.EstilosHolder> {
    private Context context;
    private Carrusel activity;
    private Estilo[] estilos; // Estilo[] estilos;
    private Estilo prevSelected; // Estilo prevSelected;

    public EstilosAdapter(Context context, Carrusel activity, Estilo[] estilos, Estilo prevSelected) {
        this.context = context;
        this.activity = activity;
        this.estilos = estilos;
        this.prevSelected = prevSelected;
    }

    @NonNull
    @Override
    public EstilosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.estilos_selector, parent, false);
        return new EstilosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstilosHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return estilos.length;
    }

    class EstilosHolder extends RecyclerView.ViewHolder {

        private ToggleButton button;
        private TextView estilo;
        private ImageView imagen;

        public EstilosHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.btnEstilo);
            estilo = itemView.findViewById(R.id.txtEstilo);
            imagen = itemView.findViewById(R.id.imgEstilo);
        }

        public void init(int position) {
            estilo.setText(estilos[position].getNombre());
            imagen.setImageDrawable(estilos[position].getImagen());

            button.setBackgroundTintList(activity.getColorStateList(R.color.boton));
            button.setChecked(estilos[position].getIdEstilo() == prevSelected.getIdEstilo());
            if (button.isChecked()) activity.checkedEstilo(estilos[position]); // prevSelected == estilos[position]
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToggleButton button = (ToggleButton) v;
                    if (!button.isChecked()) button.toggle();

                    int desactivar = -1;
                    for (int i = 0; i <getItemCount(); i++){
                        if (estilos[i].getIdEstilo() == prevSelected.getIdEstilo()) desactivar = i;
                    }

                    prevSelected = estilos[position];
                    activity.checkedEstilo(estilos[position]); // estilos[position]

                    notifyItemChanged(position);
                    notifyItemChanged(desactivar);
                }
            });
        }
    }
}
