# Food Store - Sistema de Gestión de Pedidos (Consola + JDBC)

Este proyecto es el Trabajo Práctico Integrador (TPI) de la materia **Programación II** de la Tecnicatura Universitaria en Programación de la Universidad Tecnológica Nacional (UTN). Consiste en un sistema de consola interactivo para gestionar categorías, productos, usuarios y pedidos de un negocio de comidas, con persistencia real en una base de datos relacional MySQL mediante JDBC puro.

---

## 📂 Documentación Académica e Informe Técnico (PDF)
*   **Enlace al informe técnico académico (PDF):** [👉 DESCARGAR INFORME TÉCNICO PDF 👈](documentacion_tpi.pdf)
*   *El informe incluye carátula, marco teórico, decisiones de diseño técnico, diagrama UML detallado, bitácora de dificultades y bibliografía.*

---

## 🛠️ Requisitos del Sistema
Para ejecutar este proyecto en tu computadora local necesitas tener instalado:
*   **Java Development Kit (JDK):** Versión 21 (Recomendado Oracle OpenJDK).
*   **Motor de Base de Datos:** MySQL Server (Versión 8.0 o superior, por defecto en puerto `3306`).
*   **IDE Recomendado:** IntelliJ IDEA.
*   **Driver de MySQL:** `mysql-connector-j-8.4.0.jar` (provisto dentro del proyecto en la carpeta `/lib`).

---

## 🗄️ Paso 1: Configuración de la Base de Datos (MySQL)
El proyecto incluye un script SQL con la estructura completa de las tablas, relaciones con claves foráneas, restricciones de unicidad y un set de datos de prueba precargados para facilitar la evaluación.

1. Abre tu gestor de base de datos MySQL local (MySQL Workbench, DBeaver o consola de comandos).
2. Abre y ejecuta en su totalidad el archivo **`schema.sql`** ubicado en la raíz de este proyecto.
3. Esto creará automáticamente la base de datos `pedidos_db`, las 5 tablas (`categoria`, `usuario`, `producto`, `pedido`, `detalle_pedido`) e insertará los datos iniciales de prueba (2 categorías, 2 usuarios y 4 productos).

---

## 🔌 Paso 2: Configuración de la Conexión en Java
Antes de ejecutar el programa, debes ajustar tus credenciales locales de MySQL en la clase de conexión centralizada.

1. Abre el archivo `ConexionDB.java` ubicado en la ruta:  
   `src/integrado/prog2/config/ConexionDB.java`
2. Modifica la variable **`PASSWORD`** con la contraseña real de tu servidor MySQL local (si tu MySQL no tiene contraseña, déjala con comillas vacías `""`):
   ```java
   private static final String PASSWORD = "tu_contraseña_local";

---

> **Nota:** El usuario por defecto está configurado como **`root`** y el puerto de red como **`3306`**. Si tu configuración es diferente, puedes modificar estos valores en la clase correspondiente.

# 🚀 Paso 3: Cómo Ejecutar el Proyecto

Para ejecutar la aplicación desde **IntelliJ IDEA**:

1. Abre el proyecto en IntelliJ IDEA.
2. Asegúrate de que el driver ubicado en `lib/mysql-connector-j-8.4.0.jar` esté correctamente asociado como librería del módulo:
    - `File` → `Project Structure` → `Modules` → `Dependencies`
    - Verifica que el driver de MySQL se encuentre agregado y habilitado.
3. Ve al menú:
    - `Build` → `Rebuild Project`
4. Abre el archivo:
   ```
   src/integrado/prog2/Main.java
   ```
5. Haz clic en el botón **▶ Play** ubicado junto al método:
   ```java
   public static void main(String[] args)
   ```
6. Se iniciará la interfaz interactiva por consola.

---

# 📐 Arquitectura del Proyecto (Patrones Aplicados)

El sistema fue desarrollado siguiendo una **arquitectura por capas desacoplada**, favoreciendo la alta cohesión, la reutilización de código y el mantenimiento.

## 📁 `entities/`
Contiene las clases de dominio definidas a partir del diagrama UML:

- Base
- Categoria
- Producto
- Usuario
- Pedido
- DetallePedido

Todas heredan de la clase abstracta **Base**, aplicando conceptos de **herencia** y **polimorfismo**.

## 📁 `enums/`

Enumeraciones utilizadas para representar valores acotados del dominio:

- Rol
- Estado
- FormaPago

## 📁 `exception/`

Excepciones personalizadas utilizadas para controlar la lógica de negocio:

- EntidadNoEncontradaException
- StockInvalidoException
- EmailDuplicadoException

## 📁 `dao/`

Capa de persistencia implementada con **JDBC puro**.

Incluye:

- Interfaces DAO
- Implementaciones concretas
- Operaciones:
    - INSERT
    - SELECT (incluyendo INNER JOIN)
    - UPDATE
    - DELETE lógico

Se utilizan **PreparedStatement** y **try-with-resources** para garantizar seguridad y correcta liberación de recursos.

## 📁 `service/`

Capa encargada de la lógica de negocio.

Responsabilidades:

- Validación de reglas de negocio.
- Control de stock.
- Validación de precios.
- Verificación de unicidad del correo electrónico.
- Manejo de transacciones mediante:
    - `commit`
    - `rollback`

para asegurar la integridad de los datos ante errores.

## 📁 `menu/`

Contiene toda la interfaz interactiva por consola.

## 📁 `main/`

Punto de entrada de la aplicación.

---

# 👥 Autores del Proyecto

- **Leonel Ponce De Leon**  
  *UTN - Tecnicatura Universitaria en Programación*

- **Bruno Fioucheta**  
  *UTN - Tecnicatura Universitaria en Programación*