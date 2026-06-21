package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements IProductoDAO {

    public ProductoDAOImpl() {
        // no necesitamos instanciar CategoriaDAOImpl acá
    }

    @Override
    public void crear(Producto producto) {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id, eliminado, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setString(3, producto.getDescripcion());
                ps.setInt(4, producto.getStock());
                ps.setString(5, producto.getImagen());
                ps.setBoolean(6, producto.getDisponible());

                if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
                    throw new SQLException("No se puede guardar un producto sin una categoría válida asociada.");
                }
                ps.setLong(7, producto.getCategoria().getId());
                ps.setBoolean(8, producto.isEliminado());
                ps.setTimestamp(9, Timestamp.valueOf(producto.getCreatedAt()));

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getLong(1));
                    }
                }
                System.out.println("¡Producto guardado en la base de datos con ID: " + producto.getId() + "!");
            }
        } catch (SQLException e) {
            System.err.println("Error al crear el producto: " + e.getMessage());
        }
    }

    @Override
    public Producto obtenerPorId(Long id) {
        // Usamos INNER JOIN para traer los datos del producto y su categoría de un solo viaje
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.categoria_id, p.eliminado, p.created_at, " +
                "       c.nombre AS cat_nombre, c.descripcion AS cat_descripcion, c.eliminado AS cat_eliminado, c.created_at AS cat_created_at " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.id = ? AND p.eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearProducto(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.categoria_id, p.eliminado, p.created_at, " +
                "       c.nombre AS cat_nombre, c.descripcion AS cat_descripcion, c.eliminado AS cat_eliminado, c.created_at AS cat_created_at " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todos los productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ?, categoria_id = ? " +
                "WHERE id = ? AND eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setString(3, producto.getDescripcion());
                ps.setInt(4, producto.getStock());
                ps.setString(5, producto.getImagen());
                ps.setBoolean(6, producto.getDisponible());

                if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
                    throw new SQLException("No se puede actualizar un producto sin una categoría válida asociada.");
                }
                ps.setLong(7, producto.getCategoria().getId());
                ps.setLong(8, producto.getId());

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Producto con ID " + producto.getId() + " actualizado con éxito.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE producto SET eliminado = 1 WHERE id = ?";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setLong(1, id);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Producto con ID " + id + " dade de baja lógicamente con éxito.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente el producto: " + e.getMessage());
        }
    }

    @Override
    public List<Producto> listarPorCategoria(Long categoriaId) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.precio, p.descripcion, p.stock, p.imagen, p.disponible, p.categoria_id, p.eliminado, p.created_at, " +
                "       c.nombre AS cat_nombre, c.descripcion AS cat_descripcion, c.eliminado AS cat_eliminado, c.created_at AS cat_created_at " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.categoria_id = ? AND p.eliminado = 0";
        try {
            Connection conn = ConexionDB.getConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setLong(1, categoriaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearProducto(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos por categoría: " + e.getMessage());
        }
        return lista;
    }
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        boolean eliminado = rs.getBoolean("eliminado");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        String nombre = rs.getString("nombre");
        Double precio = rs.getDouble("precio");
        String descripcion = rs.getString("descripcion");
        int stock = rs.getInt("stock");
        String imagen = rs.getString("imagen");
        Boolean disponible = rs.getBoolean("disponible");

        // Mapeamos los datos de la Categoria obtenidos mediante el JOIN (usando los alias de la consulta)
        Long catId = rs.getLong("categoria_id");
        String catNombre = rs.getString("cat_nombre");
        String catDescripcion = rs.getString("cat_descripcion");
        boolean catEliminado = rs.getBoolean("cat_eliminado");
        LocalDateTime catCreatedAt = rs.getTimestamp("cat_created_at").toLocalDateTime();

        // Instanciamos el objeto Categoria correspondiente sin hacer consultas extras a la BD
        Categoria categoria = new Categoria(catId, catEliminado, catCreatedAt, catNombre, catDescripcion);

        return new Producto(id, eliminado, createdAt, nombre, precio, descripcion, stock, imagen, disponible, categoria);
    }
}