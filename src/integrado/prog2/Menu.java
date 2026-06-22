package integrado.prog2;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.service.CategoriaServiceImpl;
import integrado.prog2.service.PedidoServiceImpl;
import integrado.prog2.service.ProductoServiceImpl;
import integrado.prog2.service.UsuarioServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final CategoriaServiceImpl categoriaService = new CategoriaServiceImpl();
    private final ProductoServiceImpl productoService = new ProductoServiceImpl();
    private final UsuarioServiceImpl usuarioService = new UsuarioServiceImpl();
    private final PedidoServiceImpl pedidoService = new PedidoServiceImpl();

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("Chau!");
                default -> System.out.println("Opcion invalida");
            }
        } while (opcion != 0);
    }

    // ---------- CATEGORIAS ----------

    private void menuCategorias() {
        int op;
        do {
            System.out.println("\n--- CATEGORIAS ---");
            System.out.println("1. Listar  2. Crear  3. Editar  4. Eliminar  0. Volver");
            op = leerEntero("Seleccione: ");
            switch (op) {
                case 1 -> listarCategorias();
                case 2 -> crearCategoria();
                case 3 -> editarCategoria();
                case 4 -> eliminarCategoria();
                case 0 -> { }
                default -> System.out.println("Opcion invalida");
            }
        } while (op != 0);
    }

    private void listarCategorias() {
        List<Categoria> lista = categoriaService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay categorias cargadas");
            return;
        }
        for (Categoria c : lista) {
            System.out.println(c);
        }
    }

    private void crearCategoria() {
        String nombre = leerTexto("Nombre: ");
        String desc = leerTexto("Descripcion: ");
        try {
            categoriaService.guardar(new Categoria(nombre, desc));
            System.out.println("Categoria creada ok");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarCategoria() {
        listarCategorias();
        Long id = leerLong("Id a editar: ");
        Categoria c = categoriaService.obtenerPorId(id);
        if (c == null) {
            System.out.println("No existe esa categoria");
            return;
        }
        c.setNombre(leerTexto("Nuevo nombre: "));
        c.setDescripcion(leerTexto("Nueva descripcion: "));
        try {
            categoriaService.actualizar(c);
            System.out.println("Categoria actualizada");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        listarCategorias();
        Long id = leerLong("Id a eliminar: ");
        categoriaService.eliminar(id);
    }

    // ---------- PRODUCTOS ----------

    private void menuProductos() {
        int op;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Listar  2. Crear  3. Editar  4. Eliminar  0. Volver");
            op = leerEntero("Seleccione: ");
            switch (op) {
                case 1 -> listarProductos();
                case 2 -> crearProducto();
                case 3 -> editarProducto();
                case 4 -> eliminarProducto();
                case 0 -> { }
                default -> System.out.println("Opcion invalida");
            }
        } while (op != 0);
    }

    private void listarProductos() {
        List<Producto> lista = productoService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos cargados");
            return;
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void crearProducto() {
        listarCategorias();
        Long catId = leerLong("Id de la categoria: ");
        Categoria cat = categoriaService.obtenerPorId(catId);
        if (cat == null) {
            System.out.println("Esa categoria no existe");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        String desc = leerTexto("Descripcion: ");
        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Stock: ");
        String imagen = leerTexto("Imagen: ");
        boolean disponible = stock > 0; // si hay stock queda disponible
        try {
            productoService.guardar(new Producto(nombre, precio, desc, stock, imagen, disponible, cat));
            System.out.println("Producto creado ok");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarProducto() {
        listarProductos();
        Long id = leerLong("Id a editar: ");
        Producto p = productoService.obtenerPorId(id);
        if (p == null) {
            System.out.println("No existe ese producto");
            return;
        }
        p.setNombre(leerTexto("Nuevo nombre: "));
        p.setPrecio(leerDouble("Nuevo precio: "));
        p.setStock(leerEntero("Nuevo stock: "));
        try {
            productoService.actualizar(p);
            System.out.println("Producto actualizado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        listarProductos();
        Long id = leerLong("Id a eliminar: ");
        productoService.eliminar(id);
    }

    // ---------- USUARIOS ----------

    private void menuUsuarios() {
        int op;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar  2. Crear  3. Editar  4. Eliminar  0. Volver");
            op = leerEntero("Seleccione: ");
            switch (op) {
                case 1 -> listarUsuarios();
                case 2 -> crearUsuario();
                case 3 -> editarUsuario();
                case 4 -> eliminarUsuario();
                case 0 -> { }
                default -> System.out.println("Opcion invalida");
            }
        } while (op != 0);
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios cargados");
            return;
        }
        for (Usuario u : lista) {
            System.out.println(u);
        }
    }

    private void crearUsuario() {
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String mail = leerTexto("Mail: ");
        String celular = leerTexto("Celular: ");
        String contrasenia = leerTexto("Contrasenia: ");
        Rol rol = elegirRol();
        try {
            usuarioService.guardar(new Usuario(nombre, apellido, mail, celular, contrasenia, rol));
            System.out.println("Usuario creado ok");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        listarUsuarios();
        Long id = leerLong("Id a editar: ");
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            System.out.println("No existe ese usuario");
            return;
        }
        u.setNombre(leerTexto("Nuevo nombre: "));
        u.setApellido(leerTexto("Nuevo apellido: "));
        u.setMail(leerTexto("Nuevo mail: "));
        u.setCelular(leerTexto("Nuevo celular: "));
        try {
            usuarioService.actualizar(u);
            System.out.println("Usuario actualizado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        listarUsuarios();
        Long id = leerLong("Id a eliminar: ");
        usuarioService.eliminar(id);
    }

    private Rol elegirRol() {
        System.out.println("Rol: 1. ADMIN  2. USUARIO");
        int op = leerEntero("Seleccione: ");
        return op == 1 ? Rol.ADMIN : Rol.USUARIO;
    }

    // ---------- PEDIDOS ----------

    private void menuPedidos() {
        int op;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Listar  2. Crear  3. Editar estado/pago  4. Eliminar  0. Volver");
            op = leerEntero("Seleccione: ");
            switch (op) {
                case 1 -> listarPedidos();
                case 2 -> crearPedido();
                case 3 -> editarPedido();
                case 4 -> eliminarPedido();
                case 0 -> { }
                default -> System.out.println("Opcion invalida");
            }
        } while (op != 0);
    }

    private void listarPedidos() {
        List<Pedido> lista = pedidoService.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay pedidos cargados");
            return;
        }
        for (Pedido p : lista) {
            System.out.println(p);
        }
    }

    private void crearPedido() {
        listarUsuarios();
        Long usuarioId = leerLong("Id del usuario: ");
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        if (usuario == null) {
            System.out.println("Ese usuario no existe");
            return;
        }
        FormaPago formaPago = elegirFormaPago();
        Pedido pedido = new Pedido(LocalDate.now(), Estado.PENDIENTE, formaPago, usuario);

        // voy cargando productos hasta que diga que no
        boolean seguir = true;
        while (seguir) {
            listarProductos();
            Long prodId = leerLong("Id del producto: ");
            Producto producto = productoService.obtenerPorId(prodId);
            if (producto == null) {
                System.out.println("Ese producto no existe");
            } else {
                int cantidad = leerEntero("Cantidad: ");
                pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
                System.out.println("Agregado. Total parcial: $" + pedido.getTotal());
            }
            seguir = leerTexto("Agregar otro producto? (s/n): ").equalsIgnoreCase("s");
        }

        try {
            pedidoService.guardar(pedido);
            System.out.println("Pedido creado ok. Total: $" + pedido.getTotal());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarPedido() {
        listarPedidos();
        Long id = leerLong("Id del pedido a editar: ");
        Pedido p;
        try {
            p = pedidoService.obtenerPorId(id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        p.setEstado(elegirEstado());
        p.setFormaPago(elegirFormaPago());
        try {
            pedidoService.actualizar(p);
            System.out.println("Pedido actualizado");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        listarPedidos();
        Long id = leerLong("Id del pedido a eliminar: ");
        try {
            pedidoService.eliminar(id);
            System.out.println("Pedido dado de baja (se devolvio el stock)");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private FormaPago elegirFormaPago() {
        System.out.println("Forma de pago: 1. TARJETA  2. TRANSFERENCIA  3. EFECTIVO");
        int op = leerEntero("Seleccione: ");
        return switch (op) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            default -> FormaPago.EFECTIVO;
        };
    }

    private Estado elegirEstado() {
        System.out.println("Estado: 1. PENDIENTE  2. CONFIRMADO  3. TERMINADO  4. CANCELADO");
        int op = leerEntero("Seleccione: ");
        return switch (op) {
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            case 4 -> Estado.CANCELADO;
            default -> Estado.PENDIENTE;
        };
    }

    // ---------- helpers de lectura ----------

    private int leerEntero(String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un numero: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine(); // limpio el enter que queda en el buffer
        return n;
    }

    private long leerLong(String msg) {
        return leerEntero(msg);
    }

    private double leerDouble(String msg) {
        System.out.print(msg);
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un numero: ");
            sc.next();
        }
        double n = sc.nextDouble();
        sc.nextLine();
        return n;
    }

    private String leerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
}
