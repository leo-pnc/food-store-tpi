package integrado.prog2.service;

import java.util.List;

public interface IGenericService<T> {
    void guardar(T entidad);
    T obtenerPorId(Long id);
    List<T> listarTodos();
    void actualizar(T entidad);
    void eliminar(Long id);
}