package excepciones;
public class CodigoDuplicadoException extends RuntimeException{
    public CodigoDuplicadoException(String mensaje){
        super(mensaje);
    }
}
