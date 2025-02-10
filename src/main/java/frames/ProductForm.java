package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.CommandLineEntityPK;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import service.CommandService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;

public class ProductForm extends AbstractFrame {
    private VehicleEntity vehicle;
    private CommandService commandService;
    private CommandEntity activeCommand;

    // Composants spécifiques à ProductForm
    private JLabel lblTitle;
    private JPanel pnlProduct;      // Contiendra l'image et la description
    private JButton btnAddToCart;   // Bouton "Ajouter au Panier"

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;
        this.commandService = new CommandService();

        initComponents();

        // Configuration de pnlRoot (hérité d'AbstractFrame)
        pnlRoot.setLayout(new BorderLayout(20, 20));
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlRoot.setBackground(new Color(240, 248, 255));

        pnlRoot.add(lblTitle, BorderLayout.NORTH);
        pnlRoot.add(pnlProduct, BorderLayout.CENTER);
        pnlRoot.add(btnAddToCart, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() throws IOException {
        // Titre du produit
        lblTitle = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Panneau produit regroupant l'image et la description
        pnlProduct = new JPanel(new BorderLayout(20, 20));
        pnlProduct.setBackground(Color.WHITE);
        pnlProduct.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Panneau d'image (à gauche)
        JPanel pnlImage = new JPanel(new BorderLayout());
        pnlImage.setPreferredSize(new Dimension(300, 300));
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel imageLabel = createImageLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pnlImage.add(imageLabel, BorderLayout.CENTER);

        // Zone de description (à droite)
        JTextArea taDescription = new JTextArea(vehicle.toString());
        taDescription.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taDescription.setForeground(new Color(66, 66, 66));
        taDescription.setEditable(false);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        taDescription.setOpaque(false);
        JScrollPane descriptionScroll = new JScrollPane(taDescription);
        descriptionScroll.setPreferredSize(new Dimension(300, 300));
        descriptionScroll.setBorder(BorderFactory.createEmptyBorder());

        pnlProduct.add(pnlImage, BorderLayout.WEST);
        pnlProduct.add(descriptionScroll, BorderLayout.CENTER);

        // Bouton "Ajouter au Panier"
        btnAddToCart = new JButton("🛒 Ajouter au Panier");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAddToCart.setBackground(new Color(76, 175, 80));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btnAddToCart.addActionListener(e -> {
            try {
                addToCart(vehicle);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "Véhicule ajouté au panier !", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Crée un JLabel affichant l'image redimensionnée pour occuper exactement 300×300 pixels.
     * Vérifiez que vehicle.getImageUrl() retourne un chemin absolu, par exemple "/images/monImage.jpg".
     */
    private JLabel createImageLabel() {
        int width = 300;
        int height = 300;
        URL imgURL = getClass().getResource(vehicle.getImageUrl());
        ImageIcon icon;
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        } else {
            // Création d'un placeholder en cas d'absence d'image
            BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawString("No Image", width / 2 - 30, height / 2);
            g2d.dispose();
            icon = new ImageIcon(placeholder);
        }
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
    }

    /**
     * Ajoute le véhicule au panier en utilisant la logique existante.
     */
    private void addToCart(VehicleEntity vehicle) throws IOException {
        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());
        EntityManager entityManager = commandService.getEntityManager();
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        if (getClient().getPanier().getVehicles().isEmpty()) {
            activeCommand = new CommandEntity();
            activeCommand.setCommandDate(new Date(System.currentTimeMillis()));
            activeCommand.setClient(getClient());
            activeCommand.setCommandStatus("En attente");
            commandService.createCommand(activeCommand);
        } else if (activeCommand == null) {
            try {
                activeCommand = commandService.getLastPendingCommand(getClient());
                System.out.println("ID de la commande utilisée : " + activeCommand.getIdCommand());
            } catch (Exception e) {
                e.printStackTrace();
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            }
        }
        boolean alreadyExists = activeCommand.getVehicles().stream()
                .anyMatch(v -> v.getIdVehicle() == vehicle.getIdVehicle());
        if (!alreadyExists) {
            CommandLineEntity commandLine = new CommandLineEntity();
            CommandLineEntityPK commandLinePK = new CommandLineEntityPK();
            commandLinePK.setIdCommand(activeCommand.getIdCommand());
            commandLinePK.setIdVehicle(vehicle.getIdVehicle());
            commandLine.setId(commandLinePK);
            commandLine.setCommand(activeCommand);
            commandLine.setVehicle(vehicle);
            entityManager.persist(commandLine);
            getClient().addToPanier(vehicle);
            System.out.println(getClient().getPanier().getVehicles());
        } else {
            JOptionPane.showMessageDialog(this, "Ce véhicule est déjà dans votre panier.", "Déjà ajouté", JOptionPane.WARNING_MESSAGE);
        }
        entityManager.getTransaction().commit();
        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());
        dispose();
        new CatalogForm(getClient());
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Implémentez selon vos besoins
    }
}
