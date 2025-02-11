package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.CommandLineEntityPK;
import entity.ReviewEntity;
import entity.VehicleEntity;
import enumerations.PowerSource;
import enumerations.TransmissionType;
import jakarta.persistence.EntityManager;
import service.CommandService;
import service.ReviewService;
import service.VehicleService;
import tools.ImageUtils;
import tools.ReviewDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ProductForm extends AbstractFrame {
    // Entités et services
    private VehicleEntity vehicle;
    private final CommandService commandService;
    private final ReviewService reviewService;
    private CommandEntity activeCommand;
    private final VehicleService vehicleService;

    // Composants communs
    private JLabel lblTitle;
    private JPanel pnlProduct; // Contient l'image à gauche et le panneau de détails à droite

    // Composants mode client
    private JButton btnAddToCart;
    private JPanel pnlReviews;
    private JButton btnLeaveReview;

    // Composants mode admin (pour l'édition)
    private JTextField txtModelName;
    private JTextField txtPrice;
    private JTextField txtImageUrl;
    // Nouveaux champs à modifier
    private JTextField txtStatus;
    private JTextField txtCountryOfOrigin;
    private JComboBox<PowerSource> cbPowerSource;
    private JComboBox<TransmissionType> cbTransmissionType;
    private JTextField txtNumberOfDoors;
    private JTextField txtHorsePower;
    // Le champ de description n'est pas modifié directement (il est remplacé par le panneau des avis)
    private JButton btnSaveChanges;
    private JButton btnCancel;

    // Pour la gestion des avis en mode admin, on stocke la référence au panneau des avis
    private JComponent adminReviewsPanel;

    // Dimensions
    private static final int IMG_TARGET_WIDTH = 600;
    private static final int IMG_TARGET_HEIGHT = 400;
    private static final int DESCRIPTION_PANEL_WIDTH = 600;
    private static final int DESCRIPTION_PANEL_HEIGHT = 300;

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;
        this.vehicleService = new VehicleService();
        this.commandService = new CommandService();
        this.reviewService = new ReviewService();

        initComponents();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Initialise l'interface.
     * La structure est la suivante :
     * - Un titre en haut
     * - Un panneau central composé d'un panneau produit contenant :
     *   • À gauche : l'image du véhicule
     *   • À droite : un panneau de détails qui affiche soit la description (client),
     *     soit un formulaire éditable (admin) incluant les champs à modifier et, à la place
     *     d'un champ de description, un panneau listant les avis avec possibilité de suppression.
     * - En dessous du panneau produit, pour le client, on affiche le panneau des avis,
     *   tandis que pour l'admin, on affiche un panneau de boutons « Annuler » / « Enregistrer ».
     */
    private void initComponents() throws IOException {
        // Titre
        lblTitle = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));

        pnlRoot.setLayout(new BorderLayout(20, 20));
        pnlRoot.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlRoot.setBackground(new Color(240, 248, 255));
        pnlRoot.add(lblTitle, BorderLayout.NORTH);

        // Panneau produit : image à gauche et panneau de détails (description ou formulaire) à droite
        pnlProduct = new JPanel(new BorderLayout(20, 20));
        pnlProduct.setBackground(Color.WHITE);
        pnlProduct.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Panneau de l'image
        JPanel pnlImage = new JPanel(new BorderLayout());
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel imageLabel = createImageLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pnlImage.add(imageLabel, BorderLayout.CENTER);
        pnlProduct.add(pnlImage, BorderLayout.WEST);

        // Panneau de détails (à droite de l'image)
        JComponent detailsPanel = createDetailsPanel();
        pnlProduct.add(detailsPanel, BorderLayout.CENTER);

        // Création d'un conteneur vertical pour regrouper le panneau produit et, pour le client, la section avis
        JPanel pnlDetails = new JPanel();
        pnlDetails.setLayout(new BoxLayout(pnlDetails, BoxLayout.Y_AXIS));
        pnlDetails.setBackground(Color.WHITE);
        pnlDetails.add(pnlProduct);

        if (getClient().getRole() != ClientEntity.Role.ADMIN) {
            // Pour le client, on ajoute la section avis en dessous du panneau produit
            pnlReviews = createReviewsPanel();
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
                refreshReviews();
            });
            pnlDetails.add(Box.createVerticalStrut(20));
            pnlDetails.add(new JScrollPane(pnlReviews));
            pnlDetails.add(Box.createVerticalStrut(10));
            pnlDetails.add(btnLeaveReview);
        }

        pnlRoot.add(pnlDetails, BorderLayout.CENTER);

        // Panneau du bas : selon le rôle, on affiche les boutons d'action
        if (getClient().getRole() == ClientEntity.Role.ADMIN) {
            // Pour l'admin, un panneau avec les boutons "Annuler" et "Enregistrer"
            JPanel pnlAdminButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pnlAdminButtons.setBackground(new Color(240, 248, 255));

            btnCancel = new JButton("Annuler");
            btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnCancel.setBackground(new Color(220, 53, 69));
            btnCancel.setForeground(Color.WHITE);
            btnCancel.setFocusPainted(false);
            btnCancel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
            btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancel.addActionListener(e -> {
                dispose();
                new CatalogForm(getClient());
            });

            btnSaveChanges = new JButton("Enregistrer");
            btnSaveChanges.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnSaveChanges.setBackground(new Color(76, 175, 80));
            btnSaveChanges.setForeground(Color.WHITE);
            btnSaveChanges.setFocusPainted(false);
            btnSaveChanges.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
            btnSaveChanges.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSaveChanges.addActionListener(e -> saveChanges());

            pnlAdminButtons.add(btnCancel);
            pnlAdminButtons.add(btnSaveChanges);
            pnlRoot.add(pnlAdminButtons, BorderLayout.SOUTH);
        } else {
            // Pour le client, le bouton "Ajouter au Panier" en bas
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
            pnlRoot.add(btnAddToCart, BorderLayout.SOUTH);
        }
    }

    /**
     * Crée le panneau situé à droite de l'image.
     * Si l'utilisateur est un client, ce panneau affiche la description.
     * S'il est administrateur, il affiche un formulaire éditable pour modifier les champs
     * (Modèle, Prix, Image URL, Statut, Pays d'origine, Source d'énergie, Transmission, Nombre de portes, Puissance)
     * et, à la place d'un champ de description, un panneau avec la liste des avis (avec bouton de suppression pour chaque avis).
     */
    /**
     * Crée le panneau situé à droite de l'image.
     * Si l'utilisateur est un client, ce panneau affiche la description.
     * S'il est administrateur, il affiche un formulaire éditable pour modifier les champs
     * (Modèle, Prix, Image URL, Statut, Pays d'origine, Source d'énergie, Transmission,
     * Nombre de portes, Puissance) répartis sur deux colonnes, et en dessous un panneau
     * listant les avis avec possibilité de suppression.
     */
    private JComponent createDetailsPanel() {
        if (getClient().getRole() == ClientEntity.Role.ADMIN) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            // Création d'un panneau avec GridLayout pour répartir les champs sur 2 colonnes
            JPanel pnlFields = new JPanel(new GridLayout(0, 2, 10, 10));
            pnlFields.setBackground(Color.WHITE);

            // Colonne 1
            pnlFields.add(createFieldPanel("Modèle:",
                    txtModelName = new JTextField(vehicle.getModel().getModelName(), 20)));
            pnlFields.add(createFieldPanel("Prix:",
                    txtPrice = new JTextField(vehicle.getPrice().toString(), 10)));
            pnlFields.add(createFieldPanel("Image URL:",
                    txtImageUrl = new JTextField(vehicle.getImageUrl(), 20)));
            pnlFields.add(createFieldPanel("Statut:",
                    txtStatus = new JTextField(vehicle.getStatus(), 15)));
            pnlFields.add(createFieldPanel("Pays d'origine:",
                    txtCountryOfOrigin = new JTextField(vehicle.getCountryOfOrigin(), 15)));

            // Colonne 2
            pnlFields.add(createFieldPanel("Source d'énergie:",
                    cbPowerSource = new JComboBox<>(PowerSource.values())));
            cbPowerSource.setSelectedItem(vehicle.getVehiclePowerSource());
            pnlFields.add(createFieldPanel("Transmission:",
                    cbTransmissionType = new JComboBox<>(TransmissionType.values())));
            cbTransmissionType.setSelectedItem(vehicle.getTransmissionType());
            pnlFields.add(createFieldPanel("Nombre de portes:",
                    txtNumberOfDoors = new JTextField(
                            vehicle.getNumberOfDoors() != null ? vehicle.getNumberOfDoors().toString() : "", 5)));
            pnlFields.add(createFieldPanel("Puissance:",
                    txtHorsePower = new JTextField(
                            vehicle.getHorsePower() != null ? vehicle.getHorsePower().toString() : "", 5)));

            // Ajout du panneau des champs en haut du panel
            panel.add(pnlFields, BorderLayout.NORTH);

            // Ajout du panneau des avis admin en dessous
            adminReviewsPanel = createAdminReviewsPanel();
            panel.add(adminReviewsPanel, BorderLayout.SOUTH);

            return panel;
        } else {
            // Pour le client, affiche la description sous forme de label HTML dans un scroll pane
            JLabel lblDescription = new JLabel("<html>" + vehicle.toString().replaceAll("\n", "<br>") + "</html>");
            lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblDescription.setForeground(new Color(66, 66, 66));
            JScrollPane scroll = new JScrollPane(lblDescription);
            scroll.setPreferredSize(new Dimension(DESCRIPTION_PANEL_WIDTH, DESCRIPTION_PANEL_HEIGHT));
            scroll.setBorder(BorderFactory.createEmptyBorder());
            return scroll;
        }
    }

    /**
     * Méthode utilitaire pour créer un panneau contenant un label et un champ.
     *
     * @param labelText Le texte à afficher pour le label.
     * @param field     Le composant de saisie associé.
     * @return Un JPanel avec un FlowLayout aligné à gauche.
     */
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(field);
        return panel;
    }


    /**
     * Crée un panneau (contenu dans un JScrollPane) affichant la liste des avis
     * avec pour chacun un bouton "Supprimer" permettant à l'admin de supprimer l'avis.
     */
    private JComponent createAdminReviewsPanel() {
        JPanel pnlAdminReviews = new JPanel();
        pnlAdminReviews.setLayout(new BoxLayout(pnlAdminReviews, BoxLayout.Y_AXIS));
        pnlAdminReviews.setBackground(Color.WHITE);
        pnlAdminReviews.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Avis des Clients",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.DARK_GRAY));

        // Récupérer les reviews actualisées depuis la base de données
        List<ReviewEntity> reviews = reviewService.getReviewsByVehicle(vehicle);

        if (reviews == null || reviews.isEmpty()) {
            JLabel lblNoReviews = new JLabel("Aucun avis pour le moment.", SwingConstants.CENTER);
            lblNoReviews.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblNoReviews.setForeground(Color.GRAY);
            pnlAdminReviews.add(lblNoReviews);
        } else {
            for (ReviewEntity review : reviews) {
                JPanel reviewPanel = new JPanel(new BorderLayout());
                reviewPanel.setBackground(Color.WHITE);
                reviewPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

                // Construction de l'affichage de l'avis
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

                // Bouton de suppression de l'avis
                JButton btnDeleteReview = new JButton("Supprimer");
                btnDeleteReview.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                btnDeleteReview.setBackground(new Color(220, 53, 69));
                btnDeleteReview.setForeground(Color.WHITE);
                btnDeleteReview.setFocusPainted(false);
                btnDeleteReview.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnDeleteReview.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous supprimer cet avis ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = reviewService.deleteReview(review);
                        if (deleted) {
                            JOptionPane.showMessageDialog(this, "Avis supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            refreshAdminReviewsPanel();
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'avis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // Regrouper l'affichage et le bouton dans un conteneur
                JPanel reviewContentPanel = new JPanel(new BorderLayout());
                reviewContentPanel.setBackground(Color.WHITE);
                reviewContentPanel.add(lblRating, BorderLayout.NORTH);
                reviewContentPanel.add(lblComment, BorderLayout.CENTER);

                JPanel containerPanel = new JPanel(new BorderLayout());
                containerPanel.setBackground(Color.WHITE);
                containerPanel.add(reviewContentPanel, BorderLayout.CENTER);
                containerPanel.add(btnDeleteReview, BorderLayout.EAST);

                pnlAdminReviews.add(containerPanel);
                pnlAdminReviews.add(Box.createVerticalStrut(10));
            }
        }
        JScrollPane scrollPane = new JScrollPane(pnlAdminReviews);
        scrollPane.setPreferredSize(new Dimension(DESCRIPTION_PANEL_WIDTH, DESCRIPTION_PANEL_HEIGHT));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }


    /**
     * Méthode appelée pour rafraîchir le panneau des avis en mode admin.
     */
    private void refreshAdminReviewsPanel() {
        List<ReviewEntity> updatedReviews = reviewService.getReviewsByVehicle(vehicle);
        vehicle.setReviews(updatedReviews);
        Container parent = adminReviewsPanel.getParent();
        parent.remove(adminReviewsPanel);
        adminReviewsPanel = createAdminReviewsPanel();
        parent.add(adminReviewsPanel);
        parent.revalidate();
        parent.repaint();
    }



    /**
     * Charge et redimensionne l'image du véhicule.
     */
    private JLabel createImageLabel() {
        ImageIcon icon = ImageUtils.loadAndResizeImage(vehicle.getImageUrl(), IMG_TARGET_WIDTH, IMG_TARGET_HEIGHT);
        if (icon == null) {
            icon = createPlaceholderIcon(IMG_TARGET_WIDTH, IMG_TARGET_HEIGHT);
        }
        return new JLabel(icon, SwingConstants.CENTER);
    }

    /**
     * Crée une image de remplacement si l'image du véhicule n'est pas disponible.
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
     * Ajoute le véhicule au panier (code existant).
     */
    private void addToCart(VehicleEntity vehicle) throws IOException {
        System.out.println("PANIER : " + getClient().getPanier());
        // Obtenez un EntityManager (ici, on suppose que commandService fournit un nouvel EntityManager)
        EntityManager em = commandService.getEntityManager();
        try {
            em.getTransaction().begin();

            // Si le panier est nul ou s'il n'a aucun véhicule, créer une nouvelle commande active et la lier au client
            if (getClient().getPanier() == null || getClient().getPanier().getVehicles().isEmpty()) {
                CommandEntity newCommand = new CommandEntity();
                newCommand.setCommandDate(new Date(System.currentTimeMillis()));
                newCommand.setClient(getClient());
                newCommand.setCommandStatus("En attente");
                // Persister la nouvelle commande afin d'obtenir un ID généré
                em.persist(newCommand);
                em.flush();  // Forcer la génération de l'ID pour newCommand
                getClient().setPanier(newCommand);
            }

            // Vérifier si le véhicule existe déjà dans le panier
            boolean alreadyExists = getClient().getPanier().getVehicles().stream()
                    .anyMatch(v -> v.getIdVehicle() == vehicle.getIdVehicle());
            if (!alreadyExists) {
                // Créer et initialiser la clé composite
                CommandLineEntityPK pk = new CommandLineEntityPK();
                pk.setIdCommand(getClient().getPanier().getIdCommand());
                pk.setIdVehicle(vehicle.getIdVehicle());

                // Créer la ligne de commande et assigner la clé composite
                CommandLineEntity commandLine = new CommandLineEntity();
                commandLine.setId(pk);
                commandLine.setCommand(getClient().getPanier());
                commandLine.setVehicle(vehicle);

                // Persister la ligne de commande
                em.persist(commandLine);

                // Optionnel : mettre à jour la collection dans le panier
                getClient().getPanier().addCommandLine(commandLine);

                System.out.println("Panier mis à jour : " + getClient().getPanier().getVehicles());
            } else {
                JOptionPane.showMessageDialog(this, "Ce véhicule est déjà dans votre panier.", "Déjà ajouté", JOptionPane.WARNING_MESSAGE);
            }

            em.getTransaction().commit();
            dispose();
            new CatalogForm(getClient());
        } catch(Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }




    /**
     * Crée et retourne le panneau affichant les avis du véhicule pour le client.
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
     * Rafraîchit le panneau des avis pour le client.
     */
    private void refreshReviews() {
        pnlReviews.removeAll();
        pnlReviews.add(createReviewsPanel());
        pnlReviews.revalidate();
        pnlReviews.repaint();
    }

    /**
     * Sauvegarde les modifications apportées par l'administrateur.
     */
    private void saveChanges(){
        // Mise à jour du modèle
        vehicle.getModel().setModelName(txtModelName.getText());
        // Mise à jour des autres champs
        vehicle.setPrice(new BigDecimal(txtPrice.getText()));
        vehicle.setImageUrl(txtImageUrl.getText());
        vehicle.setStatus(txtStatus.getText());
        vehicle.setCountryOfOrigin(txtCountryOfOrigin.getText());
        vehicle.setVehiclePowerSource((PowerSource) cbPowerSource.getSelectedItem());
        vehicle.setTransmissionType((TransmissionType) cbTransmissionType.getSelectedItem());
        vehicle.setNumberOfDoors(Integer.parseInt(txtNumberOfDoors.getText()));
        vehicle.setHorsePower(Integer.parseInt(txtHorsePower.getText()));

        vehicleService.updateVehicle(vehicle);
        // Mise à jour en base via le service

        JOptionPane.showMessageDialog(this, "Véhicule modifié", "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }
}
