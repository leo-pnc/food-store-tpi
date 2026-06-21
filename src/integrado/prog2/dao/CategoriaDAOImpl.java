package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements ICategoriaDAO {

    @Override
    public void crear(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion, eliminado, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setBoolean(3, categoria.isEliminado());
            ps.setTimestamp(4, Timestamp.valueOf(categoria.getCreatedAt()));

            ps.executeUpdate();

            // Recuperamos el ID autogenerado por MySQL y se lo asignamos al objeto Java
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getLong(1));
                }
            }
            System.out.println("¡Categoría guardada en la base de datos con ID: " + categoria.getId() + "!");
        } catch (SQLException e) {
            System.err.println("Error al crear la categoría: " + e.getMessage());
        }
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria WHERE eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las categorías: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setLong(3, categoria.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Categoría con ID " + categoria.getId() + " actualizada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Long id) {
        // BAJA LÓGICA: Hacemos un UPDATE del campo eliminado a true (1) en lugar de borrar el registro físicamente
        String sql = "UPDATE categoria SET eliminado = 1 WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Categoría con ID " + id + " dada de baja lógicamente con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente la categoría: " + e.getMessage());
        }
    }

    @Override
    public Categoria obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM categoria WHERE nombre = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría por nombre: " + e.getMessage());
        }
        return null;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        boolean eliminado = rs.getBoolean("eliminado");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");

        return new Categoria(id, eliminado, createdAt, nombre, descripcion);
    }
}