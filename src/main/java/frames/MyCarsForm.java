package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;
import tools.AdvancedSearchBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MyCarsForm extends AbstractFrame {
    private JPanel pnlCars;
    private VehicleService vehicleService;

    public MyCarsForm(ClientEntity client) {
        super(client);
        this.vehicleService = new VehicleService();

        setTitle("🚗 Mes Voitures");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pnlRoot.setLayout(new BorderLayout(15, 15));
        pnlRoot.setBackground(new Color(245, 245, 245));

        initUI();

        this.pack();
        this.setLocationRelativeTo(null); // Centrer la fenêtre
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {}

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
        displayCars(getClient().getVehicles());
    }

    /**
     * Met à jour l'affichage des voitures après la recherche.
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
     * La carte comporte une image redimensionnée, le nom du véhicule, le prix et un bouton "Voir".
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Dimension fixe pour la carte
        card.setPreferredSize(new Dimension(250, 220));

        // Image redimensionnée
        JLabel imageLabel = createScaledImageLabel(vehicle.getImageUrl(), 200, 150);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Nom du véhicule
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Prix du véhicule
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0, 128, 0));

        // Bouton "Voir"
        JButton btnVoir = new JButton("Voir");
        styleButton(btnVoir, new Color(76, 175, 80));
        btnVoir.addActionListener(e -> {
            try {
                new ProductForm(getClient(), vehicle).setVisible(true);
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Panneau pour le bas de la carte : prix et bouton
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(priceLabel, BorderLayout.NORTH);
        pnlBottom.add(btnVoir, BorderLayout.SOUTH);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Crée et retourne un JLabel affichant l'image redimensionnée.
     *
     * @param imagePath Chemin absolu de l'image (ex : "/images/monImage.jpg")
     * @param width     largeur souhaitée
     * @param height    hauteur souhaitée
     */
    private JLabel createScaledImageLabel(String imagePath, int width, int height) {
        ImageIcon icon;
        try {
            URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            } else {
                throw new IOException("Image non trouvée : " + imagePath);
            }
        } catch (IOException | NullPointerException e) {
            // Création d'un placeholder
            BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2d.drawString("No Image", width / 2 - 30, height / 2);
            g2d.dispose();
            icon = new ImageIcon(placeholder);
        }
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
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
