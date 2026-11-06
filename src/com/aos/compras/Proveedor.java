package com.aos.compras;

/** Datos del proveedor para el módulo académico de Compras AOS. */
public record Proveedor(long id, String nombre, String nit, String servicio,
                        String contacto, String correo, String telefono) {
    public Proveedor {
        nombre = limpiar(nombre); nit = limpiar(nit); servicio = limpiar(servicio);
        contacto = limpiar(contacto); correo = limpiar(correo); telefono = limpiar(telefono);
        if (nombre.isBlank() || nit.isBlank())
            throw new IllegalArgumentException("Nombre y NIT son obligatorios.");
        if (nombre.length() > 160 || nit.length() > 30 || servicio.length() > 200
                || contacto.length() > 120 || correo.length() > 160 || telefono.length() > 30)
            throw new IllegalArgumentException("Uno de los campos supera su longitud máxima.");
        if (!correo.isEmpty() && !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
    }
    private static String limpiar(String valor) { return valor == null ? "" : valor.trim(); }
}
