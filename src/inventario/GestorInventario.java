package inventario;
import java.util.List;

import contratos.Crud;
import excepciones.CodigoDuplicadoException;
import excepciones.InsumoNoEncontradoException;
import modelo.InsumoPC;

import java.util.ArrayList;

public class GestorInventario implements Crud<InsumoPC>{
    private List<InsumoPC> inventario;

    public GestorInventario(){
        this.inventario = new ArrayList<>();
    }

    @Override 
    public void agregar(InsumoPC insumo){
        if(insumo == null){
            throw new IllegalArgumentException("El insumo es inválido");
        }

        InsumoPC insumoInventario = buscarPorCodigo(insumo.getCodigo());
        
        if(insumoInventario != null){
            throw new CodigoDuplicadoException("Ese código ya existe y no se ha agregado el insumo.");
        }
        
        inventario.add(insumo);
    }
    
    @Override 
    public List<InsumoPC> listar(){
        return new ArrayList<>(inventario);
    }

    @Override 
    public void actualizar(int codigo, InsumoPC elemento){
        int index = inventario.indexOf(buscarPorCodigo(codigo));

        if(elemento == null || elemento.getCodigo() != codigo) throw new IllegalArgumentException("El elemento o el código ingresado no son válidos.");
        if(index < 0) throw new InsumoNoEncontradoException("Ese insumo no existe, por ende no se puede actualizar.");

        inventario.set(index, elemento);
    }

    @Override 
    public void eliminar(int codigo){
        InsumoPC insumo = buscarPorCodigo(codigo);
        if(insumo == null) throw new InsumoNoEncontradoException("El insumo no existe.");
        inventario.remove(insumo);
    }

    public int contarInsumos(){
        return this.inventario.size();
    }

    public InventarioIterator obtenerIterador(){
        return new InventarioIterator(inventario);
    }

    private InsumoPC buscarPorCodigo(int codigo){
        for(InsumoPC insumo : inventario){
            if(insumo.getCodigo() == codigo){
                return insumo;
            }
        }
        return null;
    }
}
