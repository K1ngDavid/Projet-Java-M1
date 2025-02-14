package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.VehicleEntity;
import service.CommandService;
import service.ClientService;
import tools.AdvancedSearchBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class MyCommandsForm extends AbstractFrame {
    private JPanel pnlOrders;
    private JComboBox<String> cbFilter;
    private CommandService commandService;
    private ClientService clientService;

    // Stockage temporaire des commandes du client
    private List<CommandEntity> orders;

    public MyCommandsForm(ClientEntity client) {
        super(client);
        this.commandService = new CommandService();
        this.clientService = new ClientService();
        setTitle("📜 Historique des Commandes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Utiliser un BorderLayout sur pnlRoot (hérité d'AbstractFrame)
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.setBackground(new Color(245, 245, 245));

        // Titre principal
        JLabel lblTitle = new JLabel("📜 Mes Commandes", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlRoot.add(lblTitle, BorderLayout.NORTH);

        // Barre de filtre des commandes (bas de l'écran)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.setBackground(new Color(245, 245, 245));
        pnlFilter.add(new JLabel("🔍 Filtrer par statut: "));
        cbFilter = new JComboBox<>(new String[]{"Toutes", "En attente", "Payée"});
        cbFilter.addActionListener(e -> loadOrders());
        pnlFilter.add(cbFilter);
        pnlRoot.add(pnlFilter, BorderLayout.SOUTH);

        // Panel contenant les commandes
        pnlOrders = new JPanel();
        pnlOrders.setLayout(new BoxLayout(pnlOrders, BoxLayout.Y_AXIS));
        pnlOrders.setBackground(Color.WHITE);
        pnlOrders.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(pnlOrders);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnlRoot.add(scrollPane, BorderLayout.CENTER);

        // Charger les commandes en arrière-plan via SwingWorker
        new OrderLoaderWorker().execute();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }

    /**
     * SwingWorker pour charger les commandes.
     */
    private class OrderLoaderWorker extends SwingWorker<List<CommandEntity>, Void> {
        @Override
        protected List<CommandEntity> doInBackground() throws Exception {
            // Si le client est admin, récupérer toutes les commandes,
            // sinon récupérer uniquement celles du client.
            if (getClient().getRole() == ClientEntity.Role.ADMIN) {
                return commandService.getAllCommands();
            } else {
                return commandService.getAllCommandsByClient(getClient());
            }
        }

        @Override
        protected void done() {
            try {
                orders = get();  // Récupère la liste de commandes depuis doInBackground()
                loadOrders();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Met à jour l'affichage des commandes selon le filtre sélectionné.
     */
    private void loadOrders() {
        pnlOrders.removeAll();
        String selectedFilter = (String) cbFilter.getSelectedItem();

        if (orders == null || orders.isEmpty()) {
            JLabel lblNoOrders = new JLabel("❌ Aucune commande trouvée.", SwingConstants.CENTER);
            lblNoOrders.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblNoOrders.setForeground(Color.GRAY);
            pnlOrders.add(lblNoOrders);
        } else {
            for (CommandEntity order : orders) {
                // Appliquer le filtre si nécessaire
                if (!"Toutes".equals(selectedFilter) && !order.getCommandStatus().equals(selectedFilter)) {
                    continue;
                }
                pnlOrders.add(createOrderPanel(order));
                pnlOrders.add(Box.createRigidArea(new Dimension(0, 15))); // Espacement entre les cartes
            }
        }
        pnlOrders.revalidate();
        pnlOrders.repaint();
    }

    /**
     * Crée une carte visuelle pour afficher une commande.
     */
    private JPanel createOrderPanel(CommandEntity order) {
        JPanel panelOrder = new JPanel(new BorderLayout());
        panelOrder.setBackground(Color.WHITE);
        panelOrder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // En-tête de la commande (Date, Statut et, pour l'admin, le nom du client)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(230, 230, 230));
        JLabel lblCommandInfo = new JLabel("📅 " + order.getCommandDate() + "  ID : " + order.getIdCommand(), SwingConstants.LEFT);
        lblCommandInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblStatus = new JLabel(order.getCommandStatus(), SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnlHeader.add(lblCommandInfo, BorderLayout.WEST);
        pnlHeader.add(lblStatus, BorderLayout.EAST);

        // Si l'utilisateur est admin, afficher le nom du client
        if (getClient().getRole() == ClientEntity.Role.ADMIN && order.getClient() != null) {
            JLabel lblClientName = new JLabel("👤 " + order.getClient().getName(), SwingConstants.CENTER);
            lblClientName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            pnlHeader.add(lblClientName, BorderLayout.SOUTH);
        }

        panelOrder.add(pnlHeader, BorderLayout.NORTH);

        // Liste des véhicules (affichés en miniatures)
        JPanel pnlVehicles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlVehicles.setBackground(Color.WHITE);
        for (CommandLineEntity line : order.getCommandLines()) {
            pnlVehicles.add(createVehicleCard(line.getVehicle()));
        }
        panelOrder.add(pnlVehicles, BorderLayout.CENTER);

        // Panneau d'actions
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pnlActions.setBackground(Color.WHITE);
        JButton btnDetails = createStyledButton("🔍 Voir Détails");
        btnDetails.addActionListener(e -> showOrderDetails(order));
        pnlActions.add(btnDetails);

        // Si l'utilisateur est admin, ajouter les boutons "Modifier" et "Supprimer"
        if (getClient().getRole() == ClientEntity.Role.ADMIN) {
            JButton btnEdit = createStyledButton("✏ Modifier");
            btnEdit.addActionListener(e -> editOrder(order));
            JButton btnDelete = createStyledButton("🗑 Supprimer");
            btnDelete.addActionListener(e -> deleteOrder(order));
            pnlActions.add(btnEdit);
            pnlActions.add(btnDelete);
        }

        // Bouton "Télécharger Facture" (disponible pour tous)
        JButton btnDownload = createStyledButton("📄 Télécharger Facture");
        btnDownload.addActionListener(e -> downloadInvoice(order));
        pnlActions.add(btnDownload);

        panelOrder.add(pnlActions, BorderLayout.SOUTH);

        return panelOrder;
    }

    /**
     * Crée une carte pour afficher un véhicule commandé, avec image, nom et prix.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Nom du véhicule
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(70, 70, 70));

        // Prix du véhicule
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 128, 0));

        // Image du véhicule (miniature)
        JLabel imageLabel = createScaledImageLabel(vehicle.getImageUrl(), 200, 150);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton "Voir plus"
        JButton btnVoir = new JButton("Voir plus");
        btnVoir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVoir.addActionListener(e -> {
            try {
                new ProductForm(getClient(), vehicle).setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Assemblage de la carte
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(priceLabel, BorderLayout.NORTH);
        pnlBottom.add(btnVoir, BorderLayout.SOUTH);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Crée et retourne un JLabel affichant l'image redimensionnée.
     */
    private JLabel createScaledImageLabel(String imagePath, int width, int height) {
        ImageIcon icon;
        try {
            URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) {
                BufferedImage originalImage = ImageIO.read(imgURL);
                icon = new ImageIcon(scaleImage(originalImage, width, height));
            } else {
                throw new IOException("Image non trouvée : " + imagePath);
            }
        } catch (IOException | NullPointerException e) {
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
        return new JLabel(icon, SwingConstants.CENTER);
    }

    /**
     * Redimensionne un BufferedImage avec de bonnes performances et qualité.
     */
    private BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaledImage;
    }

    /**
     * Applique un style uniforme aux boutons.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showOrderDetails(CommandEntity order) {
        JOptionPane.showMessageDialog(this, "📜 Détails de la commande #" + order.getIdCommand(), "Détails Commande", JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadInvoice(CommandEntity order) {
        JOptionPane.showMessageDialog(this, "📄 Facture téléchargée pour la commande #" + order.getIdCommand(), "Téléchargement", JOptionPane.INFORMATION_MESSAGE);
    }

    // Méthode appelée pour l'édition d'une commande (pour l'admin)
    private void editOrder(CommandEntity order) {
        // Vous pouvez créer une nouvelle fenêtre/formulaire pour éditer la commande.
        JOptionPane.showMessageDialog(this, "Fonctionnalité d'édition pour la commande #" + order.getIdCommand(), "Modifier Commande", JOptionPane.INFORMATION_MESSAGE);
    }

    // Méthode appelée pour la suppression d'une commande (pour l'admin)
// Méthode appelée pour la suppression d'une commande (pour l'admin)
    private void deleteOrder(CommandEntity order) {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer la commande #" + order.getIdCommand() + " ?",
                "Confirmer la suppression",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            try {
                // Retirer la commande de la collection du client
                ClientEntity client = order.getClient();
                if (client != null && client.getCommands() != null) {
                    client.getCommands().remove(order);
                }
                // Supprimer la commande dans la base de données
                if (commandService.deleteCommand(order)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Commande supprimée avec succès.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // Recharger les commandes après suppression
                    new OrderLoaderWorker().execute();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erreur lors de la suppression de la commande.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Erreur lors de la suppression de la commande : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

}
