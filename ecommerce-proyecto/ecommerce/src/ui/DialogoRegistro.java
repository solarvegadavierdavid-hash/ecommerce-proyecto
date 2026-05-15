package ui;

import datos.Controlador;
import modelo.Cliente;

import javax.swing.*;
import java.awt.*;

/**
 * DialogoRegistro — Formulario de registro de nuevo cliente
 */
public class DialogoRegistro extends JDialog {

    private Controlador controlador;
    private JTextField txtNombre, txtEmail, txtDireccion, txtTelefono;
    private JPasswordField txtPassword, txtConfirmar;
    private JLabel lblMensaje;

    public DialogoRegistro(JFrame parent, Controlador controlador) {
        super(parent, "Crear cuenta", true);
        this.controlador = controlador;
        setSize(400, 430);
        setLocationRelativeTo(parent);
        setResizable(false);
        construirUI();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(39, 174, 96));
        header.setPreferredSize(new Dimension(400, 50));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 12));
        JLabel titulo = new JLabel("Crear cuenta nueva");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);
        add(header, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.gridwidth = 2;

        String[] etiquetas = {"Nombre completo:", "Email:", "Contraseña:", "Confirmar contraseña:", "Dirección:", "Teléfono:"};
        JComponent[] campos = new JComponent[6];
        campos[0] = txtNombre = new JTextField();
        campos[1] = txtEmail = new JTextField();
        campos[2] = txtPassword = new JPasswordField();
        campos[3] = txtConfirmar = new JPasswordField();
        campos[4] = txtDireccion = new JTextField();
        campos[5] = txtTelefono = new JTextField();

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridy = i * 2;
            JLabel lbl = new JLabel(etiquetas[i]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            form.add(lbl, gbc);

            gbc.gridy = i * 2 + 1;
            campos[i].setPreferredSize(new Dimension(340, 30));
            campos[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            form.add(campos[i], gbc);
        }

        // Mensaje
        gbc.gridy = 12;
        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(192, 57, 43));
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));
        form.add(lblMensaje, gbc);

        add(form, BorderLayout.CENTER);

        // Botón registrar
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBtn.setBackground(Color.WHITE);
        panelBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        JButton btnRegistrar = new JButton("Crear cuenta");
        btnRegistrar.setBackground(new Color(39, 174, 96));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRegistrar.setPreferredSize(new Dimension(200, 38));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(e -> registrar());
        panelBtn.add(btnRegistrar);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());
        String dir = txtDireccion.getText().trim();
        String tel = txtTelefono.getText().trim();

        try {
            if (!pass.equals(confirmar)) {
                throw new Exception("Las contraseñas no coinciden.");
            }
            Cliente c = controlador.registrarCliente(nombre, email, pass, dir, tel);
            JOptionPane.showMessageDialog(this,
                    "¡Cuenta creada exitosamente!\nYa puedes iniciar sesión, " + c.getNombre() + ".",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            lblMensaje.setText(ex.getMessage());
        }
    }
}
