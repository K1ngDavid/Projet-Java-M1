package frames;

import entity.ClientEntity;
import service.ClientService;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginForm extends JFrame {

    private JPanel pnlRoot;
    private JTextField emailEntry;
    private JPasswordField pswdEntry;
    private JButton connexionButton;
    private JButton sInscrireButton;
    private JLabel lblError;
    private JLabel lblImage;
    private ClientService clientService;

    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 350;

    public LoginForm() {
        super("🔑 Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setResizable(false);
        setLocationRelativeTo(null);

        clientService = new ClientService();
        initComponents();
        setVisible(true);
    }

    /**
     * 🔥 Initialisation des composants avec un style moderne
     */
    private void initComponents() {
        JPanel pnlRoot = new JPanel(new BorderLayout());
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ 📸 Panel Image
        JPanel pnlImage = new JPanel(new BorderLayout());
        pnlImage.setOpaque(false);
        lblImage = new JLabel();
        loadCarImage();
        pnlImage.add(lblImage, BorderLayout.CENTER);

        // ✅ 📋 Panel Connexion
        JPanel pnlLogin = new JPanel();
        pnlLogin.setLayout(new BoxLayout(pnlLogin, BoxLayout.Y_AXIS));
        pnlLogin.setOpaque(false);
        pnlLogin.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JPanel glassPanel = new JPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBackground(new java.awt.Color(16, 84, 129)); // 🎨 Effet "Glass UI"
        glassPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblConnectez = new JLabel("🔑 Connexion");
        lblConnectez.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblConnectez.setForeground(Color.WHITE);
        lblConnectez.setAlignmentX(Component.CENTER_ALIGNMENT);

        glassPanel.add(lblConnectez);

        // 🔥 Champs d'entrée stylisés
        emailEntry = createStyledTextField("✉ Email");
        pswdEntry = createStyledPasswordField("🔒 Mot de passe");

        glassPanel.add(emailEntry);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(pswdEntry);
        glassPanel.add(Box.createVerticalStrut(10));

        // ✅ 🔴 Label Erreur
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.BOLD, 14));
        lblError.setVisible(false);
        glassPanel.add(lblError);
        glassPanel.add(Box.createVerticalStrut(10));

        // ✅ Boutons modernisés
        connexionButton = createStyledButton("🚀 Se Connecter", new Color(76, 175, 80));
        connexionButton.addActionListener(e -> loginVerification());

        sInscrireButton = createStyledButton("📝 S'inscrire", new Color(33, 150, 243));

        glassPanel.add(connexionButton);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(sInscrireButton);

        pnlLogin.add(glassPanel);

        // ✅ Organisation des panels
        pnlRoot.add(pnlImage, BorderLayout.WEST);
        pnlRoot.add(pnlLogin, BorderLayout.CENTER);
        setContentPane(pnlRoot);
    }

    /**
     * 🔥 Vérification du login avec animation d'erreur
     */
    private void loginVerification() {
        try {
            String email = emailEntry.getText().trim();
            String password = new String(pswdEntry.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                throw new LoginException("❌ Veuillez remplir tous les champs !");
            }

            if (!clientService.clientCanLogIn(email, password)) {
                throw new LoginException("❌ Identifiants incorrects !");
            }

            ClientEntity client = clientService.getClientByEmail(email);
            new HomeForm(client);
            dispose();

        } catch (LoginException e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            revalidate();
        }
    }

    /**
     * 📸 Charge l'image de la voiture
     */
    private void loadCarImage() {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource("/images/voiture-connexion.jpg"));
            Image resizedImage = originalImage.getScaledInstance(MAX_WIDTH, MAX_HEIGHT, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(resizedImage));
        } catch (IOException | NullPointerException e) {
            lblImage.setText("🚗 Image non disponible");
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    /**
     * 🎨 Crée un champ de texte stylisé
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.DARK_GRAY);

        return field;
    }

    /**
     * 🔑 Crée un champ de mot de passe stylisé
     */
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.DARK_GRAY);
        return field;
    }

    /**
     * 🎨 Crée un bouton modernisé
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }
}
