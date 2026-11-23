package inventario;
import java.util.Comparator;

import modelo.InsumoPC;

public class ComparadorPrecio implements Comparator<InsumoPC>{
    @Override 
    public int compare(InsumoPC a, InsumoPC b){
        return Double.compare(a.getPrecio(), b.getPrecio());
    }
}
