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

import database.Camisa;
import database.PedidoProducto;

public class CamisasAdapter extends RecyclerView.Adapter<CamisasAdapter.CamisasHolder> {
    private Context context;
    private Carrusel activity;
    private Camisa[] camisas;
    private Camisa prevSelected;
    private String talla;

    public CamisasAdapter(Context context, Carrusel activity, Camisa[] camisas, Camisa prevSelected, String talla) {
        this.context = context;
        this.activity = activity;
        this.camisas = camisas;
        this.prevSelected = prevSelected;
        this.talla = talla;
    }

    @NonNull
    @Override
    public CamisasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.camisas_selector, parent, false);
        return new CamisasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CamisasHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return camisas.length;
    }

    class CamisasHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private ChipGroup group;
        private ToggleButton button;
        private TextView camisa;
        private TextView precio;
        private TextView marca;
        private ImageView imagen;
        private Button descripcion;

        public CamisasHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.lblIdCamisa);
            group = itemView.findViewById(R.id.grpCamisa);
            button = itemView.findViewById(R.id.btnCamisa);
            camisa = itemView.findViewById(R.id.lblCamisa);
            marca = itemView.findViewById(R.id.lblMarcaCamisa);
            precio = itemView.findViewById(R.id.lblPrecioCamisa);
            imagen = itemView.findViewById(R.id.imgCamisa);
            descripcion = itemView.findViewById(R.id.btnDescripcionCamisa);
        }

        public void init(int position) {
            id.setText(Integer.toString(camisas[position].getIdCamisa()));
            camisa.setText(camisas[position].getNombre());
            marca.setText(camisas[position].getMarca());
            precio.setText("$" + camisas[position].getPrecio());
            imagen.setImageDrawable(camisas[position].getImagen());

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
                        if (!CamisasHolder.this.button.isChecked()) {
                            CamisasHolder.this.button.setChecked(true);
                            prevSelected = camisas[position];
                            for (int i = 0; i < getItemCount(); i++) {
                                if (i == position) continue;

                                notifyItemChanged(i);
                            }
                        }
                        activity.checkedCamisa(camisas[position], CamisasHolder.this.button.isChecked(), getTalla());
                    }
                });
            }

            button.setBackgroundTintList(activity.getColorStateList(R.color.boton));
            button.setChecked(camisas[position].getIdCamisa() == prevSelected.getIdCamisa());
            if (button.isChecked()) {
                activity.checkedCamisa(camisas[position], button.isChecked(), getTalla());

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
                        if (camisas[i].getIdCamisa() == prevSelected.getIdCamisa()) desactivar = i;
                    }

                    prevSelected = camisas[position];
                    activity.checkedCamisa(camisas[position], button.isChecked(), getTalla());

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
                    Intent newIntent = new Intent(context, Descripcion.class);
                    newIntent.putExtra("tipoProducto", PedidoProducto.TIPO_PRODUCTO.CAMISA);
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
