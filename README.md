# Compras AOS Proveedores en Java

Evidencia GA7-220501096-AA2-EV01. Módulo académico independiente basado en la gestión de proveedores del aplicativo Compras AOS de Aura Maria Becerra Ochoa.

## Abrir en Windows
1. Extraer todo el ZIP a una carpeta normal.
2. Entrar en ComprasProveedores y ejecutar iniciar.bat con doble clic.
3. Se utiliza Java 17 o superior. El iniciador busca primero el Java de Android Studio en su ubicación habitual y, si no existe, usa el Java del sistema.
4. Pulsar Nuevo, completar Nombre y NIT y pulsar Registrar. Los demás campos son opcionales. El correo, si se diligencia, debe tener formato válido.
5. Seleccionar una fila para editar sus campos y pulsar Actualizar. Consultar recarga la tabla.
6. Para eliminar un proveedor de prueba, seleccionar su fila, pulsar Eliminar y confirmar. Nuevo limpia el formulario.
7. Cerrar y abrir otra vez para comprobar que el registro se conserva.

## Base de datos
H2 2.3.232 mediante JDBC. Se crea automáticamente datos/compras_proveedores.mv.db. No requiere instalar MySQL ni configurar un servidor. Se usa una cuenta local de práctica sa sin contraseña; no se habilita servidor ni acceso remoto. La base contiene únicamente los datos que se registren en esta aplicación. No conecta con la base productiva de Compras ni reproduce sus roles, auditoría, asociaciones o reglas de eliminación.

## Código y estándares
Paquete com.aos.compras. Clases Proveedor, ProveedorRepository y Aplicacion. Clases en PascalCase; métodos y variables en camelCase. PreparedStatement utiliza parámetros; try-with-resources libera conexiones, sentencias y resultados. La tabla define NIT único y un identificador generado. Los tamaños se validan antes de guardar.

## Compilar y probar
Con JDK 17 o superior disponible en PATH, ejecutar compilar.bat. Ejecutar probar.bat para repetir la prueba de integración en una base temporal independiente. El JAR ya se incluye compilado para Java 17.

## Verificación realizada
Compilación con Eclipse ECJ 3.38.0 para Java 17 y ejecución con OpenJDK 17 en Linux. Trece comprobaciones de integración correctas, incluidas CRUD, persistencia, NIT duplicado y validaciones. Ver resultados_pruebas.txt. La interfaz Swing y los iniciadores BAT requieren comprobación en el computador Windows de la aprendiz.

## Versionamiento y publicación
Se incluye historial Git local con un commit y una copia portable historial.bundle. Para recuperar el historial: git clone historial.bundle ComprasProveedores. La publicación de este módulo Java en GitHub está pendiente. El archivo ENLACE_REPOSITORIO.txt debe completarse con la URL real que contenga este código antes de entregar.

## Relación con el proyecto
El aplicativo PHP existente usa nombre, NIT, servicio, contacto, correo y teléfono en SupplierController. Aquí se demuestra ese mismo tipo de registro mediante Java y JDBC. Es una práctica independiente: su base y sus validaciones no son equivalentes a toda la lógica del aplicativo web.

## Dependencia
H2 Database 2.3.232, https://h2database.com/ . Binario original distribuido en Maven Central: https://repo.maven.apache.org/maven2/com/h2database/h2/2.3.232/ . Licencias MPL 2.0 o EPL 1.0, a elección; consultar LICENCIAS_H2.txt y los textos en lib.
