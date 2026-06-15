-- SCRIPT DE CREACIÓN DE BASE DE DATOS Y CARGA DE DATOS DE PRUEBA

-- Crear la Base de Datos si no existe y usarla
CREATE DATABASE IF NOT EXISTS pedidos_db;
USE pedidos_db;

-- Eliminar tablas previas si existen
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS categoria;

-- creamos de la Tabla: categoria
CREATE TABLE categoria (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion VARCHAR(255),
                           eliminado BOOLEAN DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- creamos de la Tabla: usuario
CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         mail VARCHAR(150) NOT NULL UNIQUE,
                         celular VARCHAR(30),
                         contrasenia VARCHAR(255) NOT NULL,
                         rol VARCHAR(20) NOT NULL,
                         eliminado BOOLEAN DEFAULT 0,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- creamos de la Tabla: producto
CREATE TABLE producto (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          precio DECIMAL(10,2) NOT NULL,
                          descripcion VARCHAR(255),
                          stock INT NOT NULL,
                          imagen VARCHAR(255),
                          disponible BOOLEAN DEFAULT 1,
                          categoria_id BIGINT NOT NULL,
                          eliminado BOOLEAN DEFAULT 0,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- creamos de la Tabla: pedido
CREATE TABLE pedido (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha DATE NOT NULL,
                        estado VARCHAR(30) NOT NULL,
                        total DECIMAL(10,2) NOT NULL,
                        forma_pago VARCHAR(30) NOT NULL,
                        usuario_id BIGINT NOT NULL,
                        eliminado BOOLEAN DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- creamos de la Tabla: detalle_pedido
CREATE TABLE detalle_pedido (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                cantidad INT NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL,
                                producto_id BIGINT NOT NULL,
                                pedido_id BIGINT NOT NULL,
                                eliminado BOOLEAN DEFAULT 0,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id),
                                CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);

-- Inserción de Categorías
INSERT INTO categoria (nombre, descripcion) VALUES
                                                ('Hamburguesas', 'Deliciosas hamburguesas gourmet con papas fritas'),
                                                ('Bebidas', 'Gaseosas, aguas y jugos bien helados');

-- Inserción de Usuarios (roles validos: ADMIN, USUARIO)
INSERT INTO usuario (nombre, apellido, mail, celular, contrasenia, rol) VALUES
                                                                            ('Leonel', 'Ponce', 'leo.admin@foodstore.com', '2615551234', 'admin123', 'ADMIN'),
                                                                            ('Bruno', 'Fioucheta', 'bruno.cliente@gmail.com', '2615555678', 'cliente123', 'USUARIO');

-- Inserción de Productos
INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id) VALUES
                                                                                                ('Hamburguesa Simple', 4500.00, 'Medallon de carne de 120g, queso cheddar y aderezo especial', 30, 'hamburguesa_simple.png', 1, 1),
                                                                                                ('Hamburguesa Doble Bacon', 5800.00, 'Doble medallon de carne, doble cheddar, panceta crocante', 20, 'hamburguesa_doble_bacon.png', 1, 1),
                                                                                                ('Coca-Cola Original 500ml', 1500.00, 'Gaseosa sabor original bien fria', 50, 'coca_500.png', 1, 2),
                                                                                                ('Agua Mineral 500ml', 1200.00, 'Agua mineral sin gas bajo sodio', 40, 'agua_500.png', 1, 2);