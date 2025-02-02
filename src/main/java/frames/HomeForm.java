package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import service.CommandService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HomeForm extends AbstractFrame {

    private CommandService commandService;
    public HomeForm(ClientEntity client) {
        super(client); // Appelle le constructeur parent pour initialiser le client
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null !");
        }
        commandService = new CommandService();
        initComponents();
        this.pack();
        System.out.println(client.getIdClient());
    }

    private void initComponents() {
        pnlRoot.setBackground(new Color(210, 231, 255));

        JLabel jLabel1 = new JLabel("Home");
        jLabel1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        jLabel1.setForeground(new Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        // Border pour les panels
        Border lineBorder1 = BorderFactory.createLineBorder(Color.BLACK);

        // 🔥 Création des panneaux sans taille fixe pour permettre l'expansion
        JPanel panelCategories = createPanel("Mes catégories", lineBorder1);
        JPanel panelCommandes = createPanel("Mes commandes", lineBorder1);
        displayCommandes(panelCommandes); // Remplit le panneau des commandes
        JPanel panelVoitures = createPanel("Mes voitures", lineBorder1);
        displayVoitures(panelVoitures); // Remplit le panneau des voitures
        JPanel panelDepenses = createPanel("Mes dépenses", lineBorder1);

        // Utilisation de GroupLayout pour une meilleure responsivité
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
                                        .addComponent(panelCategories, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelVoitures, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                )
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(panelDepenses, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelCommandes, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                )
                                .addContainerGap(20, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(30)
                        .addComponent(jLabel1)
                        .addGap(30)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelCategories, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addComponent(panelDepenses, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        )
                        .addGap(30)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelVoitures, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                                .addComponent(panelCommandes, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        )
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
     * 🔥 Affiche les voitures sous forme de liste dans un panneau vertical.
     */
    private void displayVoitures(JPanel panelVoitures) {
        panelVoitures.setLayout(new BoxLayout(panelVoitures, BoxLayout.Y_AXIS));
        ClientEntity client = this.getClient(); // Récupère le client

        // Vérification de nullité du client
        if (client == null) {
            JLabel errorLabel = new JLabel("Erreur : client non initialisé.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelVoitures.add(errorLabel);
            return;
        }

        List<VehicleEntity> voitures = client.getVehicles();
        if (voitures != null && !voitures.isEmpty()) {
            for (VehicleEntity voiture : voitures) {
                String brandName = (voiture.getModel() != null) ? voiture.getModel().getBrandName() : "Marque inconnue";
                String modelName = (voiture.getModel() != null) ? voiture.getModel().getModelName() : "Modèle inconnu";

                JLabel voitureLabel = new JLabel(brandName + " - " + modelName);
                voitureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelVoitures.add(voitureLabel);
            }
        } else {
            JLabel noVoituresLabel = new JLabel("<html>Aucune voiture<br>disponible.</html>");
            noVoituresLabel.setForeground(Color.RED);
            noVoituresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelVoitures.add(noVoituresLabel);
        }
    }

    /**
     * 🔥 Affiche les commandes sous forme de liste dans un panneau vertical.
     */
    private void displayCommandes(JPanel panelCommandes) {
        panelCommandes.setLayout(new BoxLayout(panelCommandes, BoxLayout.Y_AXIS));
        ClientEntity client = this.getClient();

        if (client == null) {
            JLabel errorLabel = new JLabel("Erreur : client non initialisé.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCommandes.add(errorLabel);
            return;
        }

        List<CommandEntity> commandes = commandService.getPaidCommands(client);
        if (commandes != null && !commandes.isEmpty()) {
            for (CommandEntity commande : commandes) {
                JLabel commandeLabel = new JLabel(
                        "<html>Commande ID: " + commande.getIdCommand() +
                                "<br>Status: " + commande.getCommandStatus() +
                                "<br>Date: " + commande.getCommandDate() + "</html>"
                );
                commandeLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                commandeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelCommandes.add(commandeLabel);
            }
        } else {
            JLabel noCommandesLabel = new JLabel("Aucune commande disponible.");
            noCommandesLabel.setForeground(Color.RED);
            noCommandesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCommandes.add(noCommandesLabel);
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(this.getClient()).setVisible(true);
    }
}
