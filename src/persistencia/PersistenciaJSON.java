package persistencia;

import modelo.CertificacionFuente;
import modelo.Fuente;
import modelo.InsumoPC;
import modelo.MemoriaRAM;
import modelo.PlacaVideo;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;

public class PersistenciaJSON {
    public static void guardarJSON(List<InsumoPC> inventario, String rutaArchivo) throws IOException{
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            escritor.write("[");
            escritor.newLine();
            
            boolean primero = true;

            for (InsumoPC insumo : inventario) {
                if(!primero){
                    escritor.write(",");
                    escritor.newLine();
                }
                escritor.write("{");
                
                escritor.newLine();
                escritor.write("\"tipo\": \"" + escaparJSON(insumo.getClass().getSimpleName()) + "\",");
                escritor.newLine();
                escritor.write("\"codigo\": " + insumo.getCodigo() + ",");
                escritor.newLine();
                escritor.write("\"marca\": \"" + escaparJSON(insumo.getMarca()) + "\",");
                escritor.newLine();
                escritor.write("\"modelo\": \"" + escaparJSON(insumo.getModelo()) + "\",");
                escritor.newLine();
                escritor.write("\"precio\": " + insumo.getPrecio() + ",");
                escritor.newLine();
                escritor.write("\"stock\": " + insumo.consultarStock() + ",");

                if (insumo instanceof MemoriaRAM memoria) {
                    escritor.newLine();
                    escritor.write("\"capacidad\": " + memoria.getCapacidad() + ",");
                    escritor.newLine();
                    escritor.write("\"velocidad\": " + memoria.getVelocidad());
                }else if(insumo instanceof PlacaVideo placa){
                    escritor.newLine();
                    escritor.write("\"vram\": " + placa.getVram() + ",");
                    escritor.newLine();
                    escritor.write("\"consumo\": " + placa.getConsumo());
                }else if(insumo instanceof Fuente fuente){
                    escritor.newLine();
                    escritor.write("\"potencia\": " + fuente.getPotencia() + ",");
                    escritor.newLine();
                    escritor.write("\"certificacion\": \"" + fuente.getCertificacion().name() + "\"");
                }else{
                    throw new IOException("Tipo de insumo no soportado para JSON");
                }

                escritor.newLine();
                escritor.write("}");
                primero = false;
            }

            escritor.newLine();
            escritor.write("]");
        }
    }

    public static List<InsumoPC> cargarJSON(String rutaArchivo) throws IOException {
        List<InsumoPC> inventario = new ArrayList<>();

        String contenido = leerContenidoJSON(rutaArchivo).trim();

        if (!contenido.startsWith("[") || !contenido.endsWith("]")) {
            throw new IOException("Se esperaba un arreglo JSON.");
        }

        String contenidoArreglo = contenido.substring(
            1, contenido.length() - 1
        ).trim();

        if (contenidoArreglo.isEmpty()) {
            return inventario;
        }

        List<String> objetosJSON = separarObjetosJSON(contenidoArreglo);

        for (String objeto : objetosJSON) {
            List<String> propiedades = separarPropiedadesJSON(objeto);
            Map<String, String> datosProducto = new HashMap<>();

            for (String propiedad : propiedades) {
                String[] partes = propiedad.split(":", 2);

                if (partes.length != 2) {
                    throw new IOException("Propiedad JSON incorrecta: " + propiedad);
                }

                String nombre = desescaparJSON(partes[0].trim());
                String valor = partes[1].trim();

                if (datosProducto.putIfAbsent(nombre, valor) != null) {
                    throw new IOException("Propiedad JSON duplicada: " + nombre);
                }
            }

            try {
                String tipo = desescaparJSON(
                    obtenerObligatorio(datosProducto, "tipo")
                );

                int codigo = Integer.parseInt(
                    obtenerObligatorio(datosProducto, "codigo")
                );

                String marca = desescaparJSON(
                    obtenerObligatorio(datosProducto, "marca")
                );

                String modelo = desescaparJSON(
                    obtenerObligatorio(datosProducto, "modelo")
                );

                double precio = Double.parseDouble(
                    obtenerObligatorio(datosProducto, "precio")
                );

                int stock = Integer.parseInt(
                    obtenerObligatorio(datosProducto, "stock")
                );

                if (!Double.isFinite(precio)) {
                    throw new IllegalArgumentException("Precio no válido.");
                }

                switch (tipo) {
                    case "MemoriaRAM" -> {
                        int capacidad = Integer.parseInt(
                            obtenerObligatorio(datosProducto, "capacidad")
                        );

                        int velocidad = Integer.parseInt(
                            obtenerObligatorio(datosProducto, "velocidad")
                        );

                        inventario.add(new MemoriaRAM(
                            codigo, marca, modelo, precio, stock,
                            capacidad, velocidad
                        ));
                    }

                    case "PlacaVideo" -> {
                        int vram = Integer.parseInt(
                            obtenerObligatorio(datosProducto, "vram")
                        );

                        int consumo = Integer.parseInt(
                            obtenerObligatorio(datosProducto, "consumo")
                        );

                        inventario.add(new PlacaVideo(
                            codigo, marca, modelo, precio, stock,
                            vram, consumo
                        ));
                    }

                    case "Fuente" -> {
                        int potencia = Integer.parseInt(
                            obtenerObligatorio(datosProducto, "potencia")
                        );

                        CertificacionFuente certificacion =
                            CertificacionFuente.valueOf(
                                desescaparJSON(
                                    obtenerObligatorio(
                                        datosProducto, "certificacion"
                                    )
                                )
                            );

                        inventario.add(new Fuente(
                            codigo, marca, modelo, precio, stock,
                            potencia, certificacion
                        ));
                    }

                    default -> throw new IOException(
                        "Tipo de insumo desconocido: " + tipo
                    );
                }

            } catch (IllegalArgumentException e) {
                throw new IOException(
                    "Error al convertir los datos de un producto JSON.", e
                );
            }
        }

    return inventario;
}

    public static String leerContenidoJSON(String rutaArchivo) throws IOException{
        return Files.readString(Path.of(rutaArchivo));
    }

    private static List<String> separarObjetosJSON(String contenidoArreglo){
        List<String> objetos = new ArrayList<>();
        StringBuilder objetoActual = new StringBuilder();

        int profundidad = 0;
        boolean dentroComillas = false;
        boolean escapado = false;

        for(int i = 0; i < contenidoArreglo.length(); i++){
            char caracter = contenidoArreglo.charAt(i);

            if(escapado) escapado = false;
            else if(caracter == '\\' && dentroComillas) escapado = true;
            else if(caracter == '\"') dentroComillas = !dentroComillas;

            if(!dentroComillas){
                if(caracter == '{') profundidad++;
                else if(caracter == '}') profundidad--;
            }
            
            if(profundidad > 0 || (caracter == '}' && !dentroComillas && profundidad == 0)) objetoActual.append(caracter);
            if(caracter == '}' && profundidad == 0 && !dentroComillas) {
                objetos.add(objetoActual.toString());
                objetoActual.setLength(0);
            }
        }

        System.out.println("Objetos encontrados: " + objetos.size());
        return objetos;
    }

    private static List<String> separarPropiedadesJSON(String objetoJSON){
        List<String> propiedades = new ArrayList<>();
        StringBuilder propiedadActual = new StringBuilder();

        boolean dentroComillas = false;
        boolean escapado = false;

        String propiedadSinLlaves = objetoJSON.substring(1, objetoJSON.length() - 1);

        for(int i = 0; i < propiedadSinLlaves.length(); i++){
            char caracter = propiedadSinLlaves.charAt(i);

            if(escapado) escapado = false;
            else if(caracter == '\\' && dentroComillas) escapado = true;
            else if(caracter == '\"') dentroComillas = !dentroComillas;

            if(caracter == ',' && !dentroComillas){
                propiedades.add(propiedadActual.toString().trim());
                propiedadActual.setLength(0);
            }else{
                propiedadActual.append(caracter);
            }
        }

        if(propiedadActual.length() > 0) propiedades.add(propiedadActual.toString().trim());

        return propiedades;
    }

    private static String desescaparJSON(String valorJSON) {
        if (valorJSON == null || valorJSON.length() < 2
                || !valorJSON.startsWith("\"")
                || !valorJSON.endsWith("\"")) {
            throw new IllegalArgumentException("Se esperaba una cadena JSON.");
        }

        String contenido = valorJSON.substring(1, valorJSON.length() - 1);

        StringBuilder resultado = new StringBuilder();
        boolean escapado = false;

        for (int i = 0; i < contenido.length(); i++) {
            char caracter = contenido.charAt(i);

            if (escapado) {
                switch (caracter) {
                    case '"' -> resultado.append('"');
                    case '\\' -> resultado.append('\\');
                    case 'n' -> resultado.append('\n');
                    case 't' -> resultado.append('\t');
                    case 'r' -> resultado.append('\r');

                    default -> throw new IllegalArgumentException(
                        "Secuencia de escape JSON no reconocida: \\" + caracter
                    );
                }

                escapado = false;

            } else if (caracter == '\\') {
                escapado = true;

            } else {
                resultado.append(caracter);
            }
        }

        if (escapado) {
            throw new IllegalArgumentException(
                "Secuencia de escape incompleta en JSON."
            );
        }

        return resultado.toString();
    }

    private static String obtenerObligatorio(
        Map<String, String> datos, String nombre) throws IOException {

        String valor = datos.get(nombre);

        if (valor == null) {
            throw new IOException(
                "Falta la propiedad obligatoria: " + nombre
            );
        }

        return valor;
    }

    private static String escaparJSON(String valor){
         return valor.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\t", "\\t")
                    .replace("\r", "\\r");
    }
}
