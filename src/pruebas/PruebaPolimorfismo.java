package pruebas;
import contratos.InformacionElectrica;
import modelo.CertificacionFuente;
import modelo.Fuente;
import modelo.PlacaVideo;

public class PruebaPolimorfismo {
    public static void main(String[] args){
        InformacionElectrica placaVideo = new PlacaVideo(1, "GeForce", "RX590", 12, 500);
        InformacionElectrica fuente = new Fuente(2, "Poronga", "Pitoto", 850, CertificacionFuente.GOLD);

        placaVideo.mostrarDatosElectricos();
        fuente.mostrarDatosElectricos();
    }
}
