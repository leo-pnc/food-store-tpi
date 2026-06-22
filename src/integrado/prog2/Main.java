package integrado.prog2;

import integrado.prog2.config.ConexionDB;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Pedidos (Food Store)...");

        // Arrancamos el menú interactivo por consola
        new Menu().iniciar();

        // Cerramos la conexión de forma segura al salir de la aplicación
        ConexionDB.cerrarConexion();
    }
}