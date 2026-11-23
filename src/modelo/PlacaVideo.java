package modelo;
import contratos.InformacionElectrica;

public class PlacaVideo extends InsumoPC implements InformacionElectrica {
    private int vram;
    private int consumo;

    public PlacaVideo(int codigo, String marca, String modelo, double precio, int stock, int vram, int consumo){
        super(codigo, marca, modelo, precio, stock);
        this.vram = vram;
        this.consumo = consumo;
    }

    public PlacaVideo(int codigo, String marca, String modelo, double precio, int vram, int consumo){
        this(codigo, marca, modelo, precio, 0, vram, consumo);
    }

    public PlacaVideo(int codigo, String marca, String modelo, int vram, int consumo){
        this(codigo, marca, modelo, 0, vram, consumo);
    }

    public int getVram(){
        return this.vram;
    }

    public int getConsumo(){
        return this.consumo;
    }

    @Override
    public void mostrarDatosElectricos(){
        System.out.println("Consumo eléctrico: " + this.consumo + " W"); 
    }

    @Override 
    public String toString(){
        return super.toString() + " | VRAM: " + this.vram
        + " GB | Consumo: " + this.consumo + " W";
    }
}
