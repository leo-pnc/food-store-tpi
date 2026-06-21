package integrado.prog2.dao;

import integrado.prog2.entities.Pedido;
import java.util.List;

public interface IPedidoDAO extends IBaseDAO<Pedido> {
    List<Pedido> listarPorUsuario(Long usuarioId);
}