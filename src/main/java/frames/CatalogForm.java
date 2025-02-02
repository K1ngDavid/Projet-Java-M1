package frames;

import entity.*;
import jakarta.persistence.EntityManager;
import service.CommandService;
import service.VehicleService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.StreamSupport;

public class CatalogForm extends AbstractFrame {
    private JPanel pnlCatalog;
    private VehicleService vehicleService;
    private CommandService commandService;
    private CommandEntity activeCommand; // ✅ Stocke la commande active
    private ClientEntity client;

    public CatalogForm(ClientEntity client) throws IOException {
        super(client);
        this.client = client;
        this.vehicleService = new VehicleService();
        this.commandService = new CommandService();

        pnlCatalog = new JPanel(new BorderLayout());
        initComponents();

        pnlCatalog.setBackground(new Color(5, 231, 255));
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlCatalog, BorderLayout.CENTER);

        // ✅ Charger ou créer une commande "En attente" **seulement si le panier n'est pas vide**
        activeCommand = getExistingCommandOrNull();

        // ✅ Écouteur pour vider le panier à la fermeture de l'application (sans toucher aux commandes en attente)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                client.setPanier(null);// ✅ On vide juste le panier mémoire
                System.out.println("MON PANIER --> " + client.getPanier());
            }
        });

        this.repaint();
        this.pack();
        this.revalidate();
    }

    private void initComponents() throws IOException {
        JPanel panel = new JPanel(new GridLayout(0, 5, 10, 10)); // ✅ 5 colonnes dynamiques

        // ✅ Récupération des véhicules
        for (VehicleEntity vehicle : vehicleService.getUniqueVehicles()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png"))));
            JLabel nameLabel = new JLabel(vehicle.getModel().getModelName(), SwingConstants.CENTER);
            JLabel priceLabel = new JLabel(vehicle.getPrice().toString() + " €", SwingConstants.CENTER);
            JButton buyButton = new JButton("Ajouter au panier");

            // ✅ Ajout du véhicule à la commande "En attente"
            buyButton.addActionListener(e -> addToCart(vehicle));

            card.add(imageLabel, BorderLayout.CENTER);
            card.add(nameLabel, BorderLayout.NORTH);
            card.add(priceLabel, BorderLayout.SOUTH);
            card.add(buyButton, BorderLayout.PAGE_END);

            panel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        pnlCatalog.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {

    }

    /**
     * ✅ Vérifie si une commande en attente existe.
     * 🔹 Si oui, la retourne.
     * 🔹 Sinon, retourne `null` (aucune commande vide ne sera créée inutilement).
     */
    private CommandEntity getExistingCommandOrNull() {
        return commandService.getLastPendingCommand(client);
    }

    /**
     * ✅ Ajoute un véhicule à la commande en attente **ou en crée une nouvelle si nécessaire.**
     */
    private void addToCart(VehicleEntity vehicle) {
        System.out.println("MON PANIER --> " + client.getPanier());
        EntityManager entityManager = commandService.getEntityManager();

        // ✅ Démarrer la transaction (si elle n'est pas déjà active)
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        if (getClient().getPanier().getVehicles().isEmpty()) {
            activeCommand = new CommandEntity();
            activeCommand.setCommandDate(new Date(System.currentTimeMillis()));
            activeCommand.setClient(client);
            activeCommand.setCommandStatus("En attente");
            commandService.createCommand(activeCommand);
        } else if (activeCommand == null){
            try {
                // ✅ Vérifier s'il existe déjà une commande "En attente"
                activeCommand = commandService.getLastPendingCommand(client);

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
                client.addToPanier(vehicle); // ✅ Ajout au panier mémoire

                JOptionPane.showMessageDialog(this, "Véhicule ajouté au panier !", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ce véhicule est déjà dans votre panier.", "Déjà ajouté", JOptionPane.WARNING_MESSAGE);
            }

            entityManager.getTransaction().commit(); // ✅ Validation de la transaction


    }
}
