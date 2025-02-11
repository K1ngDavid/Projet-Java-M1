package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;

import tools.AdvancedSearchBar;
import tools.ImageUtils;
import tools.AddVehicleDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class CatalogForm extends AbstractFrame {
    private JPanel pnlCatalog;
    private VehicleService vehicleService;
    private JPanel vehiclePanel;
    private JScrollPane scrollPane;

    public CatalogForm(ClientEntity client) {
        super(client);
        this.vehicleService = new VehicleService();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        pnlCatalog = new JPanel(new BorderLayout());

        // Récupération de tous les véhicules
        List<VehicleEntity> vehicles = vehicleService.getUniqueVehicles();

        // Utilisation de la barre de recherche avancée
        AdvancedSearchBar searchBar = new AdvancedSearchBar(vehicles, this::displayVehicles);
        pnlCatalog.add(searchBar, BorderLayout.NORTH);

        // Si l'utilisateur est admin, on ajoute un bouton d'ajout en bas du catalogue
        if(getClient().getRole() == ClientEntity.Role.ADMIN) {
            JPanel adminActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            adminActionsPanel.setBackground(new Color(240, 248, 255));
            JButton btnAddVehicle = new JButton("Ajouter un véhicule");
            btnAddVehicle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnAddVehicle.setBackground(new Color(40, 167, 69));
            btnAddVehicle.setForeground(Color.WHITE);
            btnAddVehicle.setFocusPainted(false);
            btnAddVehicle.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAddVehicle.addActionListener(e -> openAddVehicleDialog());
            adminActionsPanel.add(btnAddVehicle);
            pnlCatalog.add(adminActionsPanel, BorderLayout.SOUTH);
        }

        initCatalogComponents();

        pnlCatalog.setBackground(new Color(240, 248, 255)); // AliceBlue
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlCatalog, BorderLayout.CENTER);

        this.repaint();
        this.pack();
        this.revalidate();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
     * Pour un client, la carte affiche un bouton "Voir".
     * Pour un admin, la carte affiche en plus les boutons "Modifier" et "Supprimer" pour le CRUD.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Augmentez la hauteur pour laisser plus d'espace pour le contenu (par exemple 350)
        card.setPreferredSize(new Dimension(220, 350));

        // Image du véhicule
        JLabel imageLabel = new JLabel(ImageUtils.loadAndResizeImage(vehicle.getImageUrl(), 600, 200));
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

        card.add(imageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Différencier l'affichage selon le rôle de l'utilisateur
        if (getClient().getRole() == ClientEntity.Role.ADMIN) {
            // Pour l'admin : boutons "Modifier" et "Supprimer"
            JPanel adminButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            adminButtonPanel.setBackground(Color.WHITE);

            JButton btnModifier = new JButton("Modifier");
            btnModifier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnModifier.setFocusPainted(false);
            btnModifier.setBackground(new Color(23, 162, 184));
            btnModifier.setForeground(Color.WHITE);
            btnModifier.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnModifier.addActionListener(e -> openEditVehicleDialog(vehicle));

            JButton btnSupprimer = new JButton("Supprimer");
            btnSupprimer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnSupprimer.setFocusPainted(false);
            btnSupprimer.setBackground(new Color(220, 53, 69));
            btnSupprimer.setForeground(Color.WHITE);
            btnSupprimer.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSupprimer.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Confirmez-vous la suppression de ce véhicule ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = vehicleService.deleteVehicle(vehicle.getIdVehicle());
                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "Véhicule supprimé avec succès.",
                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                        displayVehicles(vehicleService.getUniqueVehicles());
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du véhicule.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            adminButtonPanel.add(btnModifier);
            adminButtonPanel.add(btnSupprimer);
            card.add(adminButtonPanel);
        } else {
            // Pour un client : bouton "Voir"
            JButton btnVoirProduit = new JButton("👁 Voir");
            btnVoirProduit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnVoirProduit.setFocusPainted(false);
            btnVoirProduit.setBackground(new Color(0, 123, 255));
            btnVoirProduit.setForeground(Color.WHITE);
            btnVoirProduit.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnVoirProduit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnVoirProduit.addActionListener(e -> voirProduit(vehicle));
            card.add(btnVoirProduit);
        }

        return card;
    }

    /**
     * 🔥 Affiche les détails du produit pour un client.
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
     * 🔥 Ouvre le formulaire/dialoque pour ajouter un nouveau véhicule.
     * (À remplacer par votre propre implémentation)
     */
    private void openAddVehicleDialog() {
        AddVehicleDialog dialog = new AddVehicleDialog(this);
        dialog.setVisible(true);
        // Après fermeture du dialogue, rafraîchir l'affichage des véhicules
        displayVehicles(vehicleService.getUniqueVehicles());
    }

    /**
     * 🔥 Ouvre le formulaire/dialoque pour modifier les informations d'un véhicule.
     * (À remplacer par votre propre implémentation)
     */
    private void openEditVehicleDialog(VehicleEntity vehicle) {
        try {
            new ProductForm(getClient(),vehicle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dispose();
        // Après modification, rafraîchir l'affichage
        displayVehicles(vehicleService.getUniqueVehicles());
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Action de retour ou autre selon vos besoins pour le compte
    }
}

