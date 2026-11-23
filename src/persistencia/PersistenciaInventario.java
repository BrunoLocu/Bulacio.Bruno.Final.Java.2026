package persistencia;

import modelo.InsumoPC;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class PersistenciaInventario {
    public static void guardarInventario(
        List<InsumoPC> inventario,
        String rutaArchivo) throws IOException{
            try(ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(rutaArchivo))){
                salida.writeObject(inventario);
            }
    }

    public static List<InsumoPC> cargarInventario(String rutaArchivo) 
            throws IOException, ClassNotFoundException{
                try(ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(rutaArchivo))){
                    return (List<InsumoPC>) entrada.readObject();
                }
            }

    
}