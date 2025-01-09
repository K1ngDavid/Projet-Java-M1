package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ProductForm extends AbstractFrame {
    private JPanel pnlProduct; // Contient image + description
    private JLabel lblTitre; // Titre du produit
    private JPanel pnlImage; // Panel pour l'image
    private JPanel pnlDescription; // Panel pour la description
    private JButton btnAjouterPanier; // Bouton Ajouter au panier
    private VehicleService vehicleService;
    private VehicleEntity vehicle;

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;
        System.out.println(vehicle);
        initComponents();

        // Ajout des composants principaux au panneau racine
        pnlRoot.setLayout(new BorderLayout(10, 10)); // Espacement entre les sections
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Marges extérieures

        pnlRoot.add(lblTitre, BorderLayout.NORTH); // Titre en haut
        pnlRoot.add(pnlProduct, BorderLayout.CENTER); // Contenu principal au centre
        pnlRoot.add(btnAjouterPanier, BorderLayout.SOUTH); // Bouton en bas

        this.pack();
        this.setLocationRelativeTo(null); // Centrer la fenêtre
    }

    private void initComponents() throws IOException {
        // Initialisation du titre
        lblTitre = new JLabel(vehicle.getModel().getBrandName(), SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20)); // Style de la police
        lblTitre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Marges autour du titre

        // Panel pour l'image
        pnlImage = new JPanel(new BorderLayout());
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/" + vehicle.getImageUrl()))));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrer l'image
        pnlImage.add(imageLabel, BorderLayout.CENTER);

        // Panel pour la description
        pnlDescription = new JPanel(new BorderLayout());
        pnlDescription.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes
        JLabel lblPrice = new JLabel("Price: " + vehicle.getPrice() + " €");
        JLabel lblType = new JLabel("Type: " + vehicle.getClass().getSimpleName());
        JLabel lblDescription = new JLabel("<html>Description: " + vehicle + "</html>"); // Permet d'afficher un texte multi-ligne
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        lblType.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescription.setFont(new Font("Arial", Font.PLAIN, 14));

        // Ajout des descriptions
        pnlDescription.add(lblPrice, BorderLayout.NORTH); // Prix en haut
        pnlDescription.add(lblType, BorderLayout.CENTER); // Type au centre
        pnlDescription.add(lblDescription, BorderLayout.SOUTH); // Description en bas

        // Panel combinant l'image et la description
        pnlProduct = new JPanel(new BorderLayout(10, 10)); // Espacement entre l'image et la description
        pnlProduct.add(pnlImage, BorderLayout.WEST); // Image à gauche
        pnlProduct.add(pnlDescription, BorderLayout.CENTER); // Description à droite

        // Bouton Ajouter au panier
        btnAjouterPanier = new JButton("Add to Cart");
        btnAjouterPanier.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAjouterPanier.addActionListener(e -> {
            // Action pour ajouter au panier
            this.getClient().addToPanier(vehicle);
            System.out.println(vehicle.getModel().getBrandName() + " added to cart!");
            dispose();
            try {
                CatalogForm catalogForm = new CatalogForm(this.getClient());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Gérer l'action du compte
    }

    @Override
    void homeActionPerformed(ActionEvent evt) {
        // Gérer l'action de retour à la maison
    }
}
