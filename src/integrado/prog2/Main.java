package integrado.prog2;

import integrado.prog2.dao.CategoriaDAOImpl;
import integrado.prog2.dao.ICategoriaDAO;
import integrado.prog2.dao.IProductoDAO;
import integrado.prog2.dao.ProductoDAOImpl;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba del DAO de Productos...");

        ICategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        IProductoDAO productoDAO = new ProductoDAOImpl();

        // 1. Obtener la categoría "Hamburguesas" de la base de datos (ID: 1)
        Categoria hamburguesas = categoriaDAO.obtenerPorId(1L);
        System.out.println("Categoría cargada: " + hamburguesas);

        // 2. Crear una nueva hamburguesa asociada a esa categoría
        System.out.println("\n--- CREANDO NUEVO PRODUCTO ---");
        Producto nuevo = new Producto("Hamburguesa Triple Cheddar", 6500.00, "Triple medallon, triple cheddar y salsa de la casa", 15, "triple_cheddar.png", true, hamburguesas);
        productoDAO.crear(nuevo);

        // 3. Listar todos los productos cargados en la base de datos
        System.out.println("\n--- LISTADO COMPLETO DE PRODUCTOS ---");
        List<Producto> todos = productoDAO.listarTodos();
        for (Producto p : todos) {
            System.out.println(p);
        }

        // 4. Probar el filtro por categoría (HU-PROD-01) - Listar solo Hamburguesas (ID: 1)
        System.out.println("\n--- PRODUCTOS DE LA CATEGORÍA: " + hamburguesas.getNombre() + " ---");
        List<Producto> deCategoria = productoDAO.listarPorCategoria(1L);
        for (Producto p : deCategoria) {
            System.out.println(p);
        }
    }
}