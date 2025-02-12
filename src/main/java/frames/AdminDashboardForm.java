package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import service.ClientService;
import service.CommandService;
import service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardForm extends AbstractFrame {

    private CommandService commandService;
    private VehicleService vehicleService;
    private ClientService clientService;

    // Données chargées
    private List<CommandEntity> paidCommands;
    private List<CommandEntity> pendingOrders;
    // Pour le graphique Top Véhicules
    private List<VehicleEntity> uniqueVehicles;

    // Panneaux spécifiques
    private JPanel panelDashboard;       // Dashboard avec graphique dépenses et revenu total
    private JPanel panelDepenses;        // Graphique des dépenses avec combo box pour la période
    private JPanel panelTopVehicles;     // Graphique des véhicules les plus vendus
    private JPanel panelPendingOrders;   // Commandes en attente
    private JPanel panelUsers;           // Tableau des utilisateurs avec leur montant dépensé

    // ComboBox pour filtrer les dépenses par période
    private JComboBox<String> cbDepensesPeriod;

    public AdminDashboardForm(ClientEntity admin) {
        super(admin);
        if (admin.getRole() != ClientEntity.Role.ADMIN) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un administrateur !");
        }
        commandService = new CommandService();
        vehicleService = new VehicleService();
        clientService = new ClientService();

        initComponents();
        loadAdminData();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Configuration du panneau racine
        pnlRoot.setBackground(Color.WHITE);
        pnlRoot.setLayout(new BorderLayout());

        // ---- HEADER (identique à HomeForm) ----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/audi_rs6.jpg"));
        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(img));
        avatarLabel.setPreferredSize(new Dimension(60, 60));
        JLabel greetingLabel = new JLabel("Bonjour Admin, " + getClient().getName() + " 👋", SwingConstants.LEFT);
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        greetingLabel.setForeground(Color.WHITE);
        greetingLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(avatarLabel, BorderLayout.WEST);
        headerPanel.add(greetingLabel, BorderLayout.CENTER);
        pnlRoot.add(headerPanel, BorderLayout.NORTH);

        // ---- CONTENU (Tableau de Bord Admin) ----
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        Color panelBg = new Color(245, 245, 245);

        // Création des panneaux avec titres
        panelDashboard = createPanel("📊 Tableau de Bord", panelBg);
        panelDepenses = createPanel("📈 Dépenses", panelBg);
        panelTopVehicles = createPanel("🚀 Top Véhicules", panelBg);
        panelPendingOrders = createScrollablePanel("⌛ Toutes les commandes", panelBg);
        panelUsers = createScrollablePanel("👥 Utilisateurs et Dépenses", panelBg);

        contentPanel.add(panelUsers);
        contentPanel.add(panelDepenses);
        contentPanel.add(panelTopVehicles);
        contentPanel.add(panelPendingOrders);  // On ajoutera panelUsers dans le dashboard ou ailleurs selon votre besoin

        // Pour cet exemple, nous allons ajouter panelUsers dans un onglet supplémentaire
        // Vous pouvez aussi l'ajouter dans le GridLayout en réorganisant la structure
        pnlRoot.add(contentPanel, BorderLayout.CENTER);
        // Ajout d'un panneau en bas pour afficher le tableau des utilisateurs
    }

    /**
     * Crée un panneau avec titre et fond coloré.
     */
    private JPanel createPanel(String title, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(10,10,10,10)
        ));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(80,80,80));
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Crée un panneau scrollable avec titre.
     */
    private JPanel createScrollablePanel(String title, Color bgColor) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(bgColor);
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(10,10,10,10)
        ));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(80,80,80));
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
     * Charge les données administratives et met à jour les panneaux.
     */
    private void loadAdminData() {
        // Charge les commandes payées et en attente, ainsi que les véhicules uniques.
        paidCommands = commandService.getAllPaidCommands();
        pendingOrders = commandService.getPendingCommands(getClient());
        uniqueVehicles = vehicleService.getUniqueVehicles();
        updateUsersPanel();
        updateDepensesPanel();
        updateTopVehiclesPanel();
        updatePendingOrdersPanel();

    }

    /**
     * Met à jour le panneau Dashboard avec un graphique des dépenses sur 5 ans et le revenu total.
     */
    private void updateDashboardPanel() {
        JPanel dashboardContent = new JPanel(new GridLayout(1, 2, 20, 20));
        dashboardContent.setBackground(Color.WHITE);

        // Graphique des dépenses sur 5 ans
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate now = LocalDate.now();
        for (int i = 4; i >= 0; i--) {
            LocalDate date = now.minusYears(i);
            double total = paidCommands.stream()
                    .filter(cmd -> cmd.getCommandDate().toLocalDate().getYear() == date.getYear())
                    .mapToDouble(cmd -> cmd.getTotalAmount() != null ? cmd.getTotalAmount().doubleValue() : 0.0)
                    .sum();
            dataset.addValue(total, "Dépenses", String.valueOf(date.getYear()));
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Dépenses Totales", "Année", "Montant (€)", dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);

        // Revenu total
        BigDecimal totalRevenue = paidCommands.stream()
                .map(CommandEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        JLabel lblRevenue = new JLabel("Revenu Total : " + totalRevenue + " €", SwingConstants.CENTER);
        lblRevenue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRevenue.setForeground(new Color(0, 128, 0));

        dashboardContent.add(chartPanel);
        dashboardContent.add(lblRevenue);

        panelDashboard.add(dashboardContent, BorderLayout.CENTER);
        panelDashboard.revalidate();
        panelDashboard.repaint();
    }

    /**
     * Met à jour le panneau des dépenses avec un combo box pour la périodicité.
     */
    private void updateDepensesPanel() {
        panelDepenses.removeAll();

        // Panneau de contrôle pour la période
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(new Color(245, 245, 245));
        String[] periods = {"Jour", "Mois", "Année"};
        cbDepensesPeriod = new JComboBox<>(periods);
        cbDepensesPeriod.setSelectedIndex(2);
        cbDepensesPeriod.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbDepensesPeriod.addActionListener(e -> updateDepensesChart());
        controlPanel.add(new JLabel("Période : "));
        controlPanel.add(cbDepensesPeriod);
        panelDepenses.add(controlPanel, BorderLayout.NORTH);

        updateDepensesChart();
    }

    /**
     * Met à jour le graphique des dépenses selon la période sélectionnée.
     */
    private void updateDepensesChart() {
        panelDepenses.removeAll();

        // Réutilisation du panneau de contrôle
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.add(new JLabel("Période : "));
        controlPanel.add(cbDepensesPeriod);
        panelDepenses.add(controlPanel, BorderLayout.NORTH);

        String period = (String) cbDepensesPeriod.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate now = LocalDate.now();

        switch (period) {
            case "Jour":
                for (int i = 6; i >= 0; i--) {
                    LocalDate date = now.minusDays(i);
                    double total = paidCommands.stream()
                            .filter(cmd -> cmd.getCommandDate().toLocalDate().isEqual(date))
                            .mapToDouble(cmd -> cmd.getTotalAmount() != null ? cmd.getTotalAmount().doubleValue() : 0.0)
                            .sum();
                    dataset.addValue(total, "Dépenses", date.toString());
                }
                break;
            case "Mois":
                for (int i = 6; i >= 0; i--) {
                    LocalDate date = now.minusMonths(i);
                    double total = paidCommands.stream()
                            .filter(cmd -> {
                                LocalDate cmdDate = cmd.getCommandDate().toLocalDate();
                                return cmdDate.getMonth() == date.getMonth() && cmdDate.getYear() == date.getYear();
                            })
                            .mapToDouble(cmd -> cmd.getTotalAmount() != null ? cmd.getTotalAmount().doubleValue() : 0.0)
                            .sum();
                    dataset.addValue(total, "Dépenses", date.getMonth().toString() + " " + date.getYear());
                }
                break;
            case "Année":
            default:
                for (int i = 4; i >= 0; i--) {
                    LocalDate date = now.minusYears(i);
                    double total = paidCommands.stream()
                            .filter(cmd -> cmd.getCommandDate().toLocalDate().getYear() == date.getYear())
                            .mapToDouble(cmd -> cmd.getTotalAmount() != null ? cmd.getTotalAmount().doubleValue() : 0.0)
                            .sum();
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
     * Met à jour le panneau des véhicules les plus vendus.
     */
    private void updateTopVehiclesPanel() {
        panelTopVehicles.removeAll();

        // Récupérer les ventes par modèle via un service (ici, on utilise une méthode fictive)
        // Supposons que getSalesCountByVehicle() retourne une Map<String, Long>
        var salesCount = vehicleService.getSalesCountByVehicle(); // Cette méthode doit être adaptée dans votre CommandService

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (salesCount != null && !salesCount.isEmpty()) {
            salesCount.forEach((model, count) -> dataset.addValue(count, "Ventes", model));
        } else {
            dataset.addValue(0, "Ventes", "Aucune donnée");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top Véhicules", "Modèle", "Nombre de Ventes", dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        panelTopVehicles.add(chartPanel, BorderLayout.CENTER);
        panelTopVehicles.revalidate();
        panelTopVehicles.repaint();
    }

    /**
     * Met à jour le panneau des commandes en attente.
     */
    private void updatePendingOrdersPanel() {
        JPanel pendingContent = (JPanel) ((JScrollPane) panelPendingOrders.getComponent(1)).getViewport().getView();
        pendingContent.removeAll();
        String[] columns = {"ID Commande", "Date", "Montant (€)","Etat","Utilisateur"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        List<CommandEntity> pendingOrders = commandService.getAllCommands();
        for (CommandEntity order : pendingOrders) {
            System.out.println(order.getClient());
            model.addRow(new Object[]{
                    order.getIdCommand(),
                    order.getCommandDate(),
                    order.getTotalAmount(),
                    order.getCommandStatus(),
                    order.getClient().getName(),
            });
        }
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220,220,220));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        pendingContent.add(scrollPane);
        pendingContent.revalidate();
        pendingContent.repaint();
    }

    /**
     * Met à jour le panneau des utilisateurs en affichant, dans une JTable, pour chaque utilisateur son montant total dépensé, trié par ordre décroissant.
     */
    private void updateUsersPanel() {
        JPanel usersContent = (JPanel) ((JScrollPane) panelUsers.getComponent(1)).getViewport().getView();
        usersContent.removeAll();

        String[] columns = {"ID", "Nom", "Email", "Dépenses (€)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Récupérer tous les clients via ClientService (assurez-vous d'avoir une telle méthode)
        List<ClientEntity> clients = new ClientService().getAllClients();

        // Pour chaque client, calculer le total dépensé en sommant les commandes payées
        List<Object[]> clientSpentList = clients.stream()
                .map(c -> {
                    BigDecimal totalSpent = c.getCommands().stream()
                            .filter(cmd -> "Payée".equalsIgnoreCase(cmd.getCommandStatus()))
                            .map(CommandEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new Object[]{c.getIdClient(), c.getName(), c.getEmail(), totalSpent};
                })
                .sorted((a, b) -> ((BigDecimal)b[3]).compareTo((BigDecimal)a[3]))
                .collect(Collectors.toList());

        for (Object[] row : clientSpentList) {
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220,220,220));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        usersContent.add(scrollPane);
        usersContent.revalidate();
        usersContent.repaint();
    }

    /**
     * Retourne une liste de véhicules recommandés (ici, pour l'exemple, on utilise la liste des véhicules du client).
     */
    private List<VehicleEntity> getRecommendedVehicles() {
        // Vous pouvez remplacer ceci par un appel à un service pour obtenir des recommandations.
        return getClient().getVehicles();
    }

    /**
     * Crée une carte de recommandation pour un véhicule.
     */
    private Component createRecommendationCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200), 1),
                BorderFactory.createEmptyBorder(8,8,8,8)
        ));
        card.setPreferredSize(new Dimension(180,220));
        URL imageUrl = getClass().getResource(vehicle.getImageUrl());
        if (imageUrl == null) {
            System.err.println("Image '/images/vehicle_placeholder.jpg' non trouvée. Utilisation de '/images/audi_rs6.jpg' comme alternative.");
            imageUrl = getClass().getResource("/images/audi_rs6.jpg");
        }
        ImageIcon icon = new ImageIcon(imageUrl);
        Image img = icon.getImage().getScaledInstance(160,100,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);
        JPanel infoPanel = new JPanel(new GridLayout(2,1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("🚘 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(80,80,80));
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0,128,0));
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.CENTER);
        JButton detailsButton = new JButton("Voir plus");
        detailsButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsButton.addActionListener(e -> {
            try {
                new ProductForm(getClient(), vehicle);
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        card.add(detailsButton, BorderLayout.SOUTH);
        return card;
    }

    // La version client de createVehicleCard, au cas où vous en avez besoin
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200), 1),
                BorderFactory.createEmptyBorder(8,8,8,8)
        ));
        card.setPreferredSize(new Dimension(250,220));
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(70,70,70));
        card.add(nameLabel, BorderLayout.NORTH);
        JLabel imageLabel;
        try {
            URL imageUrl = getClass().getResource(vehicle.getImageUrl());
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaledImage = icon.getImage().getScaledInstance(230,120,Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
            } else {
                imageLabel = new JLabel("No Image", SwingConstants.CENTER);
            }
        } catch (Exception e) {
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
        }
        card.add(imageLabel, BorderLayout.CENTER);
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0,128,0));
        card.add(priceLabel, BorderLayout.SOUTH);
        return card;
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }
}
