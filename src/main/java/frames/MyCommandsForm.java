package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.VehicleEntity;
import service.CommandService;
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

    // Stockage temporaire des commandes du client
    private List<CommandEntity> orders;

    public MyCommandsForm(ClientEntity client) {
        super(client);
        this.commandService = new CommandService();

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
     * Charge les commandes du client en arrière-plan via SwingWorker.
     */
    private class OrderLoaderWorker extends SwingWorker<List<CommandEntity>, Void> {
        @Override
        protected List<CommandEntity> doInBackground() throws Exception {
            return getClient().getCommands();
        }
        @Override
        protected void done() {
            try {
                orders = get();
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
                if (!selectedFilter.equals("Toutes") && !order.getCommandStatus().equals(selectedFilter)) {
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

        // En-tête de la commande (Date et Statut)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(230, 230, 230));
        JLabel lblCommandInfo = new JLabel("📅 " + order.getCommandDate(), SwingConstants.LEFT);
        lblCommandInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblStatus = new JLabel(order.getCommandStatus(), SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlHeader.add(lblCommandInfo, BorderLayout.WEST);
        pnlHeader.add(lblStatus, BorderLayout.EAST);
        panelOrder.add(pnlHeader, BorderLayout.NORTH);

        // Liste des véhicules (affichés en miniatures)
        JPanel pnlVehicles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlVehicles.setBackground(Color.WHITE);
        for (CommandLineEntity line : order.getCommandLines()) {
            pnlVehicles.add(createVehicleCard(line.getVehicle()));
        }
        panelOrder.add(pnlVehicles, BorderLayout.CENTER);

        // Panneau d'actions (Détails et Télécharger Facture)
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pnlActions.setBackground(Color.WHITE);
        JButton btnDetails = createStyledButton("🔍 Voir Détails");
        btnDetails.addActionListener(e -> showOrderDetails(order));
        JButton btnDownload = createStyledButton("📄 Télécharger Facture");
        btnDownload.addActionListener(e -> downloadInvoice(order));
        pnlActions.add(btnDetails);
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
                // Utiliser ImageIO pour charger l'image et obtenir un BufferedImage
                BufferedImage originalImage = javax.imageio.ImageIO.read(imgURL);
                icon = new ImageIcon(scaleImage(originalImage, width, height));
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

}
