package modelo;
public class MemoriaRAM extends InsumoPC {
    private int capacidad;
    private int velocidad;

    public MemoriaRAM(int codigo, String marca, String modelo, double precio, int stock, int capacidad, int velocidad){
        super(codigo, marca, modelo, precio, stock);
        this.capacidad = capacidad;
        this.velocidad = velocidad;
    }

    public MemoriaRAM(int codigo, String marca, String modelo, double precio, int capacidad, int velocidad){
        this(codigo, marca, modelo, precio, 0, capacidad, velocidad);
    }

    public MemoriaRAM(int codigo, String marca, String modelo, int capacidad, int velocidad){
        this(codigo, marca, modelo, 0, capacidad, velocidad);
    }

    public int getCapacidad(){
        return this.capacidad;
    }

    public int getVelocidad(){
        return this.velocidad;
    }

    @Override 
    public String toString(){
        return super.toString() + " | Capacidad: " + this.capacidad
        + " GB | Velocidad: " + this.velocidad + " MT/s";
    }
}