package ui;

import datos.Controlador;
import modelo.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * VentanaAdmin — Panel de administración
 * Tabs: Productos | Pedidos | Usuarios
 */
public class VentanaAdmin extends JFrame {

    private Controlador controlador;
    private JTabbedPane tabs;

    // Tabla productos
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;

    // Tabla pedidos
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    // Tabla usuarios
    private JTable tablaUsuarios;
    private DefaultTableModel modeloUsuarios;

    public VentanaAdmin(Controlador controlador) {
        this.controlador = controlador;
        configurarVentana();
        construirUI();
        cargarTablaProductos();
    }

    private void configurarVentana() {
        setTitle("🛒 Mi Tienda Online — Panel Admin");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel titulo = new JLabel("⚙️  Panel de Administración — " + controlador.getUsuarioActual().getNombre());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);
        JButton btnCerrar = crearBoton("Cerrar sesión", new Color(192, 57, 43));
        btnCerrar.addActionListener(e -> cerrarSesion());
        header.add(btnCerrar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("📦  Productos", construirTabProductos());
        tabs.addTab("🧾  Pedidos", construirTabPedidos());
        tabs.addTab("👥  Usuarios", construirTabUsuarios());

        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i == 0) cargarTablaProductos();
            if (i == 1) cargarTablaPedidos();
            if (i == 2) cargarTablaUsuarios();
        });

        add(tabs, BorderLayout.CENTER);
    }

    // ── PRODUCTOS ────────────────────────────────────────────────────────────

    private JPanel construirTabProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] cols = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Tipo"};
        modeloProductos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.setRowHeight(28);
        tablaProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaProductos.setGridColor(new Color(220, 220, 230));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = crearBoton("+ Nuevo producto", new Color(39, 174, 96));
        JButton btnEditar = crearBoton("✏ Editar", new Color(52, 152, 219));
        JButton btnEliminar = crearBoton("🗑 Eliminar", new Color(192, 57, 43));
        panelBtn.add(btnNuevo);
        panelBtn.add(btnEditar);
        panelBtn.add(btnEliminar);
        panel.add(panelBtn, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> abrirFormularioProducto(null));
        btnEditar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) { avisoSeleccion(); return; }
            String id = modeloProductos.getValueAt(fila, 0).toString();
            abrirFormularioProducto(controlador.buscarProductoPorId(id));
        });
        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) { avisoSeleccion(); return; }
            String id = modeloProductos.getValueAt(fila, 0).toString();
            int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar el producto " + id + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    controlador.eliminarProducto(id);
                    cargarTablaProductos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void cargarTablaProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : controlador.getProductos()) {
            modeloProductos.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getCategoria(),
                "$" + String.format("%,.0f", p.getPrecio()),
                p.getStock(), p.getTipo()
            });
        }
    }

    private void abrirFormularioProducto(Producto producto) {
        JTextField txtId = new JTextField(producto != null ? producto.getId() : controlador.generarIdProducto());
        JTextField txtNombre = new JTextField(producto != null ? producto.getNombre() : "");
        JTextField txtDesc = new JTextField(producto != null ? producto.getDescripcion() : "");
        JTextField txtPrecio = new JTextField(producto != null ? String.valueOf((int)producto.getPrecio()) : "");
        JTextField txtStock = new JTextField(producto != null ? String.valueOf(producto.getStock()) : "");
        JTextField txtCategoria = new JTextField(producto != null ? producto.getCategoria() : "");
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"FÍSICO", "DIGITAL"});

        if (producto != null) {
            txtId.setEditable(false);
            cmbTipo.setSelectedItem(producto.getTipo());
        }

        Object[] campos = {
            "ID:", txtId, "Nombre:", txtNombre, "Descripción:", txtDesc,
            "Precio:", txtPrecio, "Stock:", txtStock, "Categoría:", txtCategoria,
            "Tipo:", cmbTipo
        };

        int res = JOptionPane.showConfirmDialog(this, campos,
                producto == null ? "Nuevo producto" : "Editar producto",
                JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                String desc = txtDesc.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                String cat = txtCategoria.getText().trim();
                String tipo = cmbTipo.getSelectedItem().toString();

                Producto p;
                if (tipo.equals("FÍSICO")) {
                    p = new ProductoFisico(id, nombre, desc, precio, stock, cat, 0.5, "N/A");
                } else {
                    p = new ProductoDigital(id, nombre, desc, precio, stock, cat, "N/A", "PDF");
                }

                if (producto == null) {
                    controlador.agregarProducto(p);
                    JOptionPane.showMessageDialog(this, "Producto agregado ✓");
                } else {
                    controlador.actualizarProducto(p);
                    JOptionPane.showMessageDialog(this, "Producto actualizado ✓");
                }
                cargarTablaProductos();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y stock deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── PEDIDOS ──────────────────────────────────────────────────────────────

    private JPanel construirTabPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] cols = {"# Pedido", "Cliente", "Fecha", "Total", "Método", "Estado"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaPedidos.setRowHeight(28);
        tablaPedidos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaPedidos.setGridColor(new Color(220, 220, 230));
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCambiarEstado = crearBoton("Cambiar estado", new Color(52, 152, 219));
        JButton btnDetalle = crearBoton("Ver detalle", new Color(127, 140, 141));
        panelBtn.add(btnDetalle);
        panelBtn.add(btnCambiarEstado);
        panel.add(panelBtn, BorderLayout.SOUTH);

        btnCambiarEstado.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila < 0) { avisoSeleccion(); return; }
            String id = modeloPedidos.getValueAt(fila, 0).toString();
            Pedido.Estado[] estados = Pedido.Estado.values();
            Pedido.Estado sel = (Pedido.Estado) JOptionPane.showInputDialog(this,
                    "Nuevo estado para pedido " + id + ":", "Cambiar estado",
                    JOptionPane.PLAIN_MESSAGE, null, estados, estados[0]);
            if (sel != null) {
                try {
                    controlador.actualizarEstadoPedido(id, sel);
                    cargarTablaPedidos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDetalle.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila < 0) { avisoSeleccion(); return; }
            String id = modeloPedidos.getValueAt(fila, 0).toString();
            for (Pedido p : controlador.getTodosPedidos()) {
                if (p.getIdPedido().equals(id)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Pedido: ").append(p.getIdPedido()).append("\n");
                    sb.append("Cliente: ").append(p.getNombreCliente()).append("\n");
                    sb.append("Fecha: ").append(p.getFechaCreacion()).append("\n\n");
                    for (ItemCarrito item : p.getItems()) sb.append("  • ").append(item).append("\n");
                    sb.append("\nTotal: $").append(String.format("%,.0f", p.getTotal()));
                    JOptionPane.showMessageDialog(this, sb.toString(), "Detalle", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        });

        return panel;
    }

    private void cargarTablaPedidos() {
        modeloPedidos.setRowCount(0);
        for (Pedido p : controlador.getTodosPedidos()) {
            modeloPedidos.addRow(new Object[]{
                p.getIdPedido(), p.getNombreCliente(), p.getFechaCreacion(),
                "$" + String.format("%,.0f", p.getTotal()), p.getMetodoPago(), p.getEstado()
            });
        }
    }

    // ── USUARIOS ─────────────────────────────────────────────────────────────

    private JPanel construirTabUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] cols = {"ID", "Nombre", "Email", "Rol"};
        modeloUsuarios = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(28);
        tablaUsuarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaUsuarios.setGridColor(new Color(220, 220, 230));
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        return panel;
    }

    private void cargarTablaUsuarios() {
        modeloUsuarios.setRowCount(0);
        for (Usuario u : controlador.getUsuarios()) {
            modeloUsuarios.addRow(new Object[]{u.getId(), u.getNombre(), u.getEmail(), u.getRol()});
        }
    }

    // ── UTILIDADES ───────────────────────────────────────────────────────────

    private void cerrarSesion() {
        controlador.logout();
        dispose();
        new VentanaLogin(controlador).setVisible(true);
    }

    private void avisoSeleccion() {
        JOptionPane.showMessageDialog(this, "Selecciona un elemento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }
}
