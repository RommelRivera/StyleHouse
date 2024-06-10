package com.rommelrivera.stylehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import database.Accesorio;
import database.Camisa;
import database.Database;
import database.Pantalon;
import database.PedidoProducto;
import database.Zapato;

public class Descripcion extends AppCompatActivity {

    private Button btnDescripcionRegresar;
    private TextView lblDescripcionNombre;
    private TextView lblDescripcionMarca;
    private TextView lblDescripcionTalla;
    private TextView lblDescripcionPrecio;
    private ImageView imgDescripcion;
    private Button btnAgregarCarrito;
    private TextView lblDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        // Inicializar componentes del Layout
        btnDescripcionRegresar = findViewById(R.id.btnDescripcionRegresar);
        lblDescripcionNombre = findViewById(R.id.lblDescripcionNombre);
        lblDescripcionMarca = findViewById(R.id.lblDescripcionMarca);
        lblDescripcionTalla = findViewById(R.id.lblDescripcionTalla);
        lblDescripcionPrecio = findViewById(R.id.lblDescripcionPrecio);
        imgDescripcion = findViewById(R.id.imgDescripcion);
        btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        lblDescripcion = findViewById(R.id.lblDescripcion);

        // Recuperar los datos pertinentes del producto
        Bundle extras = getIntent().getExtras();
        boolean agregar = extras.getBoolean("agregar", true);
        int tipoProducto = extras.getInt("tipoProducto", 0);
        int idProducto = extras.getInt("idProducto", 0);
        Database database = new Database(this);

        if (!agregar) {
            btnAgregarCarrito.setVisibility(View.GONE);
        }

        btnAgregarCarrito.setBackgroundTintList(getColorStateList(R.color.menu));

        int idPedido = getSharedPreferences("datos", MODE_PRIVATE).getInt("idPedido", 0);
        ArrayList<PedidoProducto> pedidoProductos = database.pedidoProductoController.getPedidosProductosByPedido(idPedido);

        // Revisa entre todos los productos actualmente presentes en el carrito,
        // si ya está, no puede agregarlo desde esta pantalla.
        for (PedidoProducto pedidoProducto : pedidoProductos) {
            if (pedidoProducto.getTipoProducto() == tipoProducto) {
                if (pedidoProducto.getIdProducto() == idProducto) {
                    btnAgregarCarrito.setEnabled(false);
                    btnAgregarCarrito.setText("Producto ya se encuentra en el carrito");
                    btnAgregarCarrito.setBackgroundTintList(getColorStateList(R.color.menu));
                }
            }
        }

        // Regresar a la lista de productos
        btnDescripcionRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Agrega el producto al carrito
        btnAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("datos", MODE_PRIVATE);
                int idPedido = pref.getInt("idPedido", 0);
                int idUsuario = pref.getInt("idUsuario", 0);
                SharedPreferences.Editor edit = pref.edit();

                // Si no hay un pedido abierto, se crea uno
                if (idPedido == 0 || !database.helperController.getPedidoEstado(idPedido).equals("ABIERTO")) {
                    idPedido = (int) database.helperController.newPedido(idUsuario);
                    edit.putInt("idPedido", idPedido);
                    edit.apply();
                }

                // Inserta el producto al carrito
                database.pedidoProductoController.insertPedidoProducto(idPedido, idProducto, tipoProducto);
                finish();
            }
        });

        // Recupera el producto desde su tabla, y asigna sus datos a los componentes del Layout
        switch (tipoProducto) {
            case PedidoProducto.TIPO_PRODUCTO.CAMISA:
                Camisa camisa = database.camisaController.getCamisaById(idProducto);
                lblDescripcionNombre.setText(camisa.getNombre());
                lblDescripcionMarca.setText(camisa.getMarca());
                lblDescripcionTalla.setText(camisa.getTalla());
                lblDescripcionPrecio.setText("$" + Double.toString(camisa.getPrecio()));
                imgDescripcion.setImageDrawable(camisa.getImagen());
                lblDescripcion.setText(camisa.getDescripcion());
                break;
            case PedidoProducto.TIPO_PRODUCTO.PANTALON:
                Pantalon pantalon = database.pantalonController.getPantalonById(idProducto);
                lblDescripcionNombre.setText(pantalon.getNombre());
                lblDescripcionMarca.setText(pantalon.getMarca());
                lblDescripcionTalla.setText(pantalon.getTalla());
                lblDescripcionPrecio.setText("$" + Double.toString(pantalon.getPrecio()));
                imgDescripcion.setImageDrawable(pantalon.getImagen());
                lblDescripcion.setText(pantalon.getDescripcion());
                break;
            case PedidoProducto.TIPO_PRODUCTO.ZAPATO:
                Zapato zapato = database.zapatoController.getZapatoById(idProducto);
                lblDescripcionNombre.setText(zapato.getNombre());
                lblDescripcionMarca.setText(zapato.getMarca());
                lblDescripcionTalla.setText(zapato.getTalla());
                lblDescripcionPrecio.setText("$" + Double.toString(zapato.getPrecio()));
                imgDescripcion.setImageDrawable(zapato.getImagen());
                lblDescripcion.setText(zapato.getDescripcion());
                break;
            case PedidoProducto.TIPO_PRODUCTO.ACCESORIO:
                Accesorio accesorio = database.accesorioController.getAccesorioById(idProducto);
                lblDescripcionNombre.setText(accesorio.getNombre());
                lblDescripcionMarca.setText(accesorio.getMarca());
                lblDescripcionTalla.setVisibility(View.GONE);
                lblDescripcionPrecio.setText("$" + Double.toString(accesorio.getPrecio()));
                imgDescripcion.setImageDrawable(accesorio.getImagen());
                lblDescripcion.setText(accesorio.getDescripcion());
                break;
        }
    }
}