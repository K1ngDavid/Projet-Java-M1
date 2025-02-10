package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.CommandLineEntityPK;
import entity.ReviewEntity;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import service.CommandService;
import service.ReviewService;
import tools.ImageUtils;
import tools.ReviewDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ProductForm extends AbstractFrame {
    // Entités et services
    private VehicleEntity vehicle;
    private CommandService commandService;
    private ReviewService reviewService;
    private CommandEntity activeCommand;

    // Composants UI
    private JLabel lblTitle;
    private JPanel pnlProduct;      // Contiendra l'image et la description
    private JButton btnAddToCart;   // Bouton "Ajouter au Panier"
    private JPanel pnlReviews;      // Panneau d'affichage des avis
    private JButton btnLeaveReview; // Bouton "Laisser un avis"

    // Constantes de dimensionnement (pour les images et les panneaux)
    private static final int IMG_TARGET_WIDTH = 600;
    private static final int IMG_TARGET_HEIGHT = 400;
    private static final int DESCRIPTION_PANEL_WIDTH = 600;
    private static final int DESCRIPTION_PANEL_HEIGHT = 300;

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;
        this.commandService = new CommandService();
        this.reviewService = new ReviewService();

        initComponents();

        // Création d'un conteneur vertical pour regrouper le produit et les avis
        JPanel pnlDetails = new JPanel();
        pnlDetails.setLayout(new BoxLayout(pnlDetails, BoxLayout.Y_AXIS));
        pnlDetails.setBackground(Color.WHITE);
        pnlDetails.add(pnlProduct);
        pnlDetails.add(Box.createVerticalStrut(20));
        pnlDetails.add(new JScrollPane(pnlReviews));
        pnlDetails.add(Box.createVerticalStrut(10));
        pnlDetails.add(btnLeaveReview);

        pnlRoot.setLayout(new BorderLayout(20, 20));
        pnlRoot.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlRoot.setBackground(new Color(240, 248, 255));

        pnlRoot.add(lblTitle, BorderLayout.NORTH);
        pnlRoot.add(pnlDetails, BorderLayout.CENTER);
        pnlRoot.add(btnAddToCart, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() throws IOException {
        // Titre du produit
        lblTitle = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Panneau produit : image et description
        pnlProduct = new JPanel(new BorderLayout(20, 20));
        pnlProduct.setBackground(Color.WHITE);
        pnlProduct.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Panneau d'image (à gauche) utilisant ImageUtils pour redimensionner
        JPanel pnlImage = new JPanel(new BorderLayout());
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel imageLabel = createImageLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pnlImage.add(imageLabel, BorderLayout.CENTER);

        // Zone de description (à droite) avec affichage HTML pour garder une mise en forme flexible
        JLabel lblDescription = new JLabel("<html>" + vehicle.toString().replaceAll("\n", "<br>") + "</html>");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescription.setForeground(new Color(66, 66, 66));
        JScrollPane descriptionScroll = new JScrollPane(lblDescription);
        descriptionScroll.setPreferredSize(new Dimension(DESCRIPTION_PANEL_WIDTH, DESCRIPTION_PANEL_HEIGHT));
        descriptionScroll.setBorder(BorderFactory.createEmptyBorder());

        pnlProduct.add(pnlImage, BorderLayout.WEST);
        pnlProduct.add(descriptionScroll, BorderLayout.CENTER);

        // Bouton "Ajouter au Panier"
        btnAddToCart = new JButton("🛒 Ajouter au Panier");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAddToCart.setBackground(new Color(76, 175, 80));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btnAddToCart.addActionListener(e -> {
            try {
                addToCart(vehicle);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "Véhicule ajouté au panier !", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
        });

        // Panneau des avis : alimenté par vehicle.getReviews()
        pnlReviews = createReviewsPanel();

        // Bouton "Laisser un avis"
        btnLeaveReview = new JButton("Laisser un avis");
        btnLeaveReview.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLeaveReview.setBackground(new Color(0, 123, 255));
        btnLeaveReview.setForeground(Color.WHITE);
        btnLeaveReview.setFocusPainted(false);
        btnLeaveReview.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLeaveReview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLeaveReview.addActionListener(e -> {
            ReviewDialog reviewDialog = new ReviewDialog(this, getClient(), vehicle);
            reviewDialog.setVisible(true);
            // Après avoir soumis un avis, on ajoute le nouvel avis à la collection via refreshReviews()
            refreshReviews();
        });
    }

    /**
     * Crée un JLabel en utilisant ImageUtils pour charger et redimensionner l'image.
     * Ici, on demande à ImageUtils de retourner une image redimensionnée à la taille souhaitée,
     * tout en préservant le ratio (via crop si nécessaire).
     */
    private JLabel createImageLabel() {
        int targetWidth = IMG_TARGET_WIDTH;
        int targetHeight = IMG_TARGET_HEIGHT;
        ImageIcon icon = ImageUtils.loadAndResizeImage(vehicle.getImageUrl(), targetWidth, targetHeight);
        if (icon == null) {
            icon = createPlaceholderIcon(targetWidth, targetHeight);
        }
        return new JLabel(icon, SwingConstants.CENTER);
    }

    /**
     * Crée un placeholder ImageIcon si l'image n'est pas trouvée.
     */
    private ImageIcon createPlaceholderIcon(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2d.drawString("No Image", width / 2 - 30, height / 2);
        g2d.dispose();
        return new ImageIcon(placeholder);
    }

    /**
     * Ajoute le véhicule au panier en utilisant la logique existante.
     */
    private void addToCart(VehicleEntity vehicle) throws IOException {
        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());
        EntityManager entityManager = commandService.getEntityManager();
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        if (getClient().getPanier().getVehicles().isEmpty()) {
            activeCommand = new CommandEntity();
            activeCommand.setCommandDate(new Date(System.currentTimeMillis()));
            activeCommand.setClient(getClient());
            activeCommand.setCommandStatus("En attente");
            commandService.createCommand(activeCommand);
        } else if (activeCommand == null) {
            try {
                activeCommand = commandService.getLastPendingCommand(getClient());
                System.out.println("ID de la commande utilisée : " + activeCommand.getIdCommand());
            } catch (Exception e) {
                e.printStackTrace();
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            }
        }
        boolean alreadyExists = activeCommand.getVehicles().stream()
                .anyMatch(v -> v.getIdVehicle() == vehicle.getIdVehicle());
        if (!alreadyExists) {
            var commandLine = new CommandLineEntity();
            var commandLinePK = new CommandLineEntityPK();
            commandLinePK.setIdCommand(activeCommand.getIdCommand());
            commandLinePK.setIdVehicle(vehicle.getIdVehicle());
            commandLine.setId(commandLinePK);
            commandLine.setCommand(activeCommand);
            commandLine.setVehicle(vehicle);
            entityManager.persist(commandLine);
            getClient().addToPanier(vehicle);
            System.out.println(getClient().getPanier().getVehicles());
        } else {
            JOptionPane.showMessageDialog(this, "Ce véhicule est déjà dans votre panier.", "Déjà ajouté", JOptionPane.WARNING_MESSAGE);
        }
        entityManager.getTransaction().commit();
        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());
        dispose();
        new CatalogForm(getClient());
    }

    /**
     * Crée et retourne un panneau affichant les avis du véhicule.
     * Utilise vehicle.getReviews() pour récupérer la liste des avis.
     */
    private JPanel createReviewsPanel() {
        JPanel pnlReviewsContainer = new JPanel();
        pnlReviewsContainer.setLayout(new BoxLayout(pnlReviewsContainer, BoxLayout.Y_AXIS));
        pnlReviewsContainer.setBackground(Color.WHITE);
        pnlReviewsContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Avis des Clients",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.DARK_GRAY));

        List<ReviewEntity> reviews = vehicle.getReviews();
        if (reviews == null || reviews.isEmpty()) {
            JLabel lblNoReviews = new JLabel("Aucun avis pour le moment.", SwingConstants.CENTER);
            lblNoReviews.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblNoReviews.setForeground(Color.GRAY);
            pnlReviewsContainer.add(lblNoReviews);
        } else {
            for (ReviewEntity review : reviews) {
                JPanel reviewPanel = new JPanel(new BorderLayout());
                reviewPanel.setBackground(Color.WHITE);
                reviewPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

                StringBuilder stars = new StringBuilder();
                for (int i = 0; i < review.getRating(); i++) {
                    stars.append("★");
                }
                for (int i = review.getRating(); i < 5; i++) {
                    stars.append("☆");
                }
                JLabel lblRating = new JLabel(stars.toString() + " - " + review.getClient().getName());
                lblRating.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                lblRating.setForeground(new Color(255, 153, 0));

                JLabel lblComment = new JLabel("<html><i>" + review.getComment() + "</i><br/><small>" + review.getReviewDate() + "</small></html>");
                lblComment.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lblComment.setForeground(new Color(66, 66, 66));

                reviewPanel.add(lblRating, BorderLayout.NORTH);
                reviewPanel.add(lblComment, BorderLayout.CENTER);

                pnlReviewsContainer.add(reviewPanel);
                pnlReviewsContainer.add(Box.createVerticalStrut(10));
            }
        }
        return pnlReviewsContainer;
    }

    /**
     * Rafraîchit le panneau des avis en rechargeant la collection via vehicle.getReviews().
     */
    private void refreshReviews() {
        pnlReviews.removeAll();
        pnlReviews.add(createReviewsPanel());
        pnlReviews.revalidate();
        pnlReviews.repaint();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }
}
