package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.VehicleEntity;
import service.CommandService;
import tools.AdvancedSearchBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MyCommandsForm extends AbstractFrame {
    private JPanel pnlOrders;
    private JScrollPane scrollPane;
    private CommandService commandService;
    private JComboBox<String> cbFilter;

    public MyCommandsForm(ClientEntity client) {
        super(client);
        this.commandService = new CommandService();

        setTitle("📜 Historique des Commandes");
        setLocationRelativeTo(null);
//        AdvancedSearchBar searchBar = new AdvancedSearchBar(orders, this::loadOrders);
        // ✅ Assurer que pnlRoot a bien un layout
        pnlRoot.setLayout(new BorderLayout());

        // ✅ Titre principal
        JLabel lblTitle = new JLabel("📜 Mes Commandes", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlRoot.add(lblTitle, BorderLayout.NORTH);

        // ✅ Barre de filtre des commandes
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.add(new JLabel("🔍 Filtrer par statut: "));

        cbFilter = new JComboBox<>(new String[]{"Toutes", "En attente", "Payée"});
        cbFilter.addActionListener(e -> loadOrders());
        pnlFilter.add(cbFilter);
        pnlRoot.add(pnlFilter, BorderLayout.SOUTH);

        // ✅ Panel contenant les commandes avec BoxLayout
        pnlOrders = new JPanel();
        pnlOrders.setLayout(new BoxLayout(pnlOrders, BoxLayout.Y_AXIS)); // 🔥 Utilisation de BoxLayout pour la liste dynamique

        // ✅ Ajout du JScrollPane avec pnlOrders à l'intérieur
        scrollPane = new JScrollPane(pnlOrders);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        pnlRoot.add(scrollPane, BorderLayout.CENTER);

        // ✅ Charger les commandes
        loadOrders();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // 🔥 Ajoute l'action associée au bouton de compte
    }

    /**
     * 🔥 Charge et filtre les commandes du client.
     */
    private void loadOrders() {
        List<CommandEntity> orders = getClient().getCommands();

        pnlOrders.removeAll();
        String selectedFilter = (String) cbFilter.getSelectedItem();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // 🔥 Ajout de marges entre les cartes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (orders.isEmpty()) {
            JLabel lblNoOrders = new JLabel("❌ Aucune commande trouvée.", SwingConstants.CENTER);
            lblNoOrders.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblNoOrders.setForeground(Color.GRAY);
            pnlOrders.add(lblNoOrders, gbc);
        } else {
            for (CommandEntity order : orders) {
                if (!selectedFilter.equals("Toutes") && !order.getCommandStatus().equals(selectedFilter)) {
                    continue;
                }
                pnlOrders.add(createOrderPanel(order), gbc);
                gbc.gridy++; // 🔥 Ajoute un espace entre les cartes
            }
        }

        pnlOrders.revalidate();
        pnlOrders.repaint();
    }


    /**
     * ✅ Crée une "carte" visuelle pour afficher une commande.
     */
    private JPanel createOrderPanel(CommandEntity order) {
        JPanel panelOrder = new JPanel(new BorderLayout());
        panelOrder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panelOrder.setBackground(Color.WHITE);
        panelOrder.setMaximumSize(new Dimension(800, 180)); // 🔥 Taille fixe pour éviter l'effet "écrasé"

        // ✅ En-tête de la commande (Date + Statut)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(230, 230, 230));

        JLabel lblCommandInfo = new JLabel("📅 " + order.getCommandDate(), SwingConstants.LEFT);
        lblCommandInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblStatus = new JLabel(getStatusIcon(order.getCommandStatus()), SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnlHeader.add(lblCommandInfo, BorderLayout.WEST);
        pnlHeader.add(lblStatus, BorderLayout.EAST);
        panelOrder.add(pnlHeader, BorderLayout.NORTH);

        // ✅ Liste des véhicules en miniatures
        JPanel pnlVehicles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (CommandLineEntity line : order.getCommandLines()) {
            pnlVehicles.add(createVehicleCard(line.getVehicle()));
        }
        panelOrder.add(pnlVehicles, BorderLayout.CENTER);

        // ✅ Actions (Détails + Télécharger Facture)
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDetails = new JButton("🔍 Voir Détails");
        btnDetails.addActionListener(e -> showOrderDetails(order));

        JButton btnDownload = new JButton("📄 Télécharger Facture");
        btnDownload.addActionListener(e -> downloadInvoice(order));

        pnlActions.add(btnDetails);
        pnlActions.add(btnDownload);
        panelOrder.add(pnlActions, BorderLayout.SOUTH);

        return panelOrder;
    }

    /**
     * ✅ Génère une "mini-carte" d'un véhicule commandé.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        // ✅ Panel principal avec layout vertical
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // 🔥 Padding interne
        ));
        card.setBackground(Color.WHITE); // 🔥 Amélioration visuelle avec un fond blanc

        // ✅ Image redimensionnée et centrée
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(vehicle.getImageUrl()));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // ✅ Nom du véhicule
        JLabel nameLabel = new JLabel(vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Police plus lisible
        nameLabel.setForeground(Color.DARK_GRAY); // Couleur du texte

        // ✅ Panel pour aligner et espacer les éléments
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Fond transparent pour s'intégrer au panel principal
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createVerticalStrut(5)); // 🔥 Espacement entre image et texte
        contentPanel.add(nameLabel);

        // ✅ Ajouter tout au panel principal
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }



    /**
     * ✅ Ouvre une fenêtre avec plus de détails sur la commande.
     */
    private void showOrderDetails(CommandEntity order) {
        JOptionPane.showMessageDialog(this, "📜 Détails de la commande #" + order.getIdCommand(), "Détails Commande", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ✅ Génère et télécharge une facture PDF.
     */
    private void downloadInvoice(CommandEntity order) {
        JOptionPane.showMessageDialog(this, "📄 Facture téléchargée pour la commande #" + order.getIdCommand(), "Téléchargement", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ✅ Retourne une icône en fonction du statut de la commande.
     */
    private String getStatusIcon(String status) {
        switch (status) {
            case "En attente":
                return "🟡 En attente";
            case "Payée":
                return "🟢 Payée";
            default:
                return "⚪ Inconnu";
        }
    }
}
