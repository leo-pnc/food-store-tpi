package integrado.prog2.dao;
import integrado.prog2.entities.Producto;
import java.util.List;
public interface IProductoDAO extends IBaseDAO<Producto> {
    //Metodo de negocio para filtrar productos por su cat.
    List<Producto> listarPorCategoria(Long categoriaId);
}
