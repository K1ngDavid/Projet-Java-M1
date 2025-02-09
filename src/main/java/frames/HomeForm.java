package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import service.CommandService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeForm extends AbstractFrame {
    private CommandService commandService;
    private JPanel panelDepenses;
    private JPanel panelVoitures;
    private JPanel panelCommandes;
    private JPanel panelCategories;
    private JComboBox<String> cbPeriodicity;

    public HomeForm(ClientEntity client) {
        super(client);
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null !");
        }
        commandService = new CommandService();
        initComponents();
        this.pack();
    }

    private void initComponents() {
        pnlRoot.setBackground(new Color(210, 231, 255));

        JLabel jLabel1 = new JLabel("🏠 Home");
        jLabel1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        jLabel1.setForeground(new Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        // Création des panneaux principaux avec bordures
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        panelCategories = createPanel("📂 Mes catégories", lineBorder);
        panelCommandes = createScrollablePanel("📜 Mes commandes", lineBorder);
        panelVoitures = createScrollablePanel("🚗 Mes voitures", lineBorder);
        panelDepenses = createPanel("📊 Mes dépenses", lineBorder);

        displayCommandes();
        displayVoitures();
        initDepensesPanel();
        displayCategories();

        // Disposition des panneaux
        GroupLayout layout = new GroupLayout(pnlRoot);
        pnlRoot.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(panelCategories)
                                        .addComponent(panelVoitures))
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(panelDepenses)
                                        .addComponent(panelCommandes))
                                .addContainerGap(20, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(30)
                        .addComponent(jLabel1)
                        .addGap(30)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelCategories)
                                .addComponent(panelDepenses))
                        .addGap(30)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelVoitures)
                                .addComponent(panelCommandes))
                        .addGap(30)
        );
    }

    /**
     * 🔥 Crée un panneau générique avec une bordure et une mise en page flexible.
     */
    private JPanel createPanel(String title, Border border) {
        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    /**
     * 🔥 Crée un panneau avec un `JScrollPane` pour permettre le défilement.
     */
    private JPanel createScrollablePanel(String title, Border border) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(border);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        containerPanel.add(label, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    /**
     * 🔥 Affiche les voitures sous forme de cartes bien organisées dans un panneau avec scroll.
     */
    private void displayVoitures() {
        JPanel contentPanel = (JPanel) ((JScrollPane) panelVoitures.getComponent(1)).getViewport().getView();
        contentPanel.removeAll();

        List<VehicleEntity> voitures = getClient().getVehicles();

        if (voitures != null && !voitures.isEmpty()) {
            for (VehicleEntity voiture : voitures) {
                contentPanel.add(createVehicleCard(voiture));
            }
        } else {
            contentPanel.add(new JLabel("🚗 Aucune voiture disponible", SwingConstants.CENTER));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Component createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice().toString() + " €", SwingConstants.CENTER);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(priceLabel, BorderLayout.SOUTH);
        return card;
    }

    /**
     * 🔥 Affiche les commandes du client sous forme de cartes avec scroll.
     */
    private void displayCommandes() {
        JPanel contentPanel = (JPanel) ((JScrollPane) panelCommandes.getComponent(1)).getViewport().getView();
        contentPanel.removeAll();

        List<CommandEntity> commandes = commandService.getPaidCommands(getClient());

        if (commandes != null && !commandes.isEmpty()) {
            for (CommandEntity commande : commandes) {
                JLabel cmdLabel = new JLabel("<html>Commande #" + commande.getIdCommand() + "🗓️   " +
                        commande.getCommandStatus() + "<br>" +
                        commande.getCommandDate() + "<br>💰 " + commande.getTotalAmount() + " €</html>");
                cmdLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                contentPanel.add(cmdLabel);
            }
        } else {
            contentPanel.add(new JLabel("📜 Aucune commande disponible", SwingConstants.CENTER));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * 📊 Initialisation du panel des dépenses avec un graphique.
     */
    private void initDepensesPanel() {
        panelDepenses.setLayout(new BorderLayout());

        // ✅ Création du panneau pour la ComboBox
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // ✅ Ajout de la ComboBox pour la sélection de période
        String[] periods = {"Jour", "Mois", "Année"};
        cbPeriodicity = new JComboBox<>(periods);
        cbPeriodicity.setSelectedIndex(2);
        cbPeriodicity.addActionListener(e -> updateChart());

        panelTop.add(new JLabel("Période : "));
        panelTop.add(cbPeriodicity);

        // ✅ Ajout du panneau supérieur (ComboBox)
        panelDepenses.add(panelTop, BorderLayout.NORTH);

        // ✅ Charger le graphique par défaut (Année)
        loadChart("Année");
    }

    /**
     * 📊 Met à jour le graphique en fonction de la période sélectionnée.
     */
    private void updateChart() {
        String period = (String) cbPeriodicity.getSelectedItem();
        loadChart(period);
    }

    /**
     * 📊 Charge et affiche un graphique des dépenses sur une période donnée.
     */
    private void loadChart(String period) {
        panelDepenses.removeAll();  // ✅ Supprime l'ancien contenu
        panelDepenses.add(cbPeriodicity, BorderLayout.NORTH);  // ✅ On garde la ComboBox

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

        // ✅ Ajouter le graphique dans le panneau central
        panelDepenses.add(new ChartPanel(chart), BorderLayout.CENTER);

        panelDepenses.revalidate();
        panelDepenses.repaint();
    }

    /**
     * 🔥 Affiche les catégories de véhicules achetés sous forme de cartes.
     */
    private void displayCategories() {
        panelCategories.setLayout(new GridLayout(0, 2, 10, 10)); // ✅ Affichage en grille (2 colonnes)
        panelCategories.removeAll(); // 🔥 Nettoyage avant ajout

        Set<String> categories = new HashSet<>();
        for (CommandEntity commande : commandService.getPaidCommands(getClient())) {
            for (VehicleEntity vehicle : commande.getVehicles()) {
                categories.add(vehicle.getVehicleType().toString()); // ✅ Ajouter la catégorie unique
            }
        }

        if (categories.isEmpty()) {
            JLabel noCategoriesLabel = new JLabel("📌 Aucune catégorie disponible", SwingConstants.CENTER);
            noCategoriesLabel.setForeground(Color.RED);
            panelCategories.add(noCategoriesLabel);
        } else {
            for (String categorie : categories) {
                JPanel categoryPanel = new JPanel(new BorderLayout());
                categoryPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                categoryPanel.setBackground(new Color(230, 230, 250));

                JLabel categoryLabel = new JLabel("🚘 " + categorie, SwingConstants.CENTER);
                categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                categoryPanel.add(categoryLabel, BorderLayout.CENTER);

                panelCategories.add(categoryPanel);
            }
        }

        panelCategories.revalidate();
        panelCategories.repaint();
    }


    /**
     * 🔥 Calcule le total des dépenses pour une période donnée.
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


    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(this.getClient()).setVisible(true);
    }
}
