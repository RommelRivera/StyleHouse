package com.rommelrivera.stylehouse;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import database.Database;
import database.Estilo;

public class Prueba {
    public static void datosPrueba(Context context) {
        SharedPreferences pref = context.getSharedPreferences("datos", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("idPedido", 0);
        edit.putInt("idUsuario", 0);
        edit.apply();

        String filler = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        Database database = new Database(context);

        database.helperController.insertMarca("Pierre Cardin");
        database.helperController.insertMarca("Adidas");
        database.helperController.insertSucursal("Cuscatlán");

        database.usuarioController.insertAdmin("admin", "admin", "123", "ADMINISTRADOR");

        database.estiloController.insertEstilo("Formal", R.drawable.formal);
        Estilo formal = database.estiloController.getEstilosByNombre("Formal")[0];
        database.estiloController.insertEstilo("Streetwear", R.drawable.streetwear);
        Estilo streetwear = database.estiloController.getEstilosByNombre("Streetwear")[0];

        database.camisaController.insertCamisa(formal, "Camisa formal azul", "Pierre Cardin", 10.0, "S", "Cuscatlán", R.drawable.camisa_formal_hombre_1, "Camisa formal Pierre Cardin azul. " + filler, "M");
        database.camisaController.insertCamisa(formal, "Camisa formal negra", "Pierre Cardin", 20.0, "S", "Cuscatlán", R.drawable.camisa_formal_hombre_2, "Camisa formal Pierre Cardin negra. " + filler, "M");
        database.camisaController.insertCamisa(streetwear, "Camisa streetwear negra", "Adidas", 10.0, "S", "Cuscatlán", R.drawable.camisa_streetwear_hombre_1, "Camisa streetwear Adidas negra. " + filler, "M");
        database.camisaController.insertCamisa(streetwear, "Camisa streetwear azul", "Adidas", 20.0, "S", "Cuscatlán", R.drawable.camisa_streetwear_hombre_2, "Camisa streetwear Adidas azul. " + filler, "M");

        database.camisaController.insertCamisa(formal, "Camisa formal amarilla", "Pierre Cardin", 15.0, "S", "Cuscatlán", R.drawable.camisa_formal_mujer_1, "Camisa formal Pierre Cardin amarilla. " + filler, "F");
        database.camisaController.insertCamisa(formal, "Camisa formal negra", "Pierre Cardin", 25.0, "S", "Cuscatlán", R.drawable.camisa_formal_mujer_2, "Camisa formal Pierre Cardin negra. " + filler, "F");
        database.camisaController.insertCamisa(streetwear, "Camisa streetwear rosa", "Adidas", 15.0, "S", "Cuscatlán", R.drawable.camisa_streetwear_mujer_1, "Camisa streetwear Adidas rosa. " + filler, "F");
        database.camisaController.insertCamisa(streetwear, "Camisa streetwear negra", "Adidas", 25.0, "S", "Cuscatlán", R.drawable.camisa_streetwear_mujer_2, "Camisa streetwear Adidas negra. " + filler, "F");

        database.pantalonController.insertPantalon(formal, "Pantalon formal beige", "Pierre Cardin", 30.0, "M", "Cuscatlán", R.drawable.pantalon_formal_hombre_1, "Pantalon formal Pierre Cardin beige. " + filler, "M");
        database.pantalonController.insertPantalon(formal, "Pantalon formal gris", "Pierre Cardin", 40.0, "M", "Cuscatlán", R.drawable.pantalon_formal_hombre_2, "Pantalon formal Pierre Cardin gris. " + filler, "M");
        database.pantalonController.insertPantalon(streetwear, "Pantalon streetwear negro", "Adidas", 30.0, "M", "Cuscatlán", R.drawable.pantalon_streetwear_hombre_1, "Pantalon streetwear Adidas negro. " + filler, "M");
        database.pantalonController.insertPantalon(streetwear, "Pantalon streetwear blanco", "Adidas", 40.0, "M", "Cuscatlán", R.drawable.pantalon_streetwear_hombre_2, "Pantalon streetwear Adidas blanco. " + filler, "M");

        database.pantalonController.insertPantalon(formal, "Pantalon formal negro", "Pierre Cardin", 35.0, "M", "Cuscatlán", R.drawable.pantalon_formal_mujer_1, "Pantalon formal Pierre Cardin negro. " + filler, "F");
        database.pantalonController.insertPantalon(formal, "Pantalon formal turquesa", "Pierre Cardin", 45.0, "M", "Cuscatlán", R.drawable.pantalon_formal_mujer_2, "Pantalon formal Pierre Cardin turquesa. " + filler, "F");
        database.pantalonController.insertPantalon(streetwear, "Pantalon streetwear negro", "Adidas", 35.0, "M", "Cuscatlán", R.drawable.pantalon_streetwear_mujer_1, "Pantalon streetwear Adidas negro. " + filler, "F");
        database.pantalonController.insertPantalon(streetwear, "Pantalon streetwear turquesa", "Adidas", 45.0, "M", "Cuscatlán", R.drawable.pantalon_streetwear_mujer_2, "Pantalon streetwear Adidas turquesa. " + filler, "F");

        database.zapatoController.insertZapato(formal, "Zapatos formales negros hombre", "Pierre Cardin", 22.0, "L", "Cuscatlán", R.drawable.zapato_formal_hombre_1, "Zapatos formales Pierre Cardin negros hombre. " + filler);
        database.zapatoController.insertZapato(formal, "Zapatos formales cafes hombre", "Pierre Cardin", 33.0, "L", "Cuscatlán", R.drawable.zapato_formal_hombre_2, "Zapatos formales Pierre Cardin cafes hombre. " + filler);
        database.zapatoController.insertZapato(formal, "Zapatos formales blancos mujer", "Pierre Cardin", 22.0, "L", "Cuscatlán", R.drawable.zapato_formal_mujer_1, "Zapatos formales Pierre Cardin blancos mujer. " + filler);
        database.zapatoController.insertZapato(formal, "Zapatos formales negros mujer", "Pierre Cardin", 33.0, "L", "Cuscatlán", R.drawable.zapato_formal_mujer_2, "Zapatos formales Pierre Cardin negros mujer. " + filler);
        database.zapatoController.insertZapato(streetwear, "Zapatos streetwear blancos 1 hombre", "Adidas", 22.0, "L", "Cuscatlán", R.drawable.zapato_streetwear_hombre_1, "Zapatos streetwear Adidas blancos hombre. " + filler);
        database.zapatoController.insertZapato(streetwear, "Zapatos streetwear blancos 2 hombre", "Adidas", 23.0, "L", "Cuscatlán", R.drawable.zapato_streetwear_hombre_2, "Zapatos streetwear Adidas blancos con diseño hombre. " + filler);
        database.zapatoController.insertZapato(streetwear, "Zapatos streetwear negros mujer", "Adidas", 22.0, "L", "Cuscatlán", R.drawable.zapato_streetwear_mujer_1, "Zapatos streetwear Adidas negros mujer. " + filler);
        database.zapatoController.insertZapato(streetwear, "Zapatos streetwear rosa mujer", "Adidas", 23.0, "L", "Cuscatlán", R.drawable.zapato_streetwear_mujer_2, "Zapatos streetwear Adidas rosa mujer. " + filler);

        database.accesorioController.insertAccesorio(formal, "Cartera formal", "Pierre Cardin", 23.0, "Cuscatlán", R.drawable.accesorio_formal_cartera, "Cartera Pierre Cardin formal. " + filler);
        database.accesorioController.insertAccesorio(formal, "Collar formal", "Pierre Cardin", 32.0, "Cuscatlán", R.drawable.accesorio_formal_collar, "Collar Pierre Cardin formal. " + filler);
        database.accesorioController.insertAccesorio(formal, "Reloj formal", "Pierre Cardin", 23.0, "Cuscatlán", R.drawable.accesorio_formal_reloj, "Reloj Pierre Cardin formal." + filler);
        database.accesorioController.insertAccesorio(formal, "Corbata formal", "Pierre Cardin", 32.0, "Cuscatlán", R.drawable.accesorio_formal_corbata, "Corbata Pierre Cardin formal. " + filler);
        database.accesorioController.insertAccesorio(streetwear, "Lentes streetwear", "Adidas", 23.0, "Cuscatlán", R.drawable.accesorio_streetwear_lentes, "Lentes Adidas streetwear. " + filler);
        database.accesorioController.insertAccesorio(streetwear, "Gorra streetwear", "Adidas", 32.0, "Cuscatlán", R.drawable.accesorio_streetwear_gorra, "Gorra Adidas streetwear. " + filler);
        database.accesorioController.insertAccesorio(streetwear, "Beanie streetwear", "Adidas", 23.0, "Cuscatlán", R.drawable.accesorio_streetwear_beanie, "Beanie Adidas streetwear. " + filler);
        database.accesorioController.insertAccesorio(streetwear, "Mochila streetwear", "Adidas", 32.0, "Cuscatlán", R.drawable.accesorio_streetwear_mochila, "Mochila Adidas streetwear. " + filler);
    }
}
