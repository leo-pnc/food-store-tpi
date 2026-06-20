package integrado.prog2.dao;
import integrado.prog2.entities.Categoria;
public interface ICategoriaDAO extends IBaseDAO<Categoria> {
    // Metodo para validar la unicidad del nombre en la base de datos
    Categoria obtenerPorNombre(String nombre);
}
