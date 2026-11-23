package modelo;
import contratos.InformacionElectrica;

public class Fuente extends InsumoPC implements InformacionElectrica{
    private int potencia;
    private CertificacionFuente certificacion;

    public Fuente(int codigo, String marca, String modelo, double precio, int stock, int potencia, CertificacionFuente certificacion){
        super(codigo, marca, modelo, precio, stock);
        this.potencia = potencia;
        this.certificacion = certificacion;
    }

    public Fuente(int codigo, String marca, String modelo, double precio, int potencia, CertificacionFuente certificacion){
        this(codigo, marca, modelo, precio, 0, potencia, certificacion);
    }

    public Fuente(int codigo, String marca, String modelo, int potencia, CertificacionFuente certificacion){
        this(codigo, marca, modelo, 0, potencia, certificacion);
    }

    @Override 
    public void mostrarDatosElectricos(){
        System.out.println("Potencia de salida: " + this.potencia + " W");
    }

    public int getPotencia(){
        return this.potencia;
    }

    public CertificacionFuente getCertificacion(){
        return this.certificacion;
    }

    public void setCertificacion(CertificacionFuente nuevaCertificacion){
        this.certificacion = nuevaCertificacion;
    }

    @Override 
    public String toString(){
        return super.toString() + " | Potencia: " + this.potencia
        + " W | Certificación: " + this.certificacion;
    }
}