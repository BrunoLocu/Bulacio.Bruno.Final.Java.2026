package modelo;
import java.io.Serializable;

public abstract class InsumoPC implements Comparable<InsumoPC>, Serializable{
    private int codigo;
    private String marca;
    private String modelo;
    private double precio;
    private int stock;

    private static final long serialVersionUID = 1L;

    public InsumoPC(int codigo, String marca, String modelo, double precio, int stock){
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    public InsumoPC(int codigo, String marca, String modelo, double precio){
        this(codigo, marca, modelo, precio, 0);
    }

    public InsumoPC(int codigo, String marca, String modelo){
        this(codigo, marca, modelo, 0);
    }

    public int getCodigo(){
        return this.codigo;
    }

    public double getPrecio(){
        return this.precio;
    }

    public String getMarca(){
        return this.marca;
    }

    public String getModelo(){
        return this.modelo;
    }

    public int consultarStock(){
        return stock;
    }
    
    public void agregarStock(int cantidad){
        if(cantidad > 0){
            stock += cantidad;
        }
    }
    
    public void restarStock(int cantidad){
        if (cantidad > 0 && cantidad <= stock){
            stock -= cantidad;
        }
    }
    
    @Override 
    public String toString(){
        return "Código: " + this.codigo 
        + " | Marca: " + this.marca 
        + " | Modelo: " + this.modelo 
        + " | Precio: " + this.precio 
        + " | Stock: " + this.stock;
    }

    @Override 
    public int compareTo(InsumoPC otro){
        return Integer.compare(this.codigo, otro.getCodigo());
    }
}
