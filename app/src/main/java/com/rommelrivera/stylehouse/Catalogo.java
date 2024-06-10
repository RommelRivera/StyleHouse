package com.rommelrivera.stylehouse;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.Database;
import database.Producto;

public class Catalogo extends RecyclerView.Adapter<Catalogo.CatalogoHolder> {
    private Context context;
    private Main activity;

    public Catalogo(Context context, Main activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CatalogoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_catalogo, parent, false);
        return new CatalogoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class CatalogoHolder extends RecyclerView.ViewHolder {
        private LinearLayout lloCategorias;
        private Button btnCaballeros;
        private Button btnDamas;
        private Button btnZapatos;
        private Button btnAccesorios;
        private boolean caballeroDama;

        private LinearLayout lloCatalogo;
        private Button btnCatalogoRegresar;
        private Button btnFiltros;
        private TextView txtBuscar;

        private LinearLayout lloFiltros;
        private Spinner spnEstilo;
        private Spinner spnMarca;
        private Spinner spnTalla;
        private Spinner spnSucursal;
        private TextView txtDesde;
        private TextView txtHasta;
        private Button btnLimpiarFiltros;
        private Button btnAplicarFiltros;

        String filtroMarca;
        String filtroEstilo;
        String filtroTalla;
        String filtroSucursal;

        private ChipGroup chgTipo;
        private Chip chpCamisas;
        private Chip chpPantalones;

        String filtroTipo;
        int categoria;
        ArrayList<Producto> productos;

        private RecyclerView rcvCatalogo;

        private Database database;

        public CatalogoHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializar componentes del Layout
            lloCategorias = itemView.findViewById(R.id.lloCategorias);
            btnCaballeros = itemView.findViewById(R.id.btnCaballeros);
            btnDamas = itemView.findViewById(R.id.btnDamas);
            btnZapatos = itemView.findViewById(R.id.btnZapatos);
            btnAccesorios = itemView.findViewById(R.id.btnAccesorios);

            lloCatalogo = itemView.findViewById(R.id.lloCatalogo);
            btnCatalogoRegresar = itemView.findViewById(R.id.btnCatalogoRegresar);
            btnFiltros = itemView.findViewById(R.id.btnFiltros);
            txtBuscar = itemView.findViewById(R.id.txtBuscar);

            lloFiltros = itemView.findViewById(R.id.lloFiltros);
            spnEstilo = itemView.findViewById(R.id.spnEstilo);
            spnMarca = itemView.findViewById(R.id.spnMarca);
            spnTalla = itemView.findViewById(R.id.spnTalla);
            spnSucursal = itemView.findViewById(R.id.spnSucursal);
            txtDesde = itemView.findViewById(R.id.txtDesde);
            txtHasta = itemView.findViewById(R.id.txtHasta);
            btnLimpiarFiltros = itemView.findViewById(R.id.btnLimpiarFiltros);
            btnAplicarFiltros = itemView.findViewById(R.id.btnAplicarFiltros);

            chgTipo = itemView.findViewById(R.id.chgTipo);
            chpCamisas = itemView.findViewById(R.id.chpCamisas);
            chpPantalones = itemView.findViewById(R.id.chpPantalones);

            rcvCatalogo = itemView.findViewById(R.id.rcvCatalogo);

            // Inicializar la base de datos y la lista de productos que se mostrará en pantalla
            database = new Database(context);
            productos = new ArrayList<>();
        }

