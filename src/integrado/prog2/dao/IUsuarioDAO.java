package integrado.prog2.dao;

import integrado.prog2.entities.Usuario;

public interface IUsuarioDAO extends IBaseDAO<Usuario> {
    // lo uso para chequear que el mail no este repetido
    Usuario obtenerPorMail(String mail);
}
