package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import java.util.List;

public interface IPedidoService extends IGenericService<Pedido> {
    List<Pedido> listarPorUsuario(Long usuarioId);
}