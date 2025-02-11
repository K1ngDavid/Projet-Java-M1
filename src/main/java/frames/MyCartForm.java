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
import java.util.List;

public class MyCartForm extends AbstractFrame {
    private JPanel pnlCart;
    private JLabel lblTitre;
    private JButton btnSaveCart, btnCancelAll, btnPayAll;
    private CommandService commandService;

    public MyCartForm(ClientEntity client) throws IOException {
        super(client);
        this.commandService = new CommandService();
        System.out.println(client.getPanier());
        System.out.println(client.getPanier().getVehicles());
        initComponents();
        loadPendingCommands(); // Charger les commandes en attente
    }

    /**
     * Initialise les composants graphiques et organise la mise en page.
     */
    private void initComponents() throws IOException {
        // Utilisation d'un BorderLayout sur pnlRoot (hérité d'AbstractFrame)
        pnlRoot.setLayout(new BorderLayout(20, 20));
        pnlRoot.setBackground(new Color(245, 245, 245));

        // Titre
        lblTitre = new JLabel("🛒 Mon Panier & Paiement", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(new Color(33, 33, 33));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        pnlRoot.add(lblTitre, BorderLayout.NORTH);

        // Panneau des commandes
        pnlCart = new JPanel();
        pnlCart.setLayout(new BoxLayout(pnlCart, BoxLayout.Y_AXIS));
        pnlCart.setBackground(Color.WHITE);
        pnlCart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(pnlCart);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnlRoot.add(scrollPane, BorderLayout.CENTER);

        // Panneau des boutons d'action global
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBottom.setBackground(new Color(245, 245, 245));

        btnSaveCart = createStyledButton("💾 Sauvegarder Panier");
        btnCancelAll = createStyledButton("🗑️ Annuler Toutes les Commandes");
        btnPayAll = createStyledButton("🛍 Acheter Toutes les Commandes");

        btnSaveCart.addActionListener(e -> saveCart());
        btnCancelAll.addActionListener(e -> cancelAllCommands());
        btnPayAll.addActionListener(e -> payAllCommands());

        pnlBottom.add(btnSaveCart);
        pnlBottom.add(btnCancelAll);
        pnlBottom.add(btnPayAll);
        pnlRoot.add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * Crée un bouton stylisé avec une police moderne et des couleurs cohérentes.
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

    /**
     * Charge et affiche les commandes en attente dans le panneau pnlCart.
     */
    private void loadPendingCommands() {
        pnlCart.removeAll(); // Nettoyer l'affichage
        List<CommandEntity> commandes = commandService.getPendingCommands(getClient());

        if (commandes.isEmpty()) {
            JLabel lblNoCommands = new JLabel("Aucune commande en attente.", SwingConstants.CENTER);
            lblNoCommands.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblNoCommands.setForeground(Color.GRAY);
            pnlCart.add(lblNoCommands);
        } else {
            for (CommandEntity commande : commandes) {
                pnlCart.add(createCommandPanel(commande));
                pnlCart.add(Box.createVerticalStrut(15)); // Espacement entre commandes
            }
        }
        pnlCart.revalidate();
        pnlCart.repaint();
    }

    /**
     * Crée un panneau pour afficher une commande et ses véhicules associés.
     */
    private JPanel createCommandPanel(CommandEntity commande) {
        JPanel panelCommand = new JPanel(new BorderLayout(10, 10));
        panelCommand.setBackground(Color.WHITE);
        panelCommand.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // En-tête de la commande (Date et Statut)
        JLabel lblCommandInfo = new JLabel("📅 " + commande.getCommandDate() + " - Statut : " + commande.getCommandStatus(), SwingConstants.CENTER);
        lblCommandInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCommandInfo.setForeground(new Color(33, 33, 33));
        panelCommand.add(lblCommandInfo, BorderLayout.NORTH);

        // Liste des véhicules dans une grille (3 colonnes)
        JPanel pnlVehicles = new JPanel(new GridLayout(0, 3, 10, 10));
        pnlVehicles.setBackground(Color.WHITE);
        for (CommandLineEntity commandLine : commande.getCommandLines()) {
            VehicleEntity vehicle = commandLine.getVehicle();
            pnlVehicles.add(createVehicleCard(vehicle));
        }
        panelCommand.add(pnlVehicles, BorderLayout.CENTER);

        // Panneau d'actions pour la commande
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlActions.setBackground(Color.WHITE);
        JButton btnCancel = createStyledButton("🗑️ Annuler");
        btnCancel.setBackground(new Color(220, 53, 69)); // rouge
        JButton btnPay = createStyledButton("💳 Payer");
        btnPay.setBackground(new Color(40, 167, 69)); // vert

        btnCancel.addActionListener(e -> cancelCommand(commande));
        btnPay.addActionListener(e -> payCommand(commande));

        pnlActions.add(btnCancel);
        pnlActions.add(btnPay);
        panelCommand.add(pnlActions, BorderLayout.SOUTH);

        return panelCommand;
    }

    /**
     * Crée une carte pour afficher un véhicule dans une commande.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Définition de la taille souhaitée pour l'image miniature
        int thumbWidth = 150;
        int thumbHeight = 150;

        // Chargement de l'image via getResource()
        URL imgURL = getClass().getResource(vehicle.getImageUrl());
        ImageIcon icon;
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        } else {
            // En cas d'absence d'image, créer un placeholder
            BufferedImage placeholder = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, thumbWidth, thumbHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawString("No Image", thumbWidth / 2 - 30, thumbHeight / 2);
            g2d.dispose();
            icon = new ImageIcon(placeholder);
        }
        // Redimensionnement de l'image en miniature
        Image scaledImage = icon.getImage().getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);

        // Création des labels pour le nom et le prix
        JLabel nameLabel = new JLabel("🚗 " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(priceLabel, BorderLayout.SOUTH);

        return card;
    }


    /**
     * Sauvegarde le panier dans une commande "En attente".
     */
    private void saveCart() {
        if (this.getClient().getPanier() == null) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide !", "Alerte", JOptionPane.WARNING_MESSAGE);
            return;
        }
        EntityManager entityManager = commandService.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Vérifier s'il existe déjà une commande "En attente"
            CommandEntity existingCommand = commandService.getPendingCommand(getClient());
            if (existingCommand == null) {
                existingCommand = new CommandEntity();
                existingCommand.setClient(this.getClient());
                existingCommand.setCommandStatus("En attente");
                existingCommand.setCommandDate(new Date(System.currentTimeMillis()));
                entityManager.persist(existingCommand);
                entityManager.flush();
            }

            for (VehicleEntity vehicle : this.getClient().getPanier().getVehicles()) {
                boolean alreadyExists = existingCommand.getVehicles().stream()
                        .anyMatch(v -> v.getIdVehicle() == vehicle.getIdVehicle());
                if (!alreadyExists) {
                    CommandLineEntity commandLine = new CommandLineEntity();
                    commandLine.setCommand(existingCommand);
                    commandLine.setVehicle(vehicle);
                    entityManager.persist(commandLine);
                }
            }

            entityManager.getTransaction().commit();
            getClient().setPanier(null);
            loadPendingCommands();
            JOptionPane.showMessageDialog(this, "Panier sauvegardé avec succès !", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Annule une commande spécifique.
     */
    private void cancelCommand(CommandEntity commande) {
        commandService.deleteCommand(commande);
        if(getClient().getPanier() != null && getClient().getPanier().getIdCommand() == commande.getIdCommand()){
            getClient().setPanier(null);
        }
        loadPendingCommands();
    }

    /**
     * Effectue le paiement d'une commande spécifique.
     */
    private void payCommand(CommandEntity commande) {
        new PaymentForm(getClient(), List.of(commande)).setVisible(true);
        dispose();
        System.out.println(commande.isPending());
        loadPendingCommands();
    }

    /**
     * Ouvre la fenêtre de paiement avec toutes les commandes en attente.
     */
    private void payAllCommands() {
        List<CommandEntity> pendingCommands = commandService.getPendingCommands(getClient());
        if (pendingCommands.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune commande en attente de paiement.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new PaymentForm(getClient(), pendingCommands).setVisible(true);
        dispose();
        pnlCart.revalidate();
        pnlCart.repaint();
    }

    /**
     * Annule toutes les commandes.
     */
    private void cancelAllCommands() {
        commandService.getPendingCommands(getClient()).forEach(commandService::deleteCommand);
        getClient().setPanier(null);
        loadPendingCommands();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Implémentez selon vos besoins
    }
}
