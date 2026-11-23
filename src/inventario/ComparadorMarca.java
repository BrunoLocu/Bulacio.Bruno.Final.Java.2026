package inventario;
import java.util.Comparator;

import modelo.InsumoPC;

public class ComparadorMarca implements Comparator<InsumoPC>{
    @Override 
    public int compare(InsumoPC a, InsumoPC b){
        return a.getMarca().compareToIgnoreCase(b.getMarca());
    }
}