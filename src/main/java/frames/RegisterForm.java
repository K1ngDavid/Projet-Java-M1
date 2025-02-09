package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.security.auth.login.FailedLoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterForm extends JFrame {

    private JPanel pnlRoot;
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnCreateAccount;
    private JButton btnBackToLogin;
    private JLabel lblError;
    private ClientService clientService;

    public RegisterForm() {
        super("📝 Inscription");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        clientService = new ClientService();
        initComponents();
        setVisible(true);
    }

    /**
     * 🔥 Initialise les composants graphiques avec un design moderne.
     */
    private void initComponents() {
        JPanel pnlRoot = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(72, 61, 139), getWidth(), getHeight(), new Color(123, 104, 238));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 📋 Panel Inscription (Effet Glass UI)
        JPanel pnlRegister = new JPanel();
        pnlRegister.setLayout(new BoxLayout(pnlRegister, BoxLayout.Y_AXIS));
        pnlRegister.setOpaque(false);
        pnlRegister.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel glassPanel = new JPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBackground(new Color(255, 255, 255, 150));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblRegister = new JLabel("📝 Inscription");
        lblRegister.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblRegister.setForeground(Color.DARK_GRAY);
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        glassPanel.add(lblRegister);
        glassPanel.add(Box.createVerticalStrut(20));

        // 🔥 Champs d'entrée stylisés
        txtUsername = createStyledTextField("👤 Nom d'utilisateur");
        txtEmail = createStyledTextField("✉ Email");
        txtPassword = createStyledPasswordField("🔑 Mot de passe");
        txtConfirmPassword = createStyledPasswordField("🔑 Confirmer mot de passe");

        glassPanel.add(txtUsername);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(txtEmail);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(txtPassword);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(txtConfirmPassword);
        glassPanel.add(Box.createVerticalStrut(10));

        // 🔴 Label Erreur
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.BOLD, 12));
        lblError.setVisible(false);
        glassPanel.add(lblError);
        glassPanel.add(Box.createVerticalStrut(10));

        // ✅ Bouton Création de Compte
        btnCreateAccount = createStyledButton("✅ Créer mon compte", new Color(76, 175, 80));
        btnCreateAccount.addActionListener(e -> registerUser());

        // 🔙 Bouton Retour à la Connexion
        btnBackToLogin = createStyledButton("↩ Retour", new Color(33, 150, 243));
        btnBackToLogin.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        glassPanel.add(btnCreateAccount);
        glassPanel.add(Box.createVerticalStrut(5));
        glassPanel.add(btnBackToLogin);

        pnlRegister.add(glassPanel);
        pnlRoot.add(pnlRegister, BorderLayout.CENTER);
        setContentPane(pnlRoot);
    }

    /**
     * 🔥 Vérification des données et création de compte.
     */
    private void registerUser() {
        try {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                throw new Exception("❌ Veuillez remplir tous les champs !");
            }

            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new Exception("⚠ Email invalide !");
            }

            if (!password.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$")) {
                throw new Exception("⚠ Mot de passe invalide !");
            }

            if (!password.equals(confirmPassword)) {
                throw new Exception("❌ Les mots de passe ne correspondent pas !");
            }

            ClientEntity client = new ClientEntity();
            client.setName(username);
            client.setEmail(email);
            client.setPassword(password);

            clientService.addClient(client);
            JOptionPane.showMessageDialog(this, "🎉 Inscription réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginForm();

        } catch (Exception e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            revalidate();
        }
    }

    /**
     * 🎨 Crée un champ de texte stylisé.
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    /**
     * 🔑 Crée un champ de mot de passe stylisé.
     */
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        return field;
    }

    /**
     * 🎨 Crée un bouton stylisé.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
