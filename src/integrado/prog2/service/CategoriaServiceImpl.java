package integrado.prog2.service;

import integrado.prog2.dao.CategoriaDAOImpl;
import integrado.prog2.dao.ICategoriaDAO;
import integrado.prog2.entities.Categoria;

import java.util.List;

public class CategoriaServiceImpl implements IBaseService<Categoria> {

    private final ICategoriaDAO dao = new CategoriaDAOImpl();

    @Override
    public void crear(Categoria categoria) {
        // el nombre no puede ir vacio
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre de la categoria no puede estar vacio");
        }
        // y no puede repetirse con otra que ya exista
        if (dao.obtenerPorNombre(categoria.getNombre()) != null) {
            throw new IllegalArgumentException("ya existe una categoria con el nombre " + categoria.getNombre());
        }
        dao.crear(categoria);
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return dao.obtenerPorId(id);
    }

    @Override
    public List<Categoria> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public void actualizar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre no puede estar vacio");
        }
        dao.actualizar(categoria);
    }

    @Override
    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}
