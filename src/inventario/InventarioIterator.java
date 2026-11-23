package inventario;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import modelo.InsumoPC;

public class InventarioIterator implements Iterator<InsumoPC>{
    private List<InsumoPC> inventario;
    private int posicion;

    public InventarioIterator(List<InsumoPC> inventario){
        this.inventario = inventario;
        posicion = 0;
    }

    @Override 
    public boolean hasNext(){
        return posicion < inventario.size();
    }

    @Override 
    public InsumoPC next(){
        InsumoPC elementoInsumoPC;
        if(hasNext()){
             elementoInsumoPC = inventario.get(posicion);
             posicion++;
             return elementoInsumoPC;
        }
        throw new NoSuchElementException("No hay mas elementos");
    }
}
