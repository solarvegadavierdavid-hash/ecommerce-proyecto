package ui;

import datos.Controlador;
import modelo.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * VentanaCliente — Interfaz principal para el rol CLIENTE
 * Tabs: Catálogo | Carrito | Mis Pedidos
 */
public class VentanaCliente extends JFrame {

    private Controlador controlador;
    private JTabbedPane tabs;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JLabel lblTotal;
    private JLabel lblBienvenida;
    private JTextField txtBuscar;

    public VentanaCliente(Controlador controlador) {
        this.controlador = controlador;
        configurarVentana();
        construirUI();
        cargarProductos(controlador.getProductos());
    }

    private void configurarVentana() {
        setTitle("🛒 Mi Tienda Online — Cliente");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblBienvenida = new JLabel("Hola, " + controlador.getUsuarioActual().getNombre() + " 👋");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);
        header.add(lblBienvenida, BorderLayout.WEST);

        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setBackground(new Color(192, 57, 43));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> cerrarSesion());
        header.add(btnCerrar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Tabs ────────────────────────────────────────────────────────────
        tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("🛍️  Catálogo", construirTabCatalogo());
        tabs.addTab("🛒  Carrito", construirTabCarrito());
        tabs.addTab("📦  Mis pedidos", construirTabPedidos());

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) actualizarTablaCarrito();
            if (idx == 2) cargarPedidosCliente();
        });

        add(tabs, BorderLayout.CENTER);
    }

    // ── TAB CATÁLOGO ────────────────────────────────────────────────────────

    private JPanel construirTabCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Barra de búsqueda
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBuscar.setPreferredSize(new Dimension(250, 32));
        JButton btnBuscar = crearBoton("Buscar", new Color(52, 152, 219));
        JButton btnTodos = crearBoton("Ver todos", new Color(127, 140, 141));
        panelBuscar.add(new JLabel("Buscar: "));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnTodos);
        panel.add(panelBuscar, BorderLayout.NORTH);

        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Tipo"};
        modeloProductos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.setRowHeight(28);
        tablaProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setGridColor(new Color(220, 220, 230));
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tablaProductos);
        panel.add(scroll, BorderLayout.CENTER);

        // Botón agregar al carrito
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerCantidad.setPreferredSize(new Dimension(60, 32));
        JButton btnAgregar = crearBoton("+ Agregar al carrito", new Color(39, 174, 96));
        panelBtn.add(new JLabel("Cantidad: "));
        panelBtn.add(spinnerCantidad);
        panelBtn.add(btnAgregar);
        panel.add(panelBtn, BorderLayout.SOUTH);

        // Acciones
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (!texto.isEmpty()) {
                cargarProductos(controlador.buscarProductos(texto));
            }
        });
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarProductos(controlador.getProductos());
        });
        btnAgregar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = modeloProductos.getValueAt(fila, 0).toString();
            int cantidad = (int) spinnerCantidad.getValue();
            try {
                controlador.agregarAlCarrito(id, cantidad);
                JOptionPane.showMessageDialog(this,
                        "Producto agregado al carrito ✓", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                tabs.setTitleAt(1, "🛒  Carrito (" + controlador.getCarrito().getTotalItems() + ")");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void cargarProductos(ArrayList<Producto> lista) {
        modeloProductos.setRowCount(0);
        for (Producto p : lista) {
            modeloProductos.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getCategoria(),
                "$" + String.format("%,.0f", p.getPrecio()),
                p.getStock(), p.getTipo()
            });
        }
    }

    // ── TAB CARRITO ─────────────────────────────────────────────────────────

    private JPanel construirTabCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] cols = {"Producto", "Precio unit.", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaCarrito.setRowHeight(28);
        tablaCarrito.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaCarrito.setGridColor(new Color(220, 220, 230));
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // Footer carrito
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total: $0");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotal.setForeground(new Color(39, 174, 96));
        panelTotal.add(lblTotal);
        footer.add(panelTotal, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEliminar = crearBoton("Eliminar seleccionado", new Color(192, 57, 43));
        JButton btnVaciar = crearBoton("Vaciar carrito", new Color(127, 140, 141));
        JButton btnComprar = crearBoton("✓ Confirmar pedido", new Color(39, 174, 96));
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        footer.add(panelBotones, BorderLayout.EAST);
        panel.add(footer, BorderLayout.SOUTH);

        // Acciones
        btnEliminar.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un ítem.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String nombre = modeloCarrito.getValueAt(fila, 0).toString();
            // Buscar id por nombre
            for (ItemCarrito item : controlador.getCarrito().getItems()) {
                if (item.getProducto().getNombre().equals(nombre)) {
                    try {
                        controlador.eliminarDelCarrito(item.getProducto().getId());
                        actualizarTablaCarrito();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        });
        btnVaciar.addActionListener(e -> {
            controlador.getCarrito().vaciar();
            actualizarTablaCarrito();
        });
        btnComprar.addActionListener(e -> abrirDialogoPago());

        return panel;
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (ItemCarrito item : controlador.getCarrito().getItems()) {
            modeloCarrito.addRow(new Object[]{
                item.getProducto().getNombre(),
                "$" + String.format("%,.0f", item.getProducto().getPrecio()),
                item.getCantidad(),
                "$" + String.format("%,.0f", item.getSubtotal())
            });
            total += item.getSubtotal();
        }
        lblTotal.setText("Total: $" + String.format("%,.0f", total));
        tabs.setTitleAt(1, "🛒  Carrito (" + controlador.getCarrito().getTotalItems() + ")");
    }

    private void abrirDialogoPago() {
        if (controlador.getCarrito().estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] metodos = {"Tarjeta de crédito", "Efectivo", "Transferencia bancaria"};
        String metodo = (String) JOptionPane.showInputDialog(this,
                "Selecciona el método de pago:\n\nTotal a pagar: $"
                + String.format("%,.0f", controlador.getCarrito().getTotal()),
                "Confirmar pedido", JOptionPane.PLAIN_MESSAGE, null, metodos, metodos[0]);
        if (metodo == null) return;

        try {
            Pedido pedido = controlador.confirmarPedido(metodo);
            actualizarTablaCarrito();
            cargarProductos(controlador.getProductos());
            JOptionPane.showMessageDialog(this,
                    "✅ Pedido confirmado exitosamente!\n\nID: " + pedido.getIdPedido()
                    + "\nTotal: $" + String.format("%,.0f", pedido.getTotal())
                    + "\nMétodo: " + metodo,
                    "Pedido exitoso", JOptionPane.INFORMATION_MESSAGE);
            tabs.setSelectedIndex(2);
            cargarPedidosCliente();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al procesar", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── TAB PEDIDOS ─────────────────────────────────────────────────────────

    private JPanel construirTabPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] cols = {"# Pedido", "Fecha", "Total", "Método pago", "Estado"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaPedidos.setRowHeight(28);
        tablaPedidos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaPedidos.setGridColor(new Color(220, 220, 230));
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        JButton btnDetalle = crearBoton("Ver detalle", new Color(52, 152, 219));
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnDetalle);
        panel.add(panelBtn, BorderLayout.SOUTH);

        btnDetalle.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un pedido.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
            String idPedido = modeloPedidos.getValueAt(fila, 0).toString();
            mostrarDetallePedido(idPedido);
        });

        return panel;
    }

    private void cargarPedidosCliente() {
        modeloPedidos.setRowCount(0);
        ArrayList<Pedido> pedidos = controlador.getPedidosCliente(controlador.getUsuarioActual().getId());
        for (Pedido p : pedidos) {
            modeloPedidos.addRow(new Object[]{
                p.getIdPedido(), p.getFechaCreacion(),
                "$" + String.format("%,.0f", p.getTotal()),
                p.getMetodoPago(), p.getEstado()
            });
        }
    }

    private void mostrarDetallePedido(String idPedido) {
        ArrayList<Pedido> pedidos = controlador.getPedidosCliente(controlador.getUsuarioActual().getId());
        for (Pedido p : pedidos) {
            if (p.getIdPedido().equals(idPedido)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Pedido: ").append(p.getIdPedido()).append("\n");
                sb.append("Fecha: ").append(p.getFechaCreacion()).append("\n");
                sb.append("Estado: ").append(p.getEstado()).append("\n\n");
                sb.append("── Productos ──────────────\n");
                for (ItemCarrito item : p.getItems()) {
                    sb.append("  • ").append(item.toString()).append("\n");
                }
                sb.append("\nTotal: $").append(String.format("%,.0f", p.getTotal()));
                sb.append("\nMétodo: ").append(p.getMetodoPago());
                JOptionPane.showMessageDialog(this, sb.toString(), "Detalle del pedido", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void cerrarSesion() {
        controlador.logout();
        dispose();
        new VentanaLogin(controlador).setVisible(true);
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
