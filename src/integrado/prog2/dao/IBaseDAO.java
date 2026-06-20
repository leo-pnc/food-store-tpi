package integrado.prog2.dao;
import java.util.List;
public interface IBaseDAO<T> {
    void crear(T entidad);
    T obtenerPorId(Long id);
    void actualizar(T entidad);
    void eliminar(Long id);
}
