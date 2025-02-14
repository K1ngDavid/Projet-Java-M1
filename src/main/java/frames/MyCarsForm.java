package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.ClientService;
import service.VehicleService;
import tools.AdvancedSearchBar;
import tools.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MyCarsForm extends AbstractFrame {
    private JPanel pnlCars;
    private VehicleService vehicleService;

    private ClientService clientService;

    public MyCarsForm(ClientEntity client) {
        super(client);
        this.vehicleService = new VehicleService();
        this.clientService = new ClientService();
        setTitle("🚗 Mes Voitures");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pnlRoot.setLayout(new BorderLayout(15, 15));
        pnlRoot.setBackground(new Color(245, 245, 245));

        initUI();

        this.pack();
        this.setLocationRelativeTo(null); // Centrer la fenêtre
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Action de retour ou autre selon vos besoins
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initUI() {
        // Création d'un panel pour la SearchBar
        AdvancedSearchBar searchBar = new AdvancedSearchBar(getClient().getVehicles(), this::displayCars);
        pnlRoot.add(searchBar, BorderLayout.NORTH);

        // Panel conteneur pour les cartes de voitures.
        // Utilisation d'un GridLayout pour disposer les cartes dans une grille à 3 colonnes.
        pnlCars = new JPanel(new GridLayout(0, 3, 15, 15));
        pnlCars.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(pnlCars);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnlRoot.add(scrollPane, BorderLayout.CENTER);

        // Affichage initial des véhicules
        displayCars(clientService.getPaidVehiclesForClient(getClient().getIdClient()));
    }

    /**
     * Met à jour l'affichage des voitures après une recherche.
     */
    private void displayCars(List<VehicleEntity> vehicles) {
        pnlCars.removeAll();

        if (vehicles == null || vehicles.isEmpty()) {
            JLabel noCarsLabel = new JLabel("❌ Aucune voiture disponible", SwingConstants.CENTER);
            noCarsLabel.setForeground(Color.RED);
            noCarsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            pnlCars.add(noCarsLabel);
        } else {
            for (VehicleEntity vehicle : vehicles) {
                pnlCars.add(createVehicleCard(vehicle));
            }
        }

        pnlCars.revalidate();
        pnlCars.repaint();
    }

    /**
     * Crée une carte d'affichage pour une voiture.
     * Pour un client, la carte affiche un bouton "Voir" qui ouvre la fiche produit.
     * Pour un admin, la carte affiche des boutons "Modifier" et "Supprimer" pour gérer le véhicule.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Dimension fixe pour la carte (vous pouvez adapter ou utiliser un layout plus flexible)
        card.setPreferredSize(new Dimension(250, 220));

        // En-tête : le nom du véhicule
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(nameLabel, BorderLayout.NORTH);

        // Zone centrale : l'image du véhicule
        // On utilise ImageUtils pour charger l'image redimensionnée en conservant le ratio.
        // Ici, nous définissons des dimensions cibles (par exemple 230x120) adaptées à la carte.
        ImageIcon vehicleIcon = ImageUtils.loadAndResizeImage(vehicle.getImageUrl(), 230, 120);
        JLabel imageLabel = new JLabel(vehicleIcon, SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Pied de carte : affichage du prix et des boutons d'action
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0, 128, 0));

        // Panneau pour les boutons (différents selon le rôle)
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlActions.setBackground(Color.WHITE);

        if (getClient().getRole() == ClientEntity.Role.ADMIN) {
            // Pour un admin : boutons Modifier et Supprimer
            JButton btnEdit = new JButton("Modifier");
            styleButton(btnEdit, new Color(23, 162, 184));
            btnEdit.addActionListener(e -> {
                // Ici, vous pouvez ouvrir un formulaire d'édition, par exemple EditVehicleForm
                JOptionPane.showMessageDialog(this, "Modifier le véhicule (ID: " + vehicle.getIdVehicle() + ")",
                        "Modification", JOptionPane.INFORMATION_MESSAGE);
            });

            JButton btnDelete = new JButton("Supprimer");
            styleButton(btnDelete, new Color(220, 53, 69));
            btnDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Confirmez-vous la suppression de ce véhicule ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = vehicleService.deleteVehicle(vehicle.getIdVehicle());
                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "Véhicule supprimé avec succès.",
                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                        // Actualisez l'affichage après suppression
                        displayCars(vehicleService.getAllVehicles());
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du véhicule.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            pnlActions.add(btnEdit);
            pnlActions.add(btnDelete);
        } else {
            // Pour un client : bouton "Voir"
            JButton btnView = new JButton("Voir");
            styleButton(btnView, new Color(76, 175, 80));
            btnView.addActionListener(e -> {
                try {
                    new ProductForm(getClient(), vehicle).setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            pnlActions.add(btnView);
        }

        // Regrouper le prix et les actions dans un même panneau de bas de carte
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(priceLabel, BorderLayout.NORTH);
        pnlBottom.add(pnlActions, BorderLayout.SOUTH);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Applique un style uniforme aux boutons.
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