        public void init() {
            // Los botones de categorías abren el componente que visualiza la lista de productos, y aplica filtros base para su categoría
            btnCaballeros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCatalogo();
                    caballeroDama = true;
                    categoria = 1;
                    filtroTipo = ((Chip) chgTipo.getChildAt(0)).getText().toString();
                    filtrar();
                }
            });

            btnDamas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCatalogo();
                    caballeroDama = true;
                    categoria = 2;
                    filtroTipo = ((Chip) chgTipo.getChildAt(0)).getText().toString();
                    filtrar();
                }
            });

            btnZapatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCatalogo();
                    caballeroDama = false;
                    categoria = 3;
                    filtrar();
                }
            });

            btnAccesorios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCatalogo();
                    caballeroDama = false;
                    categoria = 4;
                    filtrar();
                }
            });

            // El botón Regresar vuelve a mostrar las categorías y cierra la visualización de los productos
            btnCatalogoRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lloCatalogo.setVisibility(View.GONE);
                    lloCategorias.setVisibility(View.VISIBLE);
                    limpiarFiltros();
                    txtBuscar.setText(null);
                    chpCamisas.setChecked(true);
                    chpPantalones.setChecked(false);
                    filtroTipo = ((Chip) chgTipo.getChildAt(0)).getText().toString();
                    activity.changeAdapter(new Catalogo(context, activity));
                }
            });

            // Abre o cierra el menú donde puede aplicar filtros a la búsqueda
            btnFiltros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lloFiltros.getVisibility() == View.GONE) {
                        lloFiltros.setVisibility(View.VISIBLE);
                    } else {
                        lloFiltros.setVisibility(View.GONE);
                    }
                }
            });

            // Filtra los productos en base al texto en la barra de buscador, en tiempo real
            txtBuscar.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filtrar();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Inicializa los Spinner para seleccionar los filtros de Marcas, Estilos, Tallas y Sucursales
            // Recupera la lista de Marcas
            ArrayList<String> marcas = new ArrayList<>(Arrays.asList(database.helperController.getMarcas()));
            marcas.add(0, "Seleccionar marca");
            // Crea el Adapter que utilizará el Spinner
            ArrayAdapter<String> marcaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, marcas);
            marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMarca.setAdapter(marcaAdapter);
            spnMarca.setSelection(0);
            filtroMarca = spnMarca.getSelectedItem().toString();
            // Prepara el filtro de Marca al seleccionar una Marca
            spnMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filtroMarca = marcas.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Recupera la lista de Estilos
            ArrayList<String> estilos = new ArrayList<>(Arrays.asList(database.estiloController.getEstiloNombres()));
            estilos.add(0, "Seleccionar estilo");
            // Crea el Adapter que utilizará el Spinner
            ArrayAdapter<String> estiloAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, estilos);
            estiloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnEstilo.setAdapter(estiloAdapter);
            spnEstilo.setSelection(0);
            filtroEstilo = spnEstilo.getSelectedItem().toString();
            // Prepara el filtro al seleccionar un Estilo
            spnEstilo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filtroEstilo = estilos.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Crea la lista de Tallas
            String[] tallas = new String[] { "Seleccionar talla", "S", "M", "L" };
            // Crea el Adapter que utilizará el Spinner
            ArrayAdapter<String> tallaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tallas);
            tallaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnTalla.setAdapter(tallaAdapter);
            spnTalla.setSelection(0);
            filtroTalla = spnTalla.getSelectedItem().toString();
            // Prepara el filtro al seleccionar una Talla
            spnTalla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filtroTalla = tallas[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Esconde el seleccionador de Tallas si la categoría seleccionada es la de Accesorios
            if (categoria == 4) {
                itemView.findViewById(R.id.lblFiltroTalla).setVisibility(View.GONE);
                spnTalla.setVisibility(View.GONE);
            } else {
                itemView.findViewById(R.id.lblFiltroTalla).setVisibility(View.VISIBLE);
                spnTalla.setVisibility(View.VISIBLE);
            }

            // Recupera la lista de Sucursales
            ArrayList<String> sucursales = new ArrayList<>(Arrays.asList(database.helperController.getSucursales()));
            sucursales.add(0, "Seleccionar sucursal");
            // Crea el Adapter que utlilizará el Spinner
            ArrayAdapter<String> sucursalAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sucursales);
            sucursalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnSucursal.setAdapter(sucursalAdapter);
            spnSucursal.setSelection(0);
            filtroSucursal = spnSucursal.getSelectedItem().toString();
            // Prepara el filtro al seleccionar una Sucursal
            spnSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filtroSucursal = sucursales.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            txtDesde.setText("");
            txtHasta.setText("");

            // Limpia los filtros aplicados a la búsqueda
            btnLimpiarFiltros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    limpiarFiltros();
                    filtrar();
                    lloFiltros.setVisibility(View.GONE);
                }
            });

            // Aplica los filtros seleccionados a la búsqueda
            btnAplicarFiltros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filtroMarca = spnMarca.getSelectedItem().toString();
                    filtroEstilo = spnEstilo.getSelectedItem().toString();
                    filtroTalla = spnTalla.getSelectedItem().toString();
                    filtroSucursal = spnSucursal.getSelectedItem().toString();

                    filtrar();
                    lloFiltros.setVisibility(View.GONE);
                }
            });

            // Si la categoría seleccionada es Caballeros o Damas, muestra la opción de filtrar entre Camisas y Pantalones
            if (caballeroDama) {
                chgTipo.setVisibility(View.VISIBLE);
            } else {
                chgTipo.setVisibility(View.GONE);
            }

            // Aplica el filtro seleccionado a la búsqueda
            chgTipo.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                    filtroTipo = group.getChildAt(checkedIds.get(0)).getTag().toString();
                }
            });

            chpCamisas.setBackgroundTintList(context.getColorStateList(R.color.boton));
            chpPantalones.setBackgroundTintList(context.getColorStateList(R.color.boton));


            // Asigna el Adapter en forma de cuadrícula al RecyclerView
            rcvCatalogo.setLayoutManager(new GridLayoutManager(context, 2));
            rcvCatalogo.setAdapter(new GridCatalogo(context, productos));
        }

        // Esconde las categorías y muestra la lista de productos
        public void openCatalogo() {
            lloCategorias.setVisibility(View.GONE);
            lloCatalogo.setVisibility(View.VISIBLE);
        }

        // Función de apoyo para limpiar todos los filtros de la búsqueda
        public void limpiarFiltros() {
            spnMarca.setSelection(0);
            spnEstilo.setSelection(0);
            spnTalla.setSelection(0);
            spnSucursal.setSelection(0);
            txtDesde.setText("");
            txtHasta.setText("");
        }

        // Función de apoyo para aplicar los filtros a la búsqueda
        public void filtrar() {
            // Revisa que los filtros de Precio no estén vacíos y asigna su valor a las variables tipo Double
            Double precioDesde = -1.0;
            Double precioHasta = -1.0;
            if (!txtDesde.getText().toString().isEmpty()) precioDesde = Double.parseDouble(txtDesde.getText().toString());
            if (!txtHasta.getText().toString().isEmpty()) precioHasta = Double.parseDouble(txtHasta.getText().toString());
            // Asigna el nombre de la tabla donde se van a buscar los productos filtrados en base a la categoría seleccionada
            String tabla = "";
            switch (categoria) {
                case 1:
                case 2:
                    if (filtroTipo.equals("Camisas")) {
                        tabla = "camisas";
                    } else {
                        tabla = "pantalones";
                    }
                    break;
                case 3:
                    tabla = "zapatos";
                    break;
                case 4:
                    tabla = "accesorios";
                    break;
            }
            // Recupera la lista de productos con los filtros seleccionados, con la función de apoyo getProductosFiltro()
            productos = database.helperController.getProductosFiltro(tabla, categoria, filtroMarca, filtroEstilo, filtroTalla, filtroSucursal, precioDesde, precioHasta, txtBuscar.getText().toString());

            // Recrea el Adapter con la nueva lista de productos filtrados
            rcvCatalogo.setAdapter(null);
            rcvCatalogo.getRecycledViewPool().clear();
            rcvCatalogo.setAdapter(new GridCatalogo(context, productos));
        }
    }
}