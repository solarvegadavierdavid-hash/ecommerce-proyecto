package ui;

import datos.Controlador;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VentanaLogin — Pantalla de inicio de sesión y registro
 */
public class VentanaLogin extends JFrame {

    private Controlador controlador;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JLabel lblMensaje;

    public VentanaLogin(Controlador controlador) {
        this.controlador = controlador;
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("🛒 Mi Tienda Online — Iniciar Sesión");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 250));
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // Panel header
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(52, 73, 94));
        panelHeader.setPreferredSize(new Dimension(420, 80));
        panelHeader.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblTitulo = new JLabel("🛒  Mi Tienda Online");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo);
        add(panelHeader, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentro = new JPanel();
        panelCentro.setBackground(new Color(245, 245, 250));
        panelCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblEmail = new JLabel("Correo electrónico:");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panelCentro.add(lblEmail, gbc);

        gbc.gridy = 1;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(300, 35));
        panelCentro.add(txtEmail, gbc);

        // Password
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panelCentro.add(lblPass, gbc);

        gbc.gridy = 3;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(300, 35));
        panelCentro.add(txtPassword, gbc);

        // Mensaje de error/info
        gbc.gridy = 4;
        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(new Color(192, 57, 43));
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentro.add(lblMensaje, gbc);

        // Botones
        gbc.gridy = 5; gbc.gridwidth = 1;
        btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBackground(new Color(52, 73, 94));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogin.setPreferredSize(new Dimension(145, 38));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        panelCentro.add(btnLogin, gbc);

        gbc.gridx = 1;
        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(39, 174, 96));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRegistrar.setPreferredSize(new Dimension(145, 38));
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistrar.setFocusPainted(false);
        panelCentro.add(btnRegistrar, gbc);

        add(panelCentro, BorderLayout.CENTER);

        // Hint de demo
        JPanel panelHint = new JPanel();
        panelHint.setBackground(new Color(235, 245, 251));
        JLabel lblHint = new JLabel("Demo: admin@tienda.com / admin123  |  maria@email.com / 123456");
        lblHint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHint.setForeground(new Color(100, 100, 120));
        panelHint.add(lblHint);
        add(panelHint, BorderLayout.SOUTH);

        // Acciones
        btnLogin.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> abrirRegistro());
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) intentarLogin();
            }
        });
    }

    private void intentarLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario usuario = controlador.login(email, password);
            lblMensaje.setText(" ");
            dispose();

            if (usuario instanceof Administrador) {
                new VentanaAdmin(controlador).setVisible(true);
            } else {
                new VentanaCliente(controlador).setVisible(true);
            }

        } catch (Exception ex) {
            lblMensaje.setText(ex.getMessage());
            txtPassword.setText("");
        }
    }

    private void abrirRegistro() {
        new DialogoRegistro(this, controlador).setVisible(true);
    }
}
