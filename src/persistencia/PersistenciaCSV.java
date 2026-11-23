package persistencia;
import modelo.InsumoPC;
import modelo.MemoriaRAM;
import modelo.PlacaVideo;
import modelo.CertificacionFuente;
import modelo.Fuente;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class PersistenciaCSV {
    public static void guardarCSV(List<InsumoPC> inventario, String rutaArchivo) throws IOException{
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            escritor.write("tipo,codigo,marca,modelo,precio,stock,capacidad,velocidad,vram,consumo,potencia,certificacion");
            escritor.newLine();

            for (InsumoPC insumo : inventario) {
                String fila = insumo.getClass().getSimpleName() + "," 
                + insumo.getCodigo() + "," 
                + escaparCSV(insumo.getMarca()) + "," 
                + escaparCSV(insumo.getModelo()) + "," 
                + insumo.getPrecio() + "," 
                + insumo.consultarStock();

                if(insumo instanceof MemoriaRAM memoria){
                    fila += "," + memoria.getCapacidad()
                     + "," + memoria.getVelocidad()
                     + ",,,,";
                }else if(insumo instanceof PlacaVideo placa){
                    fila += ",,," + placa.getVram() + "," + placa.getConsumo() + ",,";
                }else if(insumo instanceof Fuente fuente){
                    fila += ",,,,," + fuente.getPotencia() + "," + fuente.getCertificacion();
                }else{
                    fila += ",,,,,,";
                }

                escritor.write(fila);
                escritor.newLine();
            }
        }
    }

    public static void mostrarCSV(String rutaArchivo) throws IOException{
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = lector.readLine();

            while((linea = lector.readLine()) != null){
                String[] datos = linea.split(",", -1);
                System.out.println("Tipo: " + datos[0] + " | Código: " + datos[1] + " | Marca: " + datos[2]);
            }
        }
    }

    public static List<InsumoPC> cargarCSV(String rutaArchivo) throws IOException{
        List<InsumoPC> listaProductos = new ArrayList<>();
        
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = lector.readLine();

            while((linea = lector.readLine()) != null){
                String[] datos = separarCSV(linea);

                if(datos.length != 12){
                    throw new IOException("Cantidad de columnas encontrada erronea. Se han detectado " + datos.length + " columnas");
                }

                try {
                    String tipo = datos[0];
                    int codigo = Integer.parseInt(datos[1]);
                    String marca = datos[2];
                    String modelo = datos[3];
                    double precio = Double.parseDouble(datos[4]);
                    int stock = Integer.parseInt(datos[5]);

                    if(tipo.equals("MemoriaRAM")){
                        int capacidad = Integer.parseInt(datos[6]);
                        int velocidad = Integer.parseInt(datos[7]);

                        MemoriaRAM objeto = new MemoriaRAM(codigo, marca, modelo, precio, stock, capacidad, velocidad);
                        listaProductos.add(objeto);
                    }else if(tipo.equals("PlacaVideo")){
                        int vram = Integer.parseInt(datos[8]);
                        int consumo = Integer.parseInt(datos[9]);

                        PlacaVideo objeto = new PlacaVideo(codigo, marca, modelo, precio, stock, vram, consumo);
                        listaProductos.add(objeto);
                    }else if(tipo.equals("Fuente")){
                        int potencia = Integer.parseInt(datos[10]);
                        CertificacionFuente certificacion = CertificacionFuente.valueOf(datos[11]);

                        Fuente objeto = new Fuente(codigo, marca, modelo, precio, stock, potencia, certificacion);
                        listaProductos.add(objeto);
                    }else{
                        throw new IOException("Tipo de insumo no reconocido: " + tipo);
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("El CSV contiene un valor numérico inválido.", e);
                } catch (IllegalArgumentException e) {
                    throw new IOException("Valor inválido dentro del CSV: " + e.getMessage());
                }
            }
        }

        return listaProductos;
    }

    private static String escaparCSV(String valor){
        String resultado = valor.replace("\"","\"\"");
        return "\"" + resultado + "\"";
    }

    private static String[] separarCSV(String linea){
        List<String> columnas = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean dentroComillas = false;

        for(int i = 0; i < linea.length(); i++){
            char caracter = linea.charAt(i);

            if(caracter == '"'){
                if(dentroComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"'){
                    campo.append('"');
                    i++;
                }else{
                    dentroComillas = !dentroComillas;
                }
            }else if(caracter == ',' && !dentroComillas){
                columnas.add(campo.toString());
                campo.setLength(0);
            }else{
                campo.append(caracter);
            }
        }
        columnas.add(campo.toString());

        return columnas.toArray(new String[0]);
    }
}