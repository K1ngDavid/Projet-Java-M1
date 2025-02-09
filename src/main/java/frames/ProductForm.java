package frames;

import entity.*;
import jakarta.persistence.EntityManager;
import service.CommandService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;

public class ProductForm extends AbstractFrame {
    private JPanel pnlProduct;
    private JLabel lblTitre;
    private JPanel pnlImage;
    private JPanel pnlDescription;
    private JButton btnAjouterPanier;
    private VehicleEntity vehicle;
    private CommandService commandService;
    private CommandEntity activeCommand;

    public ProductForm(ClientEntity client, VehicleEntity vehicle) throws IOException {
        super(client);
        this.vehicle = vehicle;
        this.commandService = new CommandService();

        initComponents();

        pnlRoot.setLayout(new BorderLayout(10, 10));
        pnlRoot.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlRoot.setBackground(new Color(240, 248, 255));

        pnlRoot.add(lblTitre, BorderLayout.NORTH);
        pnlRoot.add(pnlProduct, BorderLayout.CENTER);
        pnlRoot.add(btnAjouterPanier, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() throws IOException {
        lblTitre = new JLabel("🚘 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(new Color(50, 50, 50));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        pnlImage = new JPanel(new BorderLayout());
        pnlImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlImage.setBackground(Color.WHITE);

        JLabel imageLabel = createImageLabel();
        pnlImage.add(imageLabel, BorderLayout.CENTER);

        pnlDescription = new JPanel();
        pnlDescription.setLayout(new BoxLayout(pnlDescription, BoxLayout.Y_AXIS));
        pnlDescription.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlDescription.setBackground(Color.WHITE);

        JLabel lblPrice = new JLabel("💰 Prix: " + vehicle.getPrice() + " €");
        JLabel lblType = new JLabel("🔧 Type: " + vehicle.getVehicleType());
        JLabel lblDescription = new JLabel(vehicle.toString());

        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(new Color(0, 128, 0));

        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnlDescription.add(lblPrice);
        pnlDescription.add(Box.createVerticalStrut(10));
        pnlDescription.add(lblType);
        pnlDescription.add(Box.createVerticalStrut(10));
        pnlDescription.add(Box.createVerticalStrut(10));
        pnlDescription.add(lblDescription);

        pnlProduct = new JPanel(new BorderLayout(15, 15));
        pnlProduct.setBackground(Color.WHITE);
        pnlProduct.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        pnlProduct.add(pnlImage, BorderLayout.WEST);
        pnlProduct.add(pnlDescription, BorderLayout.CENTER);

        btnAjouterPanier = new JButton("🛒 Ajouter au Panier");
        btnAjouterPanier.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAjouterPanier.setBackground(new Color(76, 175, 80));
        btnAjouterPanier.setForeground(Color.WHITE);
        btnAjouterPanier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAjouterPanier.setFocusPainted(false);
        btnAjouterPanier.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnAjouterPanier.addActionListener(e -> {
            try {
                addToCart(vehicle);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void addToCart(VehicleEntity vehicle) throws IOException {
        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());
        EntityManager entityManager = commandService.getEntityManager();

        // ✅ Démarrer la transaction (si elle n'est pas déjà active)
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        if (getClient().getPanier().getVehicles().isEmpty()) {
            activeCommand = new CommandEntity();
            activeCommand.setCommandDate(new Date(System.currentTimeMillis()));
            activeCommand.setClient(getClient());
            activeCommand.setCommandStatus("En attente");
            commandService.createCommand(activeCommand);
        } else if (activeCommand == null){
            try {
                // ✅ Vérifier s'il existe déjà une commande "En attente"
                activeCommand = commandService.getLastPendingCommand(getClient());

                System.out.println("ID de la commande utilisée : " + activeCommand.getIdCommand());
            } catch (Exception e) {
                e.printStackTrace();
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback(); // ❌ Annulation en cas d'erreur
                }
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }

        // ✅ Vérifier si le véhicule est déjà dans la commande
        boolean alreadyExists = activeCommand.getVehicles().stream()
                .anyMatch(v -> v.getIdVehicle() == vehicle.getIdVehicle());

        if (!alreadyExists) {
            CommandLineEntity commandLine = new CommandLineEntity();
            CommandLineEntityPK commandLinePK = new CommandLineEntityPK();
            commandLinePK.setIdCommand(activeCommand.getIdCommand()); // ✅ ID correct
            commandLinePK.setIdVehicle(vehicle.getIdVehicle());

            commandLine.setId(commandLinePK);
            commandLine.setCommand(activeCommand);
            commandLine.setVehicle(vehicle);

            entityManager.persist(commandLine); // ✅ Ajout en base
            getClient().addToPanier(vehicle); // ✅ Ajout au panier mémoire
            System.out.println(getClient().getPanier().getVehicles());
            JOptionPane.showMessageDialog(this, "Véhicule ajouté au panier !", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ce véhicule est déjà dans votre panier.", "Déjà ajouté", JOptionPane.WARNING_MESSAGE);
        }

        entityManager.getTransaction().commit(); // ✅ Validation de la transaction

        System.out.println("MON PANIER --> " + getClient().getPanier().getVehicles());

        dispose();
        new CatalogForm(getClient());



    }

    private JLabel createImageLabel() {
        try {
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(getClass().getResource(vehicle.getImageUrl())));
            Image resizedImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(resizedImage));
        } catch (IOException e) {
            JLabel fallbackLabel = new JLabel("🚗", SwingConstants.CENTER);
            fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
            return fallbackLabel;
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {}
}
