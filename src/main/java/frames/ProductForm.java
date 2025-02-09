package frames;

import entity.ClientEntity;
import entity.VehicleEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ProductForm extends AbstractFrame {
    private JPanel pnlProduct;
    private JLabel lblTitre;
    private JPanel pnlImage;
    private JPanel pnlDescription;
    private JButton btnAjouterPanier;
    private VehicleEntity vehicle;

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;

        initComponents();

        // 🔥 Ajout des composants au panneau principal
        pnlRoot.setLayout(new BorderLayout(10, 10));
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlRoot.setBackground(new Color(240, 248, 255)); // 💙 Fond bleu clair

        pnlRoot.add(lblTitre, BorderLayout.NORTH);
        pnlRoot.add(pnlProduct, BorderLayout.CENTER);
        pnlRoot.add(btnAjouterPanier, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() throws IOException {
        // ✅ Titre du produit
        lblTitre = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(new Color(50, 50, 50));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // ✅ Panel pour l'image
        pnlImage = new JPanel(new BorderLayout());
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlImage.setBackground(Color.WHITE);

        // 🔥 Ajout de l'image (avec gestion des erreurs)
        JLabel imageLabel = createImageLabel();
        pnlImage.add(imageLabel, BorderLayout.CENTER);

        // ✅ Panel pour la description
        pnlDescription = new JPanel();
        pnlDescription.setLayout(new BoxLayout(pnlDescription, BoxLayout.Y_AXIS));
        pnlDescription.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlDescription.setBackground(Color.WHITE);

        JLabel lblPrice = new JLabel("💰 Prix: " + vehicle.getPrice() + " €");
        JLabel lblType = new JLabel("🔧 Type: " + vehicle.getVehicleType());
        JLabel lblYear = new JLabel("📅 Année: " + vehicle.getVehiclePowerSource().toString());
        JLabel lblDescription = new JLabel("Description : " + vehicle);

        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(new Color(0, 128, 0));

        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescription.setFont(new Font("Segoe UI",Font.PLAIN,14));

        pnlDescription.add(lblPrice);
        pnlDescription.add(Box.createVerticalStrut(10)); // Espacement
        pnlDescription.add(lblType);
        pnlDescription.add(Box.createVerticalStrut(10));
        pnlDescription.add(lblYear);
        pnlDescription.add(Box.createVerticalStrut(10));
        pnlDescription.add(lblDescription);

        // ✅ Panel principal (image + description)
        pnlProduct = new JPanel(new BorderLayout(15, 15));
        pnlProduct.setBackground(Color.WHITE);
        pnlProduct.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        pnlProduct.add(pnlImage, BorderLayout.WEST);
        pnlProduct.add(pnlDescription, BorderLayout.CENTER);

        // ✅ Bouton Ajouter au Panier
        btnAjouterPanier = new JButton("🛒 Ajouter au Panier");
        btnAjouterPanier.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAjouterPanier.setBackground(new Color(76, 175, 80));
        btnAjouterPanier.setForeground(Color.WHITE);
        btnAjouterPanier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAjouterPanier.setFocusPainted(false);
        btnAjouterPanier.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ✅ Effet visuel au survol du bouton
        btnAjouterPanier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAjouterPanier.setBackground(new Color(60, 150, 70)); // Assombri au survol
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAjouterPanier.setBackground(new Color(76, 175, 80)); // Remet la couleur normale
            }
        });

        // ✅ Ajout au panier
        btnAjouterPanier.addActionListener(e -> addToCart());
    }

    /**
     * ✅ Ajoute la voiture au panier et ferme la fenêtre
     */
    private void addToCart() {
        getClient().addToPanier(vehicle);
        JOptionPane.showMessageDialog(this, "🚘 " + vehicle.getModel().getBrandName() + " ajouté au panier !");
        dispose();
        try {
            new CatalogForm(getClient());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * ✅ Charge l'image du véhicule
     */
    private JLabel createImageLabel() {
        try {
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png")));

            // 🔥 Redimensionner l'image à 150x150 px
            Image resizedImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(resizedImage));
        } catch (IOException | NullPointerException e) {
            // 🔥 Si l'image ne se charge pas, afficher un emoji
            JLabel fallbackLabel = new JLabel("🚗", SwingConstants.CENTER);
            fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
            return fallbackLabel;
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // 🎯 Gestion de l'action compte (si nécessaire)
    }
}
