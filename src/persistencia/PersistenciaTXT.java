package persistencia;

import modelo.InsumoPC;
import modelo.MemoriaRAM;
import modelo.PlacaVideo;
import modelo.Fuente;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PersistenciaTXT {
    public static void exportarTXT(
        List<? extends InsumoPC> insumos,
        String rutaArchivo) throws IOException {

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {

            escritor.write("REPORTE INVENTARIO DE INSUMOS");
            escritor.newLine();
            escritor.write("----------------------------------------");
            escritor.newLine();

            for (InsumoPC insumo : insumos) {
                escritor.write(insumo.toString());
                escritor.newLine();
                escritor.write("----------------------------------------");
                escritor.newLine();
            }
        }
    }
}