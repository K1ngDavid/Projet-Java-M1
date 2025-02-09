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
import java.util.List;

public class MyCartForm extends AbstractFrame {
    private JPanel pnlCart;
    private JLabel lblTitre;
    private JButton btnSaveCart, btnCancelAll, btnPayAll;
    private CommandService commandService;

    public MyCartForm(ClientEntity client) throws IOException {
        super(client);
        this.commandService = new CommandService();
        System.out.println(client.getPanier().getVehicles());
        initComponents();
        loadPendingCommands(); // 🔥 Charger les commandes en attente
    }

    /**
     * ✅ Initialise les composants graphiques.
     */
    private void initComponents() throws IOException {
        pnlRoot.setLayout(new BorderLayout());

        // ✅ Titre
        lblTitre = new JLabel("🛒 Mon Panier & Paiement", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlRoot.add(lblTitre, BorderLayout.NORTH);

        // ✅ Panneau des commandes
        pnlCart = new JPanel();
        pnlCart.setLayout(new BoxLayout(pnlCart, BoxLayout.Y_AXIS));

        pnlRoot.add(new JScrollPane(pnlCart), BorderLayout.CENTER);

        // ✅ Panneau des boutons principaux
        JPanel pnlBottom = new JPanel(new FlowLayout());

        btnSaveCart = createStyledButton("💾 Sauvegarder Panier");
        btnCancelAll = createStyledButton("🗑️ Annuler Toutes les Commandes");
        btnPayAll = createStyledButton("🛍 Acheter Toutes les Commandes"); // 🔥 Nouveau bouton

        btnSaveCart.addActionListener(e -> saveCart());
        btnCancelAll.addActionListener(e -> cancelAllCommands());
        btnPayAll.addActionListener(e -> payAllCommands()); // 🔥 Action pour acheter tout

        pnlBottom.add(btnSaveCart);
        pnlBottom.add(btnCancelAll);
        pnlBottom.add(btnPayAll); // 🔥 Ajout du nouveau bouton

        pnlRoot.add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * ✅ Crée un bouton stylisé.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * 🔥 Charge et affiche les commandes en attente.
     */
    private void loadPendingCommands() {
        pnlCart.removeAll(); // ✅ Nettoyer l'affichage
        List<CommandEntity> commandes = commandService.getPendingCommands(getClient());


        if (commandes.isEmpty()) {
            JLabel lblNoCommands = new JLabel("Aucune commande en attente.", SwingConstants.CENTER);
            lblNoCommands.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblNoCommands.setForeground(Color.GRAY);
            pnlCart.add(lblNoCommands);
        }

        for (CommandEntity commande : commandes) {
            System.out.println(commande.getVehicles());
            pnlCart.add(createCommandPanel(commande));
        }

        pnlCart.revalidate();
        pnlCart.repaint();
    }

    /**
     * ✅ Crée un panneau pour une commande avec ses véhicules associés.
     */
    private JPanel createCommandPanel(CommandEntity commande) {
        JPanel panelCommand = new JPanel();
        panelCommand.setLayout(new BorderLayout());
        panelCommand.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // ✅ En-tête de la commande (Date + Statut)
        JLabel lblCommandInfo = new JLabel("📅 " + commande.getCommandDate() + " - Statut : " + commande.getCommandStatus());
        lblCommandInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCommandInfo.setHorizontalAlignment(SwingConstants.CENTER);
        panelCommand.add(lblCommandInfo, BorderLayout.NORTH);

        // ✅ Liste des véhicules
        JPanel pnlVehicles = new JPanel(new GridLayout(0, 3, 10, 10));
//        System.out.println("HELLLOOOOO");
//        VehicleEntity vehicle1 = commande.getCommandLines().get(0).getVehicle();
//        System.out.println(vehicle1);


        for (CommandLineEntity commandLine : commande.getCommandLines()) {
            VehicleEntity vehicle = commandLine.getVehicle();
//            System.out.println(vehicle);
            pnlVehicles.add(createVehicleCard(vehicle));
        }

        panelCommand.add(pnlVehicles, BorderLayout.CENTER);

        // ✅ Boutons d'action
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancel = createStyledButton("🗑️ Annuler");
        JButton btnPay = createStyledButton("💳 Payer");

        btnCancel.setBackground(Color.RED);
        btnCancel.addActionListener(e -> cancelCommand(commande));

        btnPay.addActionListener(e -> payCommand(commande));

        pnlActions.add(btnCancel);
        pnlActions.add(btnPay);
        panelCommand.add(pnlActions, BorderLayout.SOUTH);

        return panelCommand;
    }

    /**
     * ✅ Crée une carte pour afficher un véhicule dans une commande.
     */
    private JPanel createVehicleCard(VehicleEntity vehicle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel imageLabel = new JLabel(new ImageIcon(getClass().getResource(vehicle.getImageUrl())));
        JLabel nameLabel = new JLabel("🚗 "  + vehicle.getModel().getModelName(), SwingConstants.CENTER);
        JLabel priceLabel = new JLabel("💰 " + vehicle.getPrice().toString() + " €", SwingConstants.CENTER);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(priceLabel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * ✅ Sauvegarde le panier dans une commande "En attente".
     */
    private void saveCart() {
        if (this.getClient().getPanier() == null) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide !", "Alerte", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EntityManager entityManager = commandService.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            // ✅ Vérifier s'il existe déjà une commande "En attente"
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
     * ✅ Annule une commande spécifique.
     */
    private void cancelCommand(CommandEntity commande) {
        commandService.deleteCommand(commande);
        if(getClient().getPanier().getIdCommand() == commande.getIdCommand()) getClient().setPanier(null);
        loadPendingCommands();
    }

    /**
     * ✅ Effectue le paiement d'une commande spécifique.
     */
    /**
     * ✅ Ouvre la fenêtre de paiement avec une commande spécifique.
     */
    private void payCommand(CommandEntity commande) {
        new PaymentForm(getClient(), List.of(commande)).setVisible(true);
        dispose();
        System.out.println(commande.isPending());
        loadPendingCommands();
    }

    /**
     * ✅ Ouvre la fenêtre de paiement avec toutes les commandes en attente.
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
     * ✅ Annule toutes les commandes.
     */
    private void cancelAllCommands() {
        commandService.getPendingCommands(getClient()).forEach(commandService::deleteCommand);
        getClient().setPanier(null);
        loadPendingCommands();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
    }
}
