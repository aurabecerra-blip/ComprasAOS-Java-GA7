package com.aos.compras;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Prueba de integración con JDBC y una base de datos temporal real. */
public final class PruebaCrud {
    private static void comprobar(boolean condicion, String caso) {
        if (!condicion) throw new AssertionError(caso);
        System.out.println("OK - " + caso);
    }
    public static void main(String[] argumentos) throws Exception {
        Path carpeta = Files.createTempDirectory("compras-prueba-");
        String url = "jdbc:h2:" + carpeta.resolve("proveedores").toAbsolutePath();
        try {
            ProveedorRepository repositorio = new ProveedorRepository(url);
            comprobar(repositorio.consultar().isEmpty(), "Base de prueba inicialmente vacía");
            long id = repositorio.insertar(new Proveedor(0, "Proveedor de prueba", "PRUEBA-001", "Papelería", "Contacto ficticio", "prueba@example.com", "00123"));
            comprobar(id > 0 && repositorio.consultar().size() == 1, "Inserción y consulta JDBC");
            comprobar(repositorio.consultar().get(0).telefono().equals("00123"), "Teléfono conserva ceros iniciales");
            comprobar(new ProveedorRepository(url).consultar().size() == 1, "Persistencia al abrir otra conexión");
            try {
                repositorio.insertar(new Proveedor(0, "Duplicado", "PRUEBA-001", "", "", "", ""));
                throw new AssertionError("Se aceptó un NIT duplicado");
            } catch (SQLException error) { comprobar("23505".equals(error.getSQLState()), "Rechazo de NIT duplicado"); }
            comprobar(repositorio.actualizar(new Proveedor(id, "Proveedor actualizado", "PRUEBA-001", "Tecnología", "", "", "")), "Actualización JDBC");
            comprobar(repositorio.consultar().get(0).nombre().equals("Proveedor actualizado"), "Consulta confirma actualización");
            long otro = repositorio.insertar(new Proveedor(0, "O'Brien ?; --", "PRUEBA-002", "", "", "", ""));
            comprobar(repositorio.consultar().size() == 2, "Caracteres especiales se guardan como datos");
            comprobar(repositorio.eliminar(id) && repositorio.consultar().size() == 1, "Eliminación JDBC conserva otros registros");
            comprobar(!repositorio.eliminar(id), "Identificador inexistente no elimina otros datos");
            comprobar(repositorio.eliminar(otro) && repositorio.consultar().isEmpty(), "Base de práctica queda vacía");
            try { new Proveedor(0, " ", "NIT", "", "", "", ""); throw new AssertionError("Nombre vacío permitido"); }
            catch (IllegalArgumentException error) { System.out.println("OK - Validación de nombre obligatorio"); }
            try { new Proveedor(0, "Nombre", "NIT", "", "", "invalido", ""); throw new AssertionError("Correo inválido permitido"); }
            catch (IllegalArgumentException error) { System.out.println("OK - Validación del correo"); }
            System.out.println("RESULTADO: 13 comprobaciones correctas. Interfaz gráfica pendiente de revisión en Windows.");
        } finally {
            try (var rutas = Files.walk(carpeta)) {
                for (Path ruta : rutas.sorted(java.util.Comparator.reverseOrder()).toList()) Files.deleteIfExists(ruta);
            }
        }
    }
}
