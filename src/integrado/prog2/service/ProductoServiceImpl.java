package integrado.prog2.service;

import integrado.prog2.dao.CategoriaDAOImpl;
import integrado.prog2.dao.ICategoriaDAO;
import integrado.prog2.dao.IProductoDAO;
import integrado.prog2.dao.ProductoDAOImpl;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.StockInvalidoException;

import java.util.List;

public class ProductoServiceImpl implements IGenericService<Producto> {

    private final IProductoDAO dao = new ProductoDAOImpl();
    private final ICategoriaDAO categoriaDao = new CategoriaDAOImpl();

    @Override
    public void guardar(Producto producto) {
        validar(producto);
        dao.crear(producto);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return dao.obtenerPorId(id);
    }

    @Override
    public List<Producto> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public void actualizar(Producto producto) {
        validar(producto);
        dao.actualizar(producto);
    }

    @Override
    public void eliminar(Long id) {
        dao.eliminar(id);
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return dao.listarPorCategoria(categoriaId);
    }

    // junto todas las validaciones del producto aca asi no las repito en crear y actualizar
    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre del producto no puede estar vacio");
        }
        if (p.getPrecio() == null || p.getPrecio() < 0) {
            throw new IllegalArgumentException("el precio no puede ser negativo");
        }
        if (p.getStock() < 0) {
            throw new StockInvalidoException("el stock no puede ser negativo");
        }
        // la categoria tiene que existir en la base
        if (p.getCategoria() == null || categoriaDao.obtenerPorId(p.getCategoria().getId()) == null) {
            throw new IllegalArgumentException("la categoria del producto no existe");
        }
    }
}
