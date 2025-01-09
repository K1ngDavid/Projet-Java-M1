package frames;

import entity.ClientEntity;
import entity.VehicleEntity; // Assurez-vous que cette classe existe et représente une voiture.

import javax.swing.*;
import javax.swing.text.GapContent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HomeForm extends AbstractFrame {

    public HomeForm(ClientEntity client) {
        super(client);
        initComponents();
        this.pack();
    }

    private void initComponents() {
        pnlRoot.setBackground(new java.awt.Color(210, 231, 255));

        JLabel jLabel1 = new JLabel("Home");
        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", Font.BOLD, 24));
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));

        // Panel pour "Mes catégories"
        JPanel panelCategories = new JPanel();
        panelCategories.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel jLabel2 = new JLabel("Mes catégories");
        panelCategories.add(jLabel2);

        // Panel pour "Mes commandes"
        JPanel panelCommandes = new JPanel();
        panelCommandes.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel jLabel3 = new JLabel("Mes commandes");
        panelCommandes.add(jLabel3);

        // Panel pour "Mes voitures"
        JPanel panelVoitures = new JPanel();
        panelVoitures.setBorder(BorderFactory.createLineBorder(Color.black));
        panelVoitures.setLayout(new BoxLayout(panelVoitures, BoxLayout.Y_AXIS));
        JLabel jLabel6 = new JLabel("Mes voitures");
        panelVoitures.add(jLabel6);

        // Ajouter les voitures sous "Mes voitures"
        displayVoitures(panelVoitures);

        // Panel pour "Mes dépenses"
        JPanel panelDepenses = new JPanel();
        panelDepenses.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel jLabel7 = new JLabel("Mes dépenses");
        panelDepenses.add(jLabel7);

        GroupLayout mainPanelLayout = new GroupLayout(pnlRoot);
        pnlRoot.setLayout(mainPanelLayout);

        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createSequentialGroup()
                        .addContainerGap(20, 100) // Gap gauche
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1) // Titre
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(panelCategories, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panelVoitures, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Espace flexible
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(panelDepenses, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panelCommandes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                )
                        )
                        .addContainerGap(20, Short.MAX_VALUE) // Gap droit
        );

        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createSequentialGroup()
                        .addGap(23) // Espace initial en haut
                        .addComponent(jLabel1) // Titre
                        .addGap(18) // Espace entre le titre et le premier groupe
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelCategories) // Panel "Mes catégories"
                                .addComponent(panelDepenses) // Panel "Mes dépenses"
                        )
                        .addGap(20) // Espace fixe
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(panelVoitures) // Panel "Mes voitures"
                                .addComponent(panelCommandes)) // Panel "Mes commandes"
                        .addGap(20) // Espace en bas
        );
    }

    /**
     * Affiche les voitures dans un panneau vertical.
     *
     * @param panelVoitures Le panneau dans lequel afficher les voitures.
     */
    private void displayVoitures(JPanel panelVoitures) {
        List<VehicleEntity> voitures = this.getClient().getVehicles();
        if (voitures != null && !voitures.isEmpty()) {
            for (VehicleEntity voiture : voitures) {
                JLabel voitureLabel = new JLabel(voiture.getModel().getBrandName() + " - " + voiture.getModel().getModelName());
                voitureLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
                panelVoitures.add(voitureLabel);
            }
        } else {
            JLabel noVoituresLabel = new JLabel("Aucune voiture disponible.");
            noVoituresLabel.setForeground(Color.red);
            panelVoitures.add(noVoituresLabel);
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        AccountForm accountForm = new AccountForm(this.getClient());
        accountForm.setVisible(true);
    }

    @Override
    void homeActionPerformed(ActionEvent evt) {
        // Action déjà sur Home
    }
}
