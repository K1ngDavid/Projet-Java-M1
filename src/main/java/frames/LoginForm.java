package frames;

import frames.HomeForm;
import jakarta.persistence.EntityManager;
import service.ClientService;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginForm extends AbstractFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton connexionButton;
    private JPanel pnlRoot;
    private JLabel lblError;
    private JPanel pnlUsername;
    private JPanel pnlPassword;
    private JPanel pnlConnection;
    private JButton sInscrireButton;
    private JPanel pnlImage;
    private JLabel lblImage;
    private JPanel pnlLogin;
    private JLabel lblConnectez;

    private ClientService clientService;
    private EntityManager entityManager;

    private static final int MAX_WIDTH = 550; // Largeur maximale
    private static final int MAX_HEIGHT = 500; // Hauteur maximale

    public LoginForm() {
        super();
        // Initialisation de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);

        clientService = new ClientService();

        // Chargement et configuration de l'image
        String localImagePath = "/images/voiture-connexion.jpg"; // Chemin relatif à src/main/resources
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(localImagePath));
            Image resizedImage = resizeImageWithMaxDimensions(originalImage, MAX_WIDTH, MAX_HEIGHT);
            lblImage.setIcon(new ImageIcon(resizedImage));
        } catch (IOException | NullPointerException e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
        }

        // Définir le panneau principal comme contenu de la fenêtre


        pnlLogin.setSize(600,400);
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlImage, BorderLayout.WEST);
        pnlRoot.add(pnlLogin, BorderLayout.CENTER);
        this.setContentPane(pnlRoot);
        this.setVisible(true);
        connexionButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                loginVerification();
            }
        });
    }


    private Image resizeImageWithMaxDimensions(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculer les nouvelles dimensions tout en respectant les proportions
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scalingFactor = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scalingFactor);
        int newHeight = (int) (originalHeight * scalingFactor);

        // Redimensionner l'image
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    void loginVerification() {
        try {
            if (textField1.getText().matches("^[a-zA-Z0-9._\\-+*]{3,}$") &&
                    textField2.getText().matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$")) {
                if (clientService.clientCanLogIn(textField1.getText(), textField2.getText())) {
                    System.out.println("Client found");
                    HomeForm homeForm = new HomeForm();
                    dispose();
                } else {
                    throw new LoginException("User not found");
                }
            } else {
                throw new LoginException("Login or password not in right format");
            }
        } catch (LoginException e) {
            lblError.setText(e.getMessage());
            lblError.setForeground(Color.red);
            this.revalidate();
        }
    }
}
