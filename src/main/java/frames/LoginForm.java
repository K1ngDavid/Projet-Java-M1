package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
     * 🔥 Initialise les composants graphiques avec un style UI/UX amélioré
     */
    private void initComponents() {
        JPanel pnlRoot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(40, 44, 52), getWidth(), getHeight(), new Color(72, 61, 139));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ✅ 📸 Panel pour l'image
        JPanel pnlImage = new JPanel(new BorderLayout());
        pnlImage.setOpaque(false);
        lblImage = new JLabel();
        loadCarImage();
        pnlImage.add(lblImage, BorderLayout.CENTER);

        // ✅ 📋 Panel de connexion (Glass UI)
        JPanel pnlLogin = new JPanel();
        pnlLogin.setLayout(new BoxLayout(pnlLogin, BoxLayout.Y_AXIS));
        pnlLogin.setOpaque(false);
        pnlLogin.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JPanel glassPanel = new JPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBackground(new Color(255, 255, 255, 100)); // 🎨 Effet Glass UI
        glassPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        glassPanel.setOpaque(true);
        glassPanel.setPreferredSize(new Dimension(400, 350));

        JLabel lblConnectez = new JLabel("🔑 Connexion");
        lblConnectez.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblConnectez.setForeground(Color.DARK_GRAY);
        lblConnectez.setAlignmentX(Component.CENTER_ALIGNMENT);

        glassPanel.add(lblConnectez);
        glassPanel.add(Box.createVerticalStrut(20));

        // 🔥 Champs d'entrée modernes
        emailEntry = createStyledTextField("✉ Email");
        pswdEntry = createStyledPasswordField("🔒 Mot de passe");

        glassPanel.add(emailEntry);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(pswdEntry);
        glassPanel.add(Box.createVerticalStrut(10));

        // ✅ 🔴 Label pour afficher les erreurs avec animation
        lblError = new JLabel("", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.BOLD, 14));
        lblError.setVisible(false);
        pnlLogin.add(lblError);
        glassPanel.add(Box.createVerticalStrut(10));

        // ✅ Bouton Connexion avec effet hover
        connexionButton = createStyledButton("🚀 Se Connecter", new Color(76, 175, 80));
        connexionButton.addActionListener(e -> loginVerification());

        // ✅ Bouton Inscription
        sInscrireButton = createStyledButton("📝 S'inscrire", new Color(33, 150, 243));

        glassPanel.add(connexionButton);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(sInscrireButton);

        pnlLogin.add(glassPanel);

        // ✅ Organisation des panels
        pnlRoot.add(pnlImage, BorderLayout.WEST);
        pnlRoot.add(pnlLogin, BorderLayout.CENTER);
        setContentPane(pnlRoot);

        sInscrireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterForm().setVisible(true);
            }
        });
    }


    /**
     * 🔥 Vérification du login avec meilleure gestion des erreurs
     */
    private void loginVerification() {
        try {
            String email = emailEntry.getText().trim();
            String password = new String(pswdEntry.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                throw new LoginException("❌ Veuillez remplir tous les champs !");
            }

            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new LoginException("⚠ Email invalide !");
            }

            if (!password.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$")) {
                throw new LoginException("⚠ Mot de passe invalide !");
            }

            if (clientService.clientCanLogIn(email, password)) {
                System.out.println("dazniodaznidazndozaindazon");
                ClientEntity client = clientService.getClientByEmail(email);
                new HomeForm(client);
                dispose();
            } else {
                throw new LoginException("❌ Identifiants incorrects !");
            }
        } catch (LoginException e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            revalidate();
        }
    }

    /**
     * 📸 Charge et ajuste l'image de la voiture
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
     * 🎨 Crée un champ de texte moderne
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.DARK_GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    /**
     * 🔑 Crée un champ de mot de passe moderne
     */
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.DARK_GRAY);
        return field;
    }

    /**
     * 🎨 Crée un bouton moderne avec effet hover
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
