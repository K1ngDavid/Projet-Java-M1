package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;
import tools.AdvancedSearchBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class MyCarsForm extends AbstractFrame {
    private JPanel pnlCars;
    private VehicleService vehicleService;

    public MyCarsForm(ClientEntity client) {
        super(client);
        this.vehicleService = new VehicleService();

        setTitle("🚗 Mes Voitures");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        pnlRoot.setLayout(new BorderLayout());
        initUI();


        // ✅ Ajout correct de la SearchBar AVANT pnlCars
        JLabel labelCenter = new JLabel("Mes voitures");
        labelCenter.setHorizontalAlignment(SwingConstants.CENTER);

        this.pack();
        this.setLocationRelativeTo(null); // Centrer la fenêtre
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {}

    /**
     * 🔥 Initialise l'interface utilisateur
     */
    private void initUI() {
        pnlCars = new JPanel();
        pnlCars.setLayout(new GridLayout(0, 2, 10, 10));

        JScrollPane scrollPane = new JScrollPane(pnlCars);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ✅ Récupération des véhicules du client via `VehicleService`
        List<VehicleEntity> vehicles = getClient().getVehicles();

        // ✅ Création et ajout de `AdvancedSearchBar`
        AdvancedSearchBar searchBar = new AdvancedSearchBar(vehicles, this::displayCars);
        pnlRoot.add(searchBar, BorderLayout.NORTH);

        // ✅ Affichage initial des véhicules
        displayCars(vehicles);

        // ✅ Ajout du scrollPane contenant pnlCars
        pnlRoot.add(scrollPane, BorderLayout.CENTER);

        // 🔥 S'assurer que l'interface est bien mise à jour
        pnlRoot.revalidate();
        pnlRoot.repaint();
    }

    /**
     * 🔥 Met à jour l'affichage des voitures après la recherche.
     */
    private void displayCars(List<VehicleEntity> vehicles) {
        pnlCars.removeAll();

        if (vehicles == null || vehicles.isEmpty()) {
            JLabel noCarsLabel = new JLabel("❌ Aucune voiture disponible", SwingConstants.CENTER);
            noCarsLabel.setForeground(Color.RED);
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
     * 🔥 Crée une carte d'affichage pour une voiture.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(250, 200));

        // ✅ Image de la voiture
        JLabel imageLabel;
        try {
            imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource(vehicle.getImageUrl()))));
        } catch (IOException | NullPointerException e) {
            imageLabel = new JLabel("🚗", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        }

        // ✅ Infos de la voiture
        JLabel nameLabel = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0, 128, 0));

        // ✅ Bouton "Voir"
        JButton btnVoir = new JButton("Voir");
        btnVoir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVoir.setBackground(new Color(76, 175, 80));
        btnVoir.setForeground(Color.WHITE);
        btnVoir.setFocusPainted(false);
        btnVoir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🎯 Ajout d'un listener au bouton
        btnVoir.addActionListener(e -> {
            try {
                new ProductForm(getClient(), vehicle).setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // ✅ Panneau pour organiser le bas de la carte
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.add(priceLabel, BorderLayout.NORTH);
        pnlBottom.add(btnVoir, BorderLayout.SOUTH);

        // ✅ Ajout des composants
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }
}
