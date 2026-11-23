package contratos;
import java.util.List;

public interface Crud<T> {
    void agregar(T elemento);
    List<T> listar();
    void actualizar(int codigo, T elemento);
    void eliminar(int codigo);
}