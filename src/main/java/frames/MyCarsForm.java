package frames;

import entity.ClientEntity;
import entity.VehicleEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class MyCarsForm extends AbstractFrame {
    private ClientEntity client;
    private JPanel pnlCars;

    public MyCarsForm(ClientEntity client) {
        super(client);

        setTitle("🚗 Mes Voitures");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(new JScrollPane(pnlCars));
        JLabel labelCenter = new JLabel("Mes voitures");
        labelCenter.setHorizontalAlignment(SwingConstants.CENTER);
        pnlRoot.add(labelCenter,BorderLayout.NORTH);

        this.pack();
        this.setLocationRelativeTo(null); // Centrer la fenêtre
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {

    }

    /**
     * 🔥 Initialise l'interface utilisateur
     */
    private void initUI() {
        pnlCars = new JPanel();
        pnlCars.setLayout(new GridLayout(0, 2, 10, 10)); // 🔥 Grille avec 2 colonnes

        JScrollPane scrollPane = new JScrollPane(pnlCars);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ✅ Charger et afficher les voitures du client
        displayCars();

        // 🔥 Ajouter le panneau principal au JFrame
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 🔥 Charge et affiche les voitures du client.
     */
    private void displayCars() {
        pnlCars.removeAll(); // 🔥 Nettoyage avant affichage

        List<VehicleEntity> vehicles = getClient().getVehicles();

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
        card.setPreferredSize(new Dimension(250, 200)); // 🔥 Taille augmentée pour inclure le bouton

        // ✅ Image de la voiture
        JLabel imageLabel;
        try {
            imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png"))));
        } catch (IOException | NullPointerException e) {
            imageLabel = new JLabel("🚗", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        }

        // ✅ Infos de la voiture
        JLabel nameLabel = new JLabel("🚘 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0, 128, 0));

        // ✅ Bouton "Voir"
        JButton btnVoir = new JButton("Voir");
        btnVoir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVoir.setBackground(new Color(76, 175, 80)); // ✅ Vert
        btnVoir.setForeground(Color.WHITE);
        btnVoir.setFocusPainted(false);
        btnVoir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🎯 Ajout d'un listener au bouton
        btnVoir.addActionListener(e -> {
            try {
                new ProductForm(getClient(),vehicle).setVisible(true);
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
        card.add(pnlBottom, BorderLayout.SOUTH); // ✅ Placement propre du bas

        return card;
    }


    private void showVehicleDetails(VehicleEntity vehicle) {
        JDialog dialog = new JDialog(this, "Détails du Véhicule", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("🚘 Modèle : " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName()));
        panel.add(new JLabel("💰 Prix : " + vehicle.getPrice() + " €"));
        panel.add(new JLabel("🛠️ Type : " + vehicle.getVehicleType()));
        panel.add(new JLabel("📅 Année : " + vehicle.getVehiclePowerSource().toString()));

        JButton btnClose = new JButton("Fermer");
        btnClose.addActionListener(e -> dialog.dispose());
        panel.add(Box.createVerticalStrut(10)); // ✅ Espacement
        panel.add(btnClose);

        dialog.add(panel);
        dialog.setVisible(true);
    }

}
