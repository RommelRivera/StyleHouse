package com.rommelrivera.stylehouse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import database.Producto;

public class GridCatalogo extends RecyclerView.Adapter<GridCatalogo.GridHolder> {
    private Context context;
    private ArrayList<Producto> productos;
    private boolean[] firstTime;

    public GridCatalogo(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;

        firstTime = new boolean[productos.size()];
        for (int i = 0; i < firstTime.length; i++) {
            firstTime[i] = true;
        }
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_catalogo, parent, false);
        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder holder, int position) {
        holder.init(position);
        firstTime[position] = false;
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class GridHolder extends RecyclerView.ViewHolder {
        private TextView lblIdProducto;
        private Button btnProducto;
        private ImageView imgGridProducto;
        private TextView lblGridNombre;
        private TextView lblGridMarca;
        private TextView lblGridPrecio;

        public GridHolder(@NonNull View itemView) {
            super(itemView);

            lblIdProducto = itemView.findViewById(R.id.lblIdProducto);
            btnProducto = itemView.findViewById(R.id.btnProducto);
            imgGridProducto = itemView.findViewById(R.id.imgGridProducto);
            lblGridNombre = itemView.findViewById(R.id.lblGridNombre);
            lblGridMarca = itemView.findViewById(R.id.lblGridMarca);
            lblGridPrecio = itemView.findViewById(R.id.lblGridPrecio);
        }

        public void init(int position) {
            if (firstTime[position]) {
                lblIdProducto.setText(Integer.toString(productos.get(position).getIdProducto()));
                imgGridProducto.setImageDrawable(productos.get(position).getImagen());
                lblGridNombre.setText(productos.get(position).getNombre());
                lblGridMarca.setText(productos.get(position).getMarca());
                lblGridPrecio.setText("$" + productos.get(position).getPrecio());
            }

            btnProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(context, Descripcion.class);
                    newIntent.putExtra("tipoProducto", productos.get(position).getTipoProducto());
                    newIntent.putExtra("idProducto", productos.get(position).getIdProducto());
                    context.startActivity(newIntent);
                }
            });
        }
    }
}
