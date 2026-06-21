package integrado.prog2.service;

import integrado.prog2.dao.IUsuarioDAO;
import integrado.prog2.dao.UsuarioDAOImpl;
import integrado.prog2.entities.Usuario;
import integrado.prog2.exception.EmailDuplicadoException;

import java.util.List;

public class UsuarioServiceImpl implements IBaseService<Usuario> {

    private final IUsuarioDAO dao = new UsuarioDAOImpl();

    @Override
    public void crear(Usuario usuario) {
        if (usuario.getMail() == null || usuario.getMail().trim().isEmpty()) {
            throw new IllegalArgumentException("el mail no puede estar vacio");
        }
        // chequeo que el mail no lo tenga otro usuario
        if (dao.obtenerPorMail(usuario.getMail()) != null) {
            throw new EmailDuplicadoException("ya existe un usuario con el mail " + usuario.getMail());
        }
        dao.crear(usuario);
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return dao.obtenerPorId(id);
    }

    @Override
    public List<Usuario> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public void actualizar(Usuario usuario) {
        // si el mail ya lo usa OTRO usuario, no dejo
        Usuario existente = dao.obtenerPorMail(usuario.getMail());
        if (existente != null && !existente.getId().equals(usuario.getId())) {
            throw new EmailDuplicadoException("ese mail ya lo usa otro usuario");
        }
        dao.actualizar(usuario);
    }

    @Override
    public void eliminar(Long id) {
        dao.eliminar(id);
    }
}
