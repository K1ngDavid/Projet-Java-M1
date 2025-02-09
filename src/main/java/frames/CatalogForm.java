package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;
import tools.AdvancedSearchBar;

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

        // ✅ Récupération de tous les véhicules
        List<VehicleEntity> vehicles = vehicleService.getUniqueVehicles();

        // ✅ Utilisation de la SearchBar avec les véhicules
        AdvancedSearchBar searchBar = new AdvancedSearchBar(vehicles, this::displayVehicles);
        pnlCatalog.add(searchBar, BorderLayout.NORTH);

        initCatalogComponents();

        pnlCatalog.setBackground(new Color(240, 248, 255));
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
        vehiclePanel = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollPane = new JScrollPane(vehiclePanel);
        pnlCatalog.add(scrollPane, BorderLayout.CENTER);

        // ✅ Chargement initial des véhicules
        displayVehicles(vehicleService.getUniqueVehicles());
    }

    /**
     * 🔥 Met à jour l'affichage des véhicules après la recherche.
     */
    private void displayVehicles(List<VehicleEntity> vehicles) {
        vehiclePanel.removeAll();

        if (vehicles.isEmpty()) {
            vehiclePanel.add(new JLabel("🚗 Aucun véhicule trouvé", SwingConstants.CENTER));
        } else {
            for (VehicleEntity vehicle : vehicles) {
                vehiclePanel.add(createVehicleCard(vehicle));
            }
        }

        vehiclePanel.revalidate();
        vehiclePanel.repaint();
    }

    /**
     * 🔥 Crée une carte d'affichage pour un véhicule.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        System.out.println(vehicle.getImageUrl());
        JLabel imageLabel = new JLabel(new ImageIcon(loadImage(vehicle.getImageUrl())));
        JLabel nameLabel = new JLabel(vehicle.getModel().getModelName(), SwingConstants.CENTER);
        JLabel priceLabel = new JLabel(vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
        JButton btnVoirProduit = new JButton("👁 Voir");

        btnVoirProduit.addActionListener(e -> voirProduit(vehicle));

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(priceLabel, BorderLayout.SOUTH);
        card.add(btnVoirProduit, BorderLayout.PAGE_END);

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
     * 🖼 Charge une image et la redimensionne.
     */
    private Image loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResource(path)).getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {}
}
