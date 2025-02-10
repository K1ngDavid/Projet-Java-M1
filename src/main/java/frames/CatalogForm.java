package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;
import tools.AdvancedSearchBar;
import tools.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CatalogForm extends AbstractFrame {
    private JPanel pnlCatalog;
    private VehicleService vehicleService;
    private JPanel vehiclePanel;
    private JScrollPane scrollPane;

    public CatalogForm(ClientEntity client) {
        super(client);
        this.vehicleService = new VehicleService();

        pnlCatalog = new JPanel(new BorderLayout());

        // Récupération de tous les véhicules
        List<VehicleEntity> vehicles = vehicleService.getUniqueVehicles();

        // Utilisation de la barre de recherche avancée
        AdvancedSearchBar searchBar = new AdvancedSearchBar(vehicles, this::displayVehicles);
        pnlCatalog.add(searchBar, BorderLayout.NORTH);

        initCatalogComponents();

        pnlCatalog.setBackground(new Color(240, 248, 255)); // AliceBlue
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlCatalog, BorderLayout.CENTER);

        this.repaint();
        this.pack();
        this.revalidate();
    }

    /**
     * 🏷 Initialise l'affichage du catalogue.
     */
    private void initCatalogComponents() {
        // Utilisation d'un GridLayout pour afficher 3 cartes par ligne avec un espace de 10px
        vehiclePanel = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollPane = new JScrollPane(vehiclePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlCatalog.add(scrollPane, BorderLayout.CENTER);

        // Chargement initial des véhicules
        displayVehicles(vehicleService.getUniqueVehicles());
    }

    /**
     * 🔥 Met à jour l'affichage des véhicules après la recherche.
     */
    private void displayVehicles(List<VehicleEntity> vehicles) {
        vehiclePanel.removeAll();

        if (vehicles.isEmpty()) {
            JLabel noVehicles = new JLabel("🚗 Aucun véhicule trouvé", SwingConstants.CENTER);
            noVehicles.setFont(new Font("Segoe UI", Font.BOLD, 18));
            noVehicles.setForeground(new Color(150, 150, 150));
            vehiclePanel.setLayout(new BorderLayout());
            vehiclePanel.add(noVehicles, BorderLayout.CENTER);
        } else {
            vehiclePanel.setLayout(new GridLayout(0, 3, 10, 10));
            for (VehicleEntity vehicle : vehicles) {
                vehiclePanel.add(createVehicleCard(vehicle));
            }
        }

        vehiclePanel.revalidate();
        vehiclePanel.repaint();
    }

    /**
     * 🔥 Crée une carte d'affichage pour un véhicule avec une UX améliorée.
     * La carte a été agrandie et l'image redimensionnée pour laisser de la place au bouton.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Augmentez la hauteur pour laisser plus d'espace pour le contenu (par exemple 350 au lieu de 300)
        card.setPreferredSize(new Dimension(220, 350));

        // Image du véhicule : réduction de la hauteur pour laisser de la place aux autres composants
        JLabel imageLabel = new JLabel(loadImage(vehicle.getImageUrl(), 180, 120));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Nom du véhicule
        JLabel nameLabel = new JLabel(vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Prix du véhicule
        JLabel priceLabel = new JLabel(vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(0, 128, 0));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Bouton "Voir plus" avec style modernisé
        JButton btnVoirProduit = new JButton("👁 Voir");
        btnVoirProduit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVoirProduit.setFocusPainted(false);
        btnVoirProduit.setBackground(new Color(0, 123, 255));
        btnVoirProduit.setForeground(Color.WHITE);
        btnVoirProduit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoirProduit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoirProduit.addActionListener(e -> voirProduit(vehicle));

        // Assemblage de la carte avec des espaces fixes pour garantir la visibilité du bouton
        card.add(imageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnVoirProduit);

        return card;
    }

    /**
     * 🔥 Affiche les détails du produit.
     */
    private void voirProduit(VehicleEntity vehicle) {
        try {
            new ProductForm(getClient(), vehicle);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🖼 Charge une image et la redimensionne aux dimensions spécifiées.
     */
    private ImageIcon loadImage(String path, int width, int height) {
        // Utilisez la méthode utilitaire pour redimensionner l'image aux dimensions désirées.
        return ImageUtils.loadAndResizeImage(path, width, height);
    }

    // Ancienne méthode sans paramètres redéfinie pour compatibilité (optionnelle)
    private ImageIcon loadImage(String path) {
        return loadImage(path, 200, 200);
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {}
}
