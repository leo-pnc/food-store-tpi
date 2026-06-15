package integrado.prog2.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/pedidos_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection conexion = null;

    // Constructor privado para evitar que creen objetos de esta clase desde afuera
    private ConexionDB() {}

    // Metodo estático para obtener la conexión unica
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Forzamos la carga del Driver de MySQL para evitar errores de conexión
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("¡Conexión establecida con éxito a pedidos_db!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Error crítico: No se encontró el driver de MySQL (com.mysql.cj.jdbc.Driver). Asegúrate de agregarlo como librería.", e);
            }
        }
        return conexion;
    }

    // Metodo para cerrar la conexión de forma segura al finalizar el programa
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("Conexión con la base de datos cerrada de manera segura.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}