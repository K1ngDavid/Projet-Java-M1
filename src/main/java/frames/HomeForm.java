package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.VehicleEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import service.CommandService;
import service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class HomeForm extends AbstractFrame {
    private CommandService commandService;
    private JPanel panelDepenses;
    private JPanel panelVoitures;
    private JPanel panelCommandes;
    private JPanel panelCategories; // Ce panel sera utilisé pour les recommandations
    private JComboBox<String> cbPeriodicity;

    // Nouvelle variable pour stocker les commandes chargées
    private List<CommandEntity> paidCommands;

    public HomeForm(ClientEntity client) {
        super(client);
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null !");
        }
        commandService = new CommandService();
        initComponents();

        // Lancer le chargement asynchrone des données
        new DataLoader().execute();

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Fond blanc et BorderLayout pour pnlRoot (hérité d'AbstractFrame)
        pnlRoot.setBackground(Color.WHITE);
        pnlRoot.setLayout(new BorderLayout());

        // ─── HEADER ──────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Image d'avatar
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/audi_rs6.jpg"));
        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(img));
        avatarLabel.setPreferredSize(new Dimension(60, 60));

        JLabel greetingLabel = new JLabel("Bonjour, " + getClient().getName() + " 👋", SwingConstants.LEFT);
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        greetingLabel.setForeground(Color.WHITE);
        greetingLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        headerPanel.add(avatarLabel, BorderLayout.WEST);
        headerPanel.add(greetingLabel, BorderLayout.CENTER);
        pnlRoot.add(headerPanel, BorderLayout.NORTH);

        // ─── CONTENU (TABLEAU DE BORD) ─────────────────────────────
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Couleur de fond douce pour chaque panneau
        Color panelBg = new Color(245, 245, 245);
        panelCategories = createPanel("✨ Recommandations pour vous", panelBg);
        panelDepenses = createPanel("📊 Mes dépenses", panelBg);
        panelVoitures = createScrollablePanel("🚗 Mes voitures", panelBg);
        panelCommandes = createScrollablePanel("📜 Mes commandes", panelBg);

        contentPanel.add(panelCategories);
        contentPanel.add(panelDepenses);
        contentPanel.add(panelVoitures);
        contentPanel.add(panelCommandes);
        pnlRoot.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * SwingWorker qui charge les commandes payées en arrière-plan
     * et met à jour l'interface une fois le chargement terminé.
     */
    private class DataLoader extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            // Charger les commandes payées (opération potentiellement lourde)
            paidCommands = commandService.getPaidCommands(getClient());
            return null;
        }
        @Override
        protected void done() {
            // Mettre à jour les panneaux avec les données chargées
            displayCommandes();
            displayVoitures();
            initDepensesPanel();
            displayRecommendations();
        }
    }

    /**
     * Crée un panneau générique avec fond coloré, bordures légères et marges.
     */
    private JPanel createPanel(String title, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(80, 80, 80));
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Crée un panneau scrollable pour afficher une liste d’éléments.
     */
    private JPanel createScrollablePanel(String title, Color bgColor) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(bgColor);
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(80, 80, 80));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        containerPanel.add(label, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }

    /**
     * Affiche les commandes du client dans le panneau des commandes.
     * Utilise la liste "paidCommands" chargée par DataLoader.
     */
    private void displayCommandes() {
        JPanel contentPanel = (JPanel) ((JScrollPane) panelCommandes.getComponent(1)).getViewport().getView();
        contentPanel.removeAll();
        if (paidCommands != null && !paidCommands.isEmpty()) {
            for (CommandEntity commande : paidCommands) {
                JPanel cmdCard = new JPanel(new BorderLayout());
                cmdCard.setBackground(Color.WHITE);
                cmdCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        new EmptyBorder(8, 8, 8, 8)
                ));
                JLabel cmdLabel = new JLabel("<html>Commande #" + commande.getIdCommand() + " 🗓 " +
                        commande.getCommandDate() + "<br>Statut: " + commande.getCommandStatus() +
                        "<br>💰 " + commande.getTotalAmount() + " €</html>", SwingConstants.CENTER);
                cmdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                cmdLabel.setForeground(new Color(70, 70, 70));
                cmdCard.add(cmdLabel, BorderLayout.CENTER);
                contentPanel.add(cmdCard);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            JLabel noCmdLabel = new JLabel("📜 Aucune commande disponible", SwingConstants.CENTER);
            noCmdLabel.setForeground(new Color(150, 150, 150));
            contentPanel.add(noCmdLabel);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Affiche les véhicules du client dans le panneau des voitures.
     */
    private void displayVoitures() {
        JPanel contentPanel = (JPanel) ((JScrollPane) panelVoitures.getComponent(1)).getViewport().getView();
        contentPanel.removeAll();
        List<VehicleEntity> voitures = getClient().getVehicles();
        if (voitures != null && !voitures.isEmpty()) {
            for (VehicleEntity voiture : voitures) {
                contentPanel.add(createVehicleCard(voiture));
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            JLabel noVehicleLabel = new JLabel("🚗 Aucune voiture disponible", SwingConstants.CENTER);
            noVehicleLabel.setForeground(new Color(150, 150, 150));
            contentPanel.add(noVehicleLabel);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Initialise le panneau des dépenses en affichant un graphique interactif.
     */
    private void initDepensesPanel() {
        panelDepenses.setLayout(new BorderLayout());
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTop.setBackground(new Color(245, 245, 245));
        String[] periods = {"Jour", "Mois", "Année"};
        cbPeriodicity = new JComboBox<>(periods);
        cbPeriodicity.setSelectedIndex(2);
        cbPeriodicity.addActionListener(e -> updateChart());
        cbPeriodicity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelTop.add(new JLabel("Période : "));
        panelTop.add(cbPeriodicity);
        panelDepenses.add(panelTop, BorderLayout.NORTH);
        loadChart("Année");
    }

    /**
     * Met à jour le graphique des dépenses selon la période sélectionnée.
     */
    private void updateChart() {
        String period = (String) cbPeriodicity.getSelectedItem();
        loadChart(period);
    }

    /**
     * Charge et affiche un graphique interactif des dépenses.
     */
    private void loadChart(String period) {
        panelDepenses.removeAll();
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTop.setBackground(new Color(245, 245, 245));
        panelTop.add(new JLabel("Période : "));
        panelTop.add(cbPeriodicity);
        panelDepenses.add(panelTop, BorderLayout.NORTH);
        List<CommandEntity> paidCommands = commandService.getPaidCommands(getClient());
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate now = LocalDate.now();
        switch (period) {
            case "Jour":
                for (int i = 6; i >= 0; i--) {
                    LocalDate date = now.minusDays(i);
                    double total = getTotalForPeriod(paidCommands, date, "Jour");
                    dataset.addValue(total, "Dépenses", date.toString());
                }
                break;
            case "Mois":
                for (int i = 6; i >= 0; i--) {
                    LocalDate date = now.minusMonths(i);
                    double total = getTotalForPeriod(paidCommands, date, "Mois");
                    dataset.addValue(total, "Dépenses", date.getMonth().toString() + " " + date.getYear());
                }
                break;
            case "Année":
                for (int i = 4; i >= 0; i--) {
                    LocalDate date = now.minusYears(i);
                    double total = getTotalForPeriod(paidCommands, date, "Année");
                    dataset.addValue(total, "Dépenses", String.valueOf(date.getYear()));
                }
                break;
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Dépenses par " + period, "Période", "Montant (€)", dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        panelDepenses.add(chartPanel, BorderLayout.CENTER);
        panelDepenses.revalidate();
        panelDepenses.repaint();
    }

    /**
     * Calcule le total des dépenses pour une période donnée.
     */
    private double getTotalForPeriod(List<CommandEntity> commands, LocalDate date, String period) {
        return commands.stream()
                .filter(cmd -> {
                    LocalDate cmdDate = cmd.getCommandDate().toLocalDate();
                    return switch (period) {
                        case "Jour" -> cmdDate.isEqual(date);
                        case "Mois" -> cmdDate.getMonth() == date.getMonth() && cmdDate.getYear() == date.getYear();
                        default -> cmdDate.getYear() == date.getYear();
                    };
                })
                .mapToDouble(cmd -> cmd.getTotalAmount() != null ? cmd.getTotalAmount().doubleValue() : 0.0)
                .sum();
    }

    /**
     * Affiche un panneau de recommandations personnalisées dans le panel précédemment destiné aux catégories.
     */
    private void displayRecommendations() {
        panelCategories.setLayout(new BorderLayout());
        panelCategories.removeAll();
        JLabel titleLabel = new JLabel("✨ Recommandations pour vous", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(80, 80, 80));
        panelCategories.add(titleLabel, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        contentPanel.setBackground(Color.WHITE);
        List<VehicleEntity> recommendedVehicles = getRecommendedVehicles();
        if (recommendedVehicles != null && !recommendedVehicles.isEmpty()) {
            for (VehicleEntity vehicle : recommendedVehicles) {
                contentPanel.add(createRecommendationCard(vehicle));
            }
        } else {
            JLabel noRecLabel = new JLabel("Aucune recommandation disponible pour le moment.", SwingConstants.CENTER);
            noRecLabel.setForeground(new Color(150, 150, 150));
            contentPanel.add(noRecLabel);
        }
        JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        panelCategories.add(scrollPane, BorderLayout.CENTER);
        panelCategories.revalidate();
        panelCategories.repaint();
    }

    /**
     * Retourne une liste de véhicules recommandés (ici, simplement la liste des véhicules du client).
     */
    private List<VehicleEntity> getRecommendedVehicles() {
        VehicleService vehicleService = new VehicleService();
        return vehicleService.getUniqueVehicles();
    }

    /**
     * Crée une carte de recommandation pour un véhicule.
     */
    private Component createRecommendationCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setPreferredSize(new Dimension(180, 220));
        java.net.URL imageUrl = getClass().getResource(vehicle.getImageUrl());
        if (imageUrl == null) {
            System.err.println("Image '/images/vehicle_placeholder.jpg' non trouvée. Utilisation de '/images/audi_rs6.jpg' comme alternative.");
            imageUrl = getClass().getResource("/images/audi_rs6.jpg");
        }
        ImageIcon icon = new ImageIcon(imageUrl);
        Image img = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("🚘 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(80, 80, 80));
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 128, 0));
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.CENTER);
        JButton detailsButton = new JButton("Voir plus");
        detailsButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Détails du véhicule " + vehicle.getModel().getModelName());
        });
        card.add(detailsButton, BorderLayout.SOUTH);
        return card;
    }

    // À ajouter dans la classe HomeForm
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        // Création du panneau de la carte avec un BorderLayout
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        // Définir une dimension fixe pour la carte
        card.setPreferredSize(new Dimension(250, 220));

        // En-tête : le nom du véhicule
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getBrandName() + " " +
                vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(70, 70, 70));
        card.add(nameLabel, BorderLayout.NORTH);

        // Centre : l'image du véhicule
        JLabel imageLabel;
        try {
            // Charger l'image depuis le chemin absolu (par exemple "/images/monImage.jpg")
            URL imageUrl = getClass().getResource(vehicle.getImageUrl());
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                // Redimensionner l'image pour qu'elle s'intègre dans la zone centrale de la carte.
                // Ici, nous choisissons 230 pixels de largeur et 120 pixels de hauteur.
                Image scaledImage = icon.getImage().getScaledInstance(230, 120, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
            } else {
                imageLabel = new JLabel("No Image", SwingConstants.CENTER);
            }
        } catch (Exception e) {
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
        }
        card.add(imageLabel, BorderLayout.CENTER);

        // Pied de carte : le prix du véhicule
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 128, 0));
        card.add(priceLabel, BorderLayout.SOUTH);

        return card;
    }


    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }
}
