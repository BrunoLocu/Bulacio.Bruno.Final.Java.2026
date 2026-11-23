package excepciones;
public class InsumoNoEncontradoException extends RuntimeException{
    public InsumoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
