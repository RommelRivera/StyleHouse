package com.rommelrivera.stylehouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.android.material.chip.ChipGroup;

import database.Pantalon;
import database.PedidoProducto;

public class PantalonesAdapter extends RecyclerView.Adapter<PantalonesAdapter.PantalonesHolder> {

    private Context context;
    private Carrusel activity;
    private Pantalon[] pantalones; // Bottom[] bottoms;
    private Pantalon prevSelected;
    private String talla;

    public PantalonesAdapter(Context context, Carrusel activity, Pantalon[] pantalones, Pantalon prevSelected, String talla) {
        this.context = context;
        this.activity = activity;
        this.pantalones = pantalones;
        this.prevSelected = prevSelected;
        this.talla = talla;
    }

    @NonNull
    @Override
    public PantalonesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pantalones_selector, parent, false);
        return new PantalonesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantalonesHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return pantalones.length;
    }

    class PantalonesHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private ChipGroup group;
        private ToggleButton button;
        private TextView pantalon;
        private TextView marca;
        private TextView precio;
        private ImageView imagen;
        private Button descripcion;

        public PantalonesHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.lblIdPantalon);
            group = itemView.findViewById(R.id.grpPantalon);
            button = itemView.findViewById(R.id.btnPantalon);
            pantalon = itemView.findViewById(R.id.lblPantalon);
            marca = itemView.findViewById(R.id.lblMarcaPantalon);
            precio = itemView.findViewById(R.id.lblPrecioPantalon);
            imagen = itemView.findViewById(R.id.imgPantalon);
            descripcion = itemView.findViewById(R.id.btnDescripcionPantalon);
        }

        public void init(int position) {
            id.setText(Integer.toString(pantalones[position].getIdPantalon())); //.getIdPantalon()
            pantalon.setText(pantalones[position].getNombre());
            marca.setText(pantalones[position].getMarca());
            precio.setText("$" + pantalones[position].getPrecio());
            imagen.setImageDrawable(pantalones[position].getImagen());

            for (int i = 0; i < group.getChildCount(); i++) {
                ToggleButton button = (ToggleButton) group.getChildAt(i);
                button.setBackgroundTintList(activity.getColorStateList(R.color.tallas));
                if (i == 0) {
                    button.setChecked(true);
                    update(button); // bottoms[prevSelected].setTalla(getTalla());
                    button.setTextColor(Color.WHITE);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToggleButton button = (ToggleButton) v;
                        if (!button.isChecked()) button.toggle();
                        update(button);
                        if (!PantalonesHolder.this.button.isChecked()) {
                            PantalonesHolder.this.button.setChecked(true);
                            prevSelected = pantalones[position];
                            for (int i = 0; i < getItemCount(); i++) {
                                if (i == position) continue;

                                notifyItemChanged(i);
                            }
                        }
                        activity.checkedPantalon(pantalones[position], PantalonesHolder.this.button.isChecked(), getTalla());
                    }
                });
            }

            button.setBackgroundTintList(activity.getColorStateList(R.color.boton));
            button.setChecked(pantalones[position].getIdPantalon() == prevSelected.getIdPantalon()); // prevSelected == bottoms[position]
            if (button.isChecked()) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    ToggleButton button = (ToggleButton) group.getChildAt(i);
                    if (button.getTextOn().toString().equals(talla)) {
                        button.setChecked(true);
                        update(button);
                    } else {
                        button.setChecked(false);
                    }
                }
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToggleButton button = (ToggleButton) v;

                    int desactivar = -1;
                    for (int i = 0; i < getItemCount(); i++) {
                        if (pantalones[i].getIdPantalon() == prevSelected.getIdPantalon()) desactivar = i;
                    }

                    prevSelected = pantalones[position];
                    activity.checkedPantalon(pantalones[position], button.isChecked(), getTalla()); // bottoms[position]

                    if (!button.isChecked()) {
                        ToggleButton talla = (ToggleButton) group.getChildAt(0);
                        talla.setChecked(true);
                        update(talla);
                    }

                    notifyItemChanged(position);
                    notifyItemChanged(desactivar);
                }
            });
            
            descripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, "Mostrar descripción del pantalón seleccionado.", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(context, Descripcion.class);
                    newIntent.putExtra("tipoProducto", PedidoProducto.TIPO_PRODUCTO.PANTALON);
                    newIntent.putExtra("idProducto", Integer.parseInt(id.getText().toString()));
                    newIntent.putExtra("agregar", false);
                    context.startActivity(newIntent);
                }
            });
        }

        private String getTalla() {
            for (int i = 0; i < group.getChildCount(); i++) {
                ToggleButton button = (ToggleButton) group.getChildAt(i);
                if (button.isChecked()) talla = button.getTextOn().toString();
            }

            return talla;
        }

        private void update(ToggleButton checked) {
            for (int i = 0; i < group.getChildCount(); i++) {
                ToggleButton button = (ToggleButton) group.getChildAt(i);

                if (button == checked) {
                    button.setTextColor(Color.WHITE);
                    continue;
                }

                button.setChecked(false);
                button.setTextColor(Color.BLACK);
            }
        }
    }
}
