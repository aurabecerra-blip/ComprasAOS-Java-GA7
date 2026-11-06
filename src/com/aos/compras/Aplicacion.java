package com.aos.compras;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Interfaz de escritorio para registrar y mantener proveedores de prueba. */
public final class Aplicacion extends JFrame {
    private static final long serialVersionUID = 1L;
    private final ProveedorRepository repositorio;
    private final JTextField[] campos = new JTextField[6];
    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Nombre", "NIT", "Servicio", "Contacto", "Correo", "Teléfono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private long seleccionado = 0;
    private Aplicacion(ProveedorRepository repositorio) throws SQLException {
        super("Compras AOS - Proveedores - GA7");
        this.repositorio = repositorio;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 580); setLocationRelativeTo(null);
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        String[] etiquetas = {"Nombre *", "NIT *", "Servicio", "Contacto", "Correo", "Teléfono"};
        for (int i = 0; i < campos.length; i++) {
            formulario.add(new JLabel(etiquetas[i]));
            campos[i] = new JTextField(30); formulario.add(campos[i]);
        }
        JPanel acciones = new JPanel();
        agregarBoton(acciones, "Nuevo", () -> limpiar());
        agregarBoton(acciones, "Registrar", () -> {
            if (seleccionado != 0) throw new IllegalArgumentException("Pulsa Nuevo antes de registrar otro proveedor.");
            repositorio.insertar(leer()); limpiar(); cargar();
            JOptionPane.showMessageDialog(this, "Proveedor registrado.");
        });
        agregarBoton(acciones, "Consultar", () -> cargar());
        agregarBoton(acciones, "Actualizar", () -> {
            exigirSeleccion();
            if (!repositorio.actualizar(leer())) throw new IllegalArgumentException("El registro ya no existe.");
            limpiar(); cargar(); JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
        });
        agregarBoton(acciones, "Eliminar", () -> {
            exigirSeleccion();
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar el proveedor seleccionado de esta base de práctica?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            if (!repositorio.eliminar(seleccionado)) throw new IllegalArgumentException("El registro ya no existe.");
            limpiar(); cargar();
        });
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (evento.getValueIsAdjusting() || tabla.getSelectedRow() < 0) return;
            int fila = tabla.getSelectedRow();
            seleccionado = ((Number) modelo.getValueAt(fila, 0)).longValue();
            for (int i = 0; i < campos.length; i++) campos[i].setText((String) modelo.getValueAt(fila, i + 1));
        });
        add(formulario, BorderLayout.NORTH); add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH); cargar();
    }
    private Proveedor leer() {
        return new Proveedor(seleccionado, campos[0].getText(), campos[1].getText(), campos[2].getText(),
                campos[3].getText(), campos[4].getText(), campos[5].getText());
    }
    private void limpiar() { seleccionado = 0; tabla.clearSelection(); for (JTextField campo : campos) campo.setText(""); }
    private void exigirSeleccion() {
        if (seleccionado <= 0) throw new IllegalArgumentException("Selecciona una fila de la tabla.");
    }
    private void cargar() throws SQLException {
        modelo.setRowCount(0);
        for (Proveedor proveedor : repositorio.consultar()) modelo.addRow(new Object[]{proveedor.id(),
            proveedor.nombre(), proveedor.nit(), proveedor.servicio(), proveedor.contacto(), proveedor.correo(), proveedor.telefono()});
    }
    @FunctionalInterface private interface Accion { void ejecutar() throws SQLException; }
    private void agregarBoton(JPanel panel, String texto, Accion accion) {
        JButton boton = new JButton(texto); panel.add(boton);
        boton.addActionListener(evento -> {
            try { accion.ejecutar(); }
            catch (IllegalArgumentException error) { JOptionPane.showMessageDialog(this, error.getMessage()); }
            catch (SQLException error) {
                String mensaje = "23505".equals(error.getSQLState()) ? "Ese NIT ya está registrado." : "No fue posible guardar o consultar. Revisa los permisos de la carpeta y vuelve a intentar.";
                JOptionPane.showMessageDialog(this, mensaje, "Base de datos", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> {
            try {
                Files.createDirectories(Path.of("datos"));
                new Aplicacion(new ProveedorRepository("jdbc:h2:./datos/compras_proveedores")).setVisible(true);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, "No se pudo iniciar: " + error.getMessage());
            }
        });
    }
}
