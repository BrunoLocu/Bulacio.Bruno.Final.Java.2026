# InventarioPC — Sistema de gestión de insumos informáticos

## Sobre mí

Soy [tu nombre y apellido], estudiante de programación. Este proyecto fue desarrollado como trabajo práctico final de la materia Programación II, aplicando conceptos de programación orientada a objetos, colecciones, genéricos, persistencia de archivos y desarrollo de interfaces gráficas con Java.

## Resumen del proyecto

**InventarioPC** es una aplicación de escritorio desarrollada en Java y JavaFX que permite administrar un inventario de componentes de computadoras.

El sistema permite registrar memorias RAM, placas de video y fuentes de alimentación, almacenando sus características específicas, precios y cantidades disponibles.

### Funcionalidades principales

- Agregar nuevos productos al inventario.
- Consultar los productos registrados mediante una tabla.
- Modificar los datos de productos existentes.
- Eliminar productos con confirmación.
- Validar campos obligatorios, valores numéricos y códigos duplicados.
- Filtrar productos por precio máximo.
- Ordenar productos por código, precio o marca.
- Guardar y recuperar inventarios en formatos DAT, CSV y JSON.
- Exportar reportes TXT con los productos filtrados.

La aplicación no utiliza bases de datos. La información se conserva mediante archivos locales.

## Tecnologías utilizadas

- **Java 21:** lenguaje principal.
- **JavaFX 21:** interfaz gráfica.
- **Programación orientada a objetos:** herencia, encapsulamiento, abstracción y polimorfismo.
- **Colecciones y genéricos:** listas, iteradores, `Comparable`, `Comparator` y wildcards.
- **Interfaces funcionales:** utilización de `Consumer` y `Function`.
- **Persistencia:** serialización Java, CSV, JSON y TXT.
- **Manejo de excepciones:** validación y control de errores.

## Cómo utilizar la aplicación

Para ejecutar el proyecto se necesita Java 21 y el SDK de JavaFX compatible.

1. Descargar o clonar el repositorio.
2. Abrir el proyecto en Visual Studio Code.
3. Configurar la ruta del SDK de JavaFX en los argumentos de ejecución.
4. Ejecutar la clase `ui.InventarioApp`.
5. Utilizar los botones de la ventana principal para administrar los productos.

Los productos se agregan seleccionando su tipo e ingresando los datos solicitados. Para modificar o eliminar un producto, primero debe seleccionarse su fila en la tabla.

Los archivos pueden guardarse y cargarse mediante sus respectivos botones. Los archivos se generan en el directorio de trabajo de la aplicación.

## Interfaz gráfica

### Pantalla principal

![Ventana principal de InventarioPC](docs/capturas/principal.png)

### Agregar producto

![Formulario para agregar productos](docs/capturas/agregar.png)

### Modificar producto

![Formulario para modificar productos](docs/capturas/modificar.png)

## Diagrama de clases UML

El siguiente diagrama representa la estructura del proyecto, sus clases principales, interfaces, herencia y relaciones.

![Diagrama UML de InventarioPC](docs/uml/diagrama-clases.png)

## Ejemplos de archivos generados

El repositorio incluye archivos generados mediante las funcionalidades de persistencia de la aplicación.

| Formato | Descripción | Archivo |
|---|---|---|
| DAT | Serialización de objetos Java | [inventario.dat](ejemplos/inventario.dat) |
| CSV | Inventario exportado como valores separados | [inventario.csv](ejemplos/inventario.csv) |
| JSON | Inventario representado en formato JSON | [inventario.json](ejemplos/inventario.json) |
| TXT | Reporte de productos filtrados | [reporte_inventario.txt](ejemplos/reporte_inventario.txt) |

## Organización del proyecto

El proyecto está dividido en paquetes según sus responsabilidades:

- `modelo`: clases de los componentes informáticos.
- `contratos`: interfaces.
- `inventario`: gestión, comparadores y operaciones sobre los productos.
- `excepciones`: excepciones personalizadas.
- `persistencia`: lectura y escritura de archivos.
- `ui`: interfaz gráfica desarrollada con JavaFX.

## Objetivo académico

Aplicar de forma integrada los contenidos de Programación II mediante el desarrollo de un sistema funcional de gestión de inventarios, utilizando programación orientada a objetos, estructuras de datos, manejo de archivos y una interfaz gráfica.
