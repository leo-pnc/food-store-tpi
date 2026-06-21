package integrado.prog2.service;

import integrado.prog2.dao.*;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;

import java.util.List;

public class PedidoServiceImpl implements IPedidoService {

    private final IPedidoDAO pedidoDAO;
    private final IProductoDAO productoDAO;

    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public void guardar(Pedido pedido) {
        // mo permitir crear pedido sin usuario asociado
        if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Error: No se puede crear un pedido sin un usuario asociado.");
        }

        //validar que el pedido tenga al menos un detalle
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("Error: El pedido debe contener al menos un producto.");
        }

        // validar stock y cantidades para cada detalle del pedido
        for (DetallePedido dp : pedido.getDetalles()) {
            // Cantidad debe ser > 0
            if (dp.getCantidad() <= 0) {
                throw new StockInvalidoException("Error: La cantidad del producto '" + dp.getProducto().getNombre() + "' debe ser mayor a 0.");
            }

            Producto productoReal = productoDAO.obtenerPorId(dp.getProducto().getId());
            if (productoReal == null) {
                throw new EntidadNoEncontradaException("Error: El producto '" + dp.getProducto().getNombre() + "' no existe en la base de datos.");
            }

            // validamos stock disponible
            if (productoReal.getStock() < dp.getCantidad()) {
                throw new StockInvalidoException("Error: Stock insuficiente para '" + productoReal.getNombre() + "'. Stock disponible: " + productoReal.getStock() + ", Solicitado: " + dp.getCantidad());
            }

            //descontamos el stock localmente en nuestro objeto
            productoReal.setStock(productoReal.getStock() - dp.getCantidad());

            productoDAO.actualizar(productoReal);
        }

        // persistimos el pedido y sus detalles de forma transaccional
        pedidoDAO.crear(pedido);
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        Pedido pedido = pedidoDAO.obtenerPorId(id);
        if (pedido == null) {
            throw new EntidadNoEncontradaException("Error: El pedido con ID " + id + " no existe.");
        }
        return pedido;
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoDAO.listarTodos();
    }

    @Override
    public void actualizar(Pedido pedido) {
        obtenerPorId(pedido.getId());
        pedidoDAO.actualizar(pedido);
    }

    @Override
    public void eliminar(Long id) {
        // validamos que exista el pedido antes de eliminarlo lógicamente
        Pedido pedido = obtenerPorId(id);
        for (DetallePedido dp : pedido.getDetalles()) {
            Producto productoReal = productoDAO.obtenerPorId(dp.getProducto().getId());
            if (productoReal != null) {
                productoReal.setStock(productoReal.getStock() + dp.getCantidad());
                productoDAO.actualizar(productoReal);
            }
        }
        pedidoDAO.eliminar(id);
    }

    @Override
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoDAO.listarPorUsuario(usuarioId);
    }
}