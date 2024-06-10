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

import database.PedidoProducto;
import database.Zapato;

public class ZapatosAdapter extends RecyclerView.Adapter<ZapatosAdapter.ZapatosHolder> {

    private Context context;
    private Carrusel activity;
    private Zapato[] zapatos; // Zapato[] zapatos;
    private Zapato prevSelected;
    private String talla;

    public ZapatosAdapter(Context context, Carrusel activity, Zapato[] zapatos, Zapato prevSelected, String talla) {
        this.context = context;
        this.activity = activity;
        this.zapatos = zapatos;
        this.prevSelected = prevSelected;
        this.talla = talla;
    }

    @NonNull
    @Override
    public ZapatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zapatos_selector, parent, false);
        return new ZapatosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZapatosHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return zapatos.length;
    }

    class ZapatosHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private ChipGroup group;
        private ToggleButton button;
        private TextView zapato;
        private TextView marca;
        private TextView precio;
        private ImageView imagen;
        private Button descripcion;

        public ZapatosHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.lblIdZapato);
            group = itemView.findViewById(R.id.grpZapato);
            button = itemView.findViewById(R.id.btnZapato);
            zapato = itemView.findViewById(R.id.lblZapato);
            marca = itemView.findViewById(R.id.lblMarcaZapato);
            precio = itemView.findViewById(R.id.lblPrecioZapato);
            imagen = itemView.findViewById(R.id.imgZapato);
            descripcion = itemView.findViewById(R.id.btnDescripcionZapato);
        }

        public void init(int position) {
            id.setText(Integer.toString(zapatos[position].getIdZapato())); //getIdZapato()
            zapato.setText(zapatos[position].getNombre());
            marca.setText(zapatos[position].getMarca());
            precio.setText("$" + zapatos[position].getPrecio());
            imagen.setImageDrawable(zapatos[position].getImagen());

            for (int i = 0; i < group.getChildCount(); i++) {
                ToggleButton button = (ToggleButton) group.getChildAt(i);
                button.setBackgroundTintList(activity.getColorStateList(R.color.tallas));
                if (i == 0) {
                    button.setChecked(true);
                    update(button);
                    button.setTextColor(Color.WHITE);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToggleButton button = (ToggleButton) v;
                        if (!button.isChecked()) button.toggle();
                        update(button);
                        if(!ZapatosHolder.this.button.isChecked()) {
                            ZapatosHolder.this.button.setChecked(true);
                            prevSelected = zapatos[position];
                            for (int i = 0; i < getItemCount(); i++) {
                                if (i == position) continue;

                                notifyItemChanged(i);
                            }
                        }
                        activity.checkedZapato(zapatos[position], ZapatosHolder.this.button.isChecked(), getTalla());
                    }
                });
            }

            button.setBackgroundTintList(activity.getColorStateList(R.color.boton));
            button.setChecked(zapatos[position].getIdZapato() == prevSelected.getIdZapato());
            if (button.isChecked()) {
                activity.checkedZapato(zapatos[position], button.isChecked(), getTalla());

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
                        if (zapatos[i].getIdZapato() == prevSelected.getIdZapato()) desactivar = i;
                    }

                    prevSelected = zapatos[position];
                    activity.checkedZapato(zapatos[position], button.isChecked(), getTalla());

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
                    // Toast.makeText(context, "Mostrar descripción del zapato seleccionado", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(context, Descripcion.class);
                    newIntent.putExtra("tipoProducto", PedidoProducto.TIPO_PRODUCTO.ZAPATO);
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
