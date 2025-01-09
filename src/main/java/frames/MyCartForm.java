package frames;

import entity.ClientEntity;
import entity.VehicleEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MyCartForm extends AbstractFrame{
    private JPanel pnlCart;
    private JLabel lblTitre;

    public MyCartForm(ClientEntity client) throws IOException {
        super(client);
        initComponents();
        pnlRoot.add(lblTitre);
        pnlRoot.add(pnlCart);
    }


    private void initComponents() throws IOException {
        // Initialisation de pnlCart avec un layout approprié
        pnlCart = new JPanel(new BorderLayout());

        // Création du panneau de grille
        JPanel panel = new JPanel(new GridLayout(0, 5, 10, 10)); // 5 colonnes, espacement de 10px

        // Ajout des cartes de véhicules
        for (VehicleEntity vehicle : this.getClient().getPanier()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            // Création des composants pour chaque carte
            JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png"))));
            JLabel nameLabel = new JLabel(vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
            JLabel priceLabel = new JLabel(vehicle.getPrice().toString(), SwingConstants.CENTER);
            JButton deleteButton = new JButton("Supprimer");

            // ActionListener pour le bouton "Buy"
            deleteButton.addActionListener(e -> {
                this.getClient().getPanier().remove(vehicle);
                dispose();
                try {
                    MyCartForm myCartForm = new MyCartForm(this.getClient());
                    myCartForm.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            // Ajout des composants à la carte
            card.add(imageLabel, BorderLayout.CENTER);
            card.add(nameLabel, BorderLayout.NORTH);
            card.add(priceLabel, BorderLayout.SOUTH);
            card.add(deleteButton, BorderLayout.PAGE_END);

            // Ajout de la carte au panneau de grille
            panel.add(card);
        }

        // Ajout du panneau de grille au panneau principal
        pnlCart.add(panel, BorderLayout.CENTER);
    }


    @Override
    void accountActionPerformed(ActionEvent evt) {

    }

    @Override
    void homeActionPerformed(ActionEvent evt) {

    }
}
