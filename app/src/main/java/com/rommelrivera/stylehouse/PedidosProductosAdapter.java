package com.rommelrivera.stylehouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import database.PedidoProducto;

public class PedidosProductosAdapter extends RecyclerView.Adapter<PedidosProductosAdapter.PedidosProductosHolder> {
    private Context context;
    private Carrito.CarritoHolder activity;
    private ArrayList<PedidoProducto> pedidosProductos;
    private double total = 0;

    public PedidosProductosAdapter(Context context, Carrito.CarritoHolder activity, ArrayList<PedidoProducto> pedidosProductos, double total) {
        this.context = context;
        this.activity = activity;
        this.pedidosProductos = pedidosProductos;

        // Inicializa el total del pedido con los precios de los productos de la lista
        for (PedidoProducto pedidoProducto : pedidosProductos) {
            this.total += pedidoProducto.getPrecio();
        }
    }

    @NonNull
    @Override
    public PedidosProductosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_carrito, parent, false);
        return new PedidosProductosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosProductosHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        return pedidosProductos.size();
    }

    class PedidosProductosHolder extends RecyclerView.ViewHolder {

        private LinearLayout lloCantidad;
        private Button btnAgregarProducto;
        private Button btnQuitarProducto;
        private Button ibtEliminarProducto;
        private TextView producto;
        private TextView marca;
        private TextView precio;
        private ImageView imagen;
        private TextView cantidad;
        private Database database;

        public PedidosProductosHolder(@NonNull View itemView) {
            super(itemView);

            lloCantidad = itemView.findViewById(R.id.lloCantidad);
            btnAgregarProducto = itemView.findViewById(R.id.btnAgregarItem);
            btnQuitarProducto = itemView.findViewById(R.id.btnQuitarItem);
            ibtEliminarProducto = itemView.findViewById(R.id.ibtEliminarProducto);
            producto = itemView.findViewById(R.id.lblProducto);
            marca = itemView.findViewById(R.id.lblMarcaProducto);
            precio = itemView.findViewById(R.id.lblPrecioProducto);
            imagen = itemView.findViewById(R.id.imgProducto);
            cantidad = itemView.findViewById(R.id.lblCantidadProducto);
            database = new Database(context);
        }

        public void init(int position) {
            String estado = database.helperController.getPedidoEstado(context.getSharedPreferences("datos", Context.MODE_PRIVATE).getInt("idPedido", 0));
            // Deshabilita la capacidad de agregar o quitar ítems si es un pedido ya realizado
            if (estado.equals("PENDIENTE") || estado.equals("FINALIZADO")) { lloCantidad.setVisibility(View.GONE); }
            producto.setText(pedidosProductos.get(position).getNombre());
            marca.setText(pedidosProductos.get(position).getMarca());
            precio.setText("$" + pedidosProductos.get(position).getPrecio());
            imagen.setImageDrawable(pedidosProductos.get(position).getImagen());
            activity.setTotal(total);

            // Aumenta la cantidad del ítem por 1, y actualiza el total del pedido
            btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newCantidad = Integer.parseInt(cantidad.getText().toString()) + 1;
                    if (cantidad.getText().toString().equals("1")) {
                        btnQuitarProducto.setVisibility(View.VISIBLE);
                        ibtEliminarProducto.setVisibility(View.GONE);
                    }
                    total += pedidosProductos.get(position).getPrecio();
                    activity.setTotal(total);
                    cantidad.setText(Integer.toString(newCantidad));
                    database.pedidoProductoController.setCantidadPedidoProducto(pedidosProductos.get(position).getIdPedidoProducto(), newCantidad);
                }
            });

            // Reduce la cantidad del ítem por 1, y actualiza el total del pedido
            btnQuitarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newCantidad = Integer.parseInt(cantidad.getText().toString()) - 1;
                    if (newCantidad == 1) {
                        btnQuitarProducto.setVisibility(View.GONE);
                        ibtEliminarProducto.setVisibility(View.VISIBLE);
                    }
                    total -= pedidosProductos.get(position).getPrecio();
                    activity.setTotal(total);
                    cantidad.setText(Integer.toString(newCantidad));
                    database.pedidoProductoController.setCantidadPedidoProducto(pedidosProductos.get(position).getIdPedidoProducto(), newCantidad);
                }
            });

            // Elimina el ítem del carrito y actualiza el total del pedido
            ibtEliminarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total -= pedidosProductos.get(position).getPrecio();
                    activity.setTotal(total);
                    database.pedidoProductoController.deletePedidoProducto(pedidosProductos.get(position).getIdPedidoProducto());
                    pedidosProductos.remove(position);

                    notifyDataSetChanged();
                }
            });
        }
    }
}
