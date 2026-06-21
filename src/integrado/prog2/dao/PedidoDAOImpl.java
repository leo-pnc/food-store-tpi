package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements IPedidoDAO {

    private final IProductoDAO productoDAO;
    private final IUsuarioDAO usuarioDAO;

    public PedidoDAOImpl() {
        this.productoDAO = new ProductoDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @Override
    public void crear(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (fecha, estado, total, forma_pago, usuario_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (cantidad, subtotal, producto_id, pedido_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDate(1, java.sql.Date.valueOf(pedido.getFecha()));
                psPedido.setString(2, pedido.getEstado().name());
                psPedido.setDouble(3, pedido.getTotal());
                psPedido.setString(4, pedido.getFormaPago().name());

                if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null) {
                    throw new SQLException("No se puede crear un pedido sin un usuario válido asociado.");
                }
                psPedido.setLong(5, pedido.getUsuario().getId());
                psPedido.setBoolean(6, pedido.isEliminado());
                psPedido.setTimestamp(7, Timestamp.valueOf(pedido.getCreatedAt()));

                psPedido.executeUpdate();

                try (ResultSet rs = psPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getLong(1));
                    }
                }
            }

            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS)) {
                for (DetallePedido dp : pedido.getDetalles()) {
                    psDetalle.setInt(1, dp.getCantidad());
                    psDetalle.setDouble(2, dp.getSubtotal());

                    if (dp.getProducto() == null || dp.getProducto().getId() == null) {
                        throw new SQLException("No se puede guardar un detalle sin un producto válido.");
                    }
                    psDetalle.setLong(3, dp.getProducto().getId());
                    psDetalle.setLong(4, pedido.getId());
                    psDetalle.setBoolean(5, dp.isEliminado());
                    psDetalle.setTimestamp(6, Timestamp.valueOf(dp.getCreatedAt()));

                    psDetalle.executeUpdate();

                    try (ResultSet rs = psDetalle.getGeneratedKeys()) {
                        if (rs.next()) {
                            dp.setId(rs.getLong(1));
                        }
                    }
                }
            }

            conn.commit();
            System.out.println("¡Pedido #" + pedido.getId() + " y todos sus detalles guardados con éxito mediante transacciones!");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("¡ERROR EN LA CREACIÓN DEL PEDIDO! Se ejecutó un ROLLBACK de la transacción de forma segura.");
                } catch (SQLException ex) {
                    System.err.println("Error al ejecutar el rollback: " + ex.getMessage());
                }
            }
            System.err.println("Detalle del error en base de datos: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        String sql = "SELECT * FROM pedido WHERE id = ? AND eliminado = 0";
        Pedido pedido = null;
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // 1. Cargamos solo los campos básicos del pedido y liberamos el ResultSet
                        pedido = mapearPedidoBasico(rs);
                    }
                }
            }

            // 2. Con el ResultSet cerrado, cargamos el Usuario y sus detalles asociados secuencialmente (Integrado con el de Bruno)
            if (pedido != null) {
                pedido.setUsuario(usuarioDAO.obtenerPorId(pedido.getUsuario().getId()));
                pedido.setDetalles(cargarDetallesDePedido(pedido.getId()));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedido por ID: " + e.getMessage());
        }
        return pedido;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPedidoBasico(rs));
                }
            }

            for (Pedido p : lista) {
                p.setUsuario(usuarioDAO.obtenerPorId(p.getUsuario().getId()));
                p.setDetalles(cargarDetallesDePedido(p.getId()));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar todos los pedidos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET estado = ?, total = ?, forma_pago = ? WHERE id = ? AND eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pedido.getEstado().name());
                ps.setDouble(2, pedido.getTotal());
                ps.setString(3, pedido.getFormaPago().name());
                ps.setLong(4, pedido.getId());

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Pedido con ID " + pedido.getId() + " actualizado con éxito.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el pedido: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Long id) {
        String sqlPedido = "UPDATE pedido SET eliminado = 1 WHERE id = ?";
        String sqlDetalles = "UPDATE detalle_pedido SET eliminado = 1 WHERE pedido_id = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido)) {
                psPedido.setLong(1, id);
                psPedido.executeUpdate();
            }

            try (PreparedStatement psDetalles = conn.prepareStatement(sqlDetalles)) {
                psDetalles.setLong(1, id);
                psDetalles.executeUpdate();
            }

            conn.commit();
            System.out.println("Pedido con ID " + id + " y sus detalles dados de baja lógicamente.");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println(ex.getMessage()); }
            }
            System.err.println("Error al eliminar lógicamente el pedido: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { System.err.println(e.getMessage()); }
            }
        }
    }

    @Override
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE usuario_id = ? AND eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, usuarioId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearPedidoBasico(rs));
                    }
                }
            }

            for (Pedido p : lista) {
                p.setUsuario(usuarioDAO.obtenerPorId(p.getUsuario().getId()));
                p.setDetalles(cargarDetallesDePedido(p.getId()));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por usuario: " + e.getMessage());
        }
        return lista;
    }
    private Pedido mapearPedidoBasico(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        boolean eliminado = rs.getBoolean("eliminado");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        Estado estado = Estado.valueOf(rs.getString("estado"));
        Double total = rs.getDouble("total");
        FormaPago formaPago = FormaPago.valueOf(rs.getString("forma_pago"));

        Long usuarioId = rs.getLong("usuario_id");

        // Creamos un Usuario dummy temporal únicamente con el ID
        Usuario usuarioDummy = new Usuario();
        usuarioDummy.setId(usuarioId);

        return new Pedido(id, eliminado, createdAt, fecha, estado, total, formaPago, usuarioDummy, new ArrayList<>());
    }

    // Carga los detalles vinculados a un pedido de forma secuencial y en dos etapas para evitar colisiones de ResultSet
    private List<DetallePedido> cargarDetallesDePedido(Long pedidoId) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE pedido_id = ? AND eliminado = 0";
        Connection conn = ConexionDB.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    boolean eliminado = rs.getBoolean("eliminado");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    int cantidad = rs.getInt("cantidad");
                    Double subtotal = rs.getDouble("subtotal");
                    Long productoId = rs.getLong("producto_id");

                    Producto productoDummy = new Producto();
                    productoDummy.setId(productoId);

                    detalles.add(new DetallePedido(id, eliminado, createdAt, cantidad, subtotal, productoDummy));
                }
            }
        }

        for (DetallePedido dp : detalles) {
            dp.setProducto(productoDAO.obtenerPorId(dp.getProducto().getId()));
        }

        return detalles;
    }
}