package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, apellido, mail, celular, contrasenia, rol, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getRol().name()); // el enum lo guardo como texto
            ps.setBoolean(7, usuario.isEliminado());
            ps.setTimestamp(8, Timestamp.valueOf(usuario.getCreatedAt()));
            ps.executeUpdate();

            // agarro el id que genero la base y se lo pongo al objeto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
            System.out.println("usuario guardado con id " + usuario.getId());
        } catch (SQLException e) {
            System.out.println("error al crear usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("error al buscar usuario por id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(armarUsuario(rs));
            }
        } catch (SQLException e) {
            System.out.println("error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, mail = ?, celular = ?, contrasenia = ?, rol = ? WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());
            ps.executeUpdate();
            System.out.println("usuario " + usuario.getId() + " actualizado");
        } catch (SQLException e) {
            System.out.println("error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Long id) {
        // baja logica, no borro de verdad, solo marco eliminado
        String sql = "UPDATE usuario SET eliminado = 1 WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            System.out.println("usuario " + id + " dado de baja");
        } catch (SQLException e) {
            System.out.println("error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario obtenerPorMail(String mail) {
        String sql = "SELECT * FROM usuario WHERE mail = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("error al buscar usuario por mail: " + e.getMessage());
        }
        return null;
    }

    // armo el usuario con lo que viene de la base
    private Usuario armarUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("mail"),
                rs.getString("celular"),
                rs.getString("contrasenia"),
                Rol.valueOf(rs.getString("rol")));
        u.setId(rs.getLong("id"));
        u.setEliminado(rs.getBoolean("eliminado"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    }
}
