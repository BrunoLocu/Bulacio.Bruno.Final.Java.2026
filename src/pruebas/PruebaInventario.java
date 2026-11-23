package pruebas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import inventario.FiltroInventario;
import inventario.GestorInventario;
import inventario.InventarioIterator;
import inventario.OperacionesInventario;
import modelo.CertificacionFuente;
import modelo.Fuente;
import modelo.InsumoPC;
import modelo.MemoriaRAM;
import modelo.PlacaVideo;
import persistencia.PersistenciaInventario;
import persistencia.PersistenciaJSON;
import persistencia.PersistenciaTXT;

import java.io.IOException;
import persistencia.PersistenciaCSV;

public class PruebaInventario{
    public static void main(String[] args){
        GestorInventario gestorInventario = new GestorInventario();
        
        MemoriaRAM memoriaRAM = new MemoriaRAM(3, "Kingston, Inc", "Fury \\\"Pro\\\"\\nEdición\\tEspecial", 85000, 16, 3200);
        PlacaVideo placaVideo = new PlacaVideo(1, "MSI", "RTX 4060", 450000, 8, 115);
        Fuente fuente = new Fuente(2, "Corsair", "RM750", 120000, 750, CertificacionFuente.GOLD);
        
        gestorInventario.agregar(memoriaRAM);
        gestorInventario.agregar(placaVideo);
        gestorInventario.agregar(fuente);

        List<InsumoPC> inventario = gestorInventario.listar();
        
        try {
            inventario = FiltroInventario.filtrarPorPrecioMaximo(inventario, 200000);
            PersistenciaTXT.exportarTXT(inventario, "productos.txt");            
        } catch (Exception e) {
            System.out.println("hubo un error");
        }
        // try {
        //     List<InsumoPC> recuperados =
        //         PersistenciaJSON.cargarJSON("productos.json");

        //     System.out.println("Productos recuperados: " + recuperados.size());

        //     for (InsumoPC insumo : recuperados) {
        //         System.out.println(insumo);
        //     }
        // } catch (IOException e) {
        //     System.out.println("Error al leer el JSON");
        // }

        // try{
        //     PersistenciaJSON.guardarJSON(inventario, "productos.json");
        // }catch(Exception e){
        //     System.out.println(e.getMessage());
        // }

        // try {
        //     List<InsumoPC> productos =
        //         PersistenciaCSV.cargarCSV("productos.csv");

        //    for (InsumoPC producto : productos) {
        //         System.out.println("Marca recuperada: [" + producto.getMarca() + "]");
        //         System.out.println("Modelo recuperado: [" + producto.getModelo() + "]");
        //     }

        // } catch (IOException e) {
        //     System.out.println("Error al cargar el CSV: " + e.getMessage());
        // }

        // try {
        //     List<InsumoPC> inventario = PersistenciaInventario.cargarInventario("inventario.dat");
        //     for(InsumoPC insumo : inventario){
        //         System.out.println(insumo.toString());
        //     }
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }  catch (ClassNotFoundException e){
        //     System.out.println(e.getMessage());
        // }
        
        // OperacionesInventario.aplicarOperacion(inventario, producto -> producto.agregarStock(10));
        // List<String> descripciones = OperacionesInventario.obtenerDescripciones(inventario, producto -> "Marca: " + producto.getMarca() + " | Código: " + producto.getCodigo());

        
        // for (String descripcioString : descripciones) {
        //     System.out.println(descripcioString);
        // }
        // List<InsumoPC> inventarioFiltrado = new ArrayList<>();
        // FiltroInventario.copiarInsumos(inventario, inventarioFiltrado);

        // List<InsumoPC> inventarioFiltrado = FiltroInventario.filtrarPorPrecioMaximo(inventario, 150000);
        
        // InventarioIterator inventario = new InventarioIterator(gestorInventario.listar());

        // while (inventario.hasNext()) {
        //     System.out.println(inventario.next());
        // }

        // PlacaVideo placaVideo2 = new PlacaVideo(2, "MSI", "RTX4060", 480000, 8, 115);
        
        // try {
        //     gestorInventario.agregar(placaVideo2);
        // } catch (CodigoDuplicadoException e) {
        //     System.out.println(e.getMessage());
        // }

        // try {
        //     gestorInventario.eliminar(99);
        // } catch (InsumoNoEncontradoException e) {
        //     System.out.println(e.getMessage());
        // }

        // System.out.println("Cantidad de productos inventario: " + inventario.size());
        // System.out.println("Cantidad de productos inventario copiado: " + inventarioFiltrado.size());

    }
}