package inventario;
import java.util.ArrayList;
import java.util.List;

import modelo.InsumoPC;

public class FiltroInventario {
    public static List<InsumoPC> filtrarPorPrecioMaximo(
        List<? extends InsumoPC> productos,
        double precioMaximo){
        List<InsumoPC> listaInsumos = new ArrayList<>();
        
        for (InsumoPC insumo : productos) {
            if(insumo.getPrecio() <= precioMaximo){
                listaInsumos.add(insumo);
            }
        }

        return listaInsumos;
    }

    public static void copiarInsumos(
        List<? extends InsumoPC> origen,
        List<? super InsumoPC> destino){
            for (InsumoPC objeto : origen) {
                destino.add(objeto);
            }
    }
}