package com.rommelrivera.stylehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoHolder> {
    private Context context;
    private Main activity;
    private int[] pedidos;
    private String[] estados;
    private boolean[] firstTime;

    public PedidoAdapter(Context context, Main activity, int[] pedidos, String[] estados) {
        this.context = context;
        this.activity = activity;
        this.pedidos = pedidos;
        this.estados = estados;

        firstTime = new boolean[pedidos.length];
        for (int i = 0; i < pedidos.length; i++) {
            firstTime[i] = true;
        }
    }

    @NonNull
    @Override
    public PedidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pedidos_selector, parent, false);
        return new PedidoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoHolder holder, int position) {
        holder.init(position);
        firstTime[position] = false;
    }

    @Override
    public int getItemCount() {
        return pedidos.length;
    }

    class PedidoHolder extends RecyclerView.ViewHolder {
        private TextView lblIdPedido;
        private Button btnPedido;
        private TextView lblNumPedido;
        private TextView lblEstadoPedido;

        public PedidoHolder(@NonNull View itemView) {
            super(itemView);

            lblIdPedido = itemView.findViewById(R.id.lblIdPedido);
            btnPedido = itemView.findViewById(R.id.btnPedido);
            lblNumPedido = itemView.findViewById(R.id.lblNumPedido);
            lblEstadoPedido = itemView.findViewById(R.id.lblEstadoPedido);
        }

        public void init(int position) {
            if (firstTime[position]) {
                lblIdPedido.setText(Integer.toString(pedidos[position]));
                lblNumPedido.setText(Integer.toString(position));
                lblEstadoPedido.setText(estados[position]);

                if (lblEstadoPedido.getText().toString().equals("PENDIENTE")) {
                    btnPedido.setBackgroundTintList(context.getColorStateList(R.color.pedido_pendiente));
                } else {
                    btnPedido.setBackgroundTintList(context.getColorStateList(R.color.pedido_finalizado));
                }
            }

            // Manda al usuario a la pantalla Carrito con la información del pedido seleccionado
            btnPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.changeAdapter(new Carrito(context, activity, Integer.parseInt(lblIdPedido.getText().toString())));
                }
            });
        }
    }
}
