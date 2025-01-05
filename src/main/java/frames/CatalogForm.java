package frames;

import entity.ClientEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class CatalogForm extends AbstractFrame{
    private JPanel pnlCatalog;
    private ClientEntity client;

    public CatalogForm(ClientEntity client) throws IOException {
        super();
        this.client = client;
        initComponents();

        pnlCatalog.setBackground(new java.awt.Color(5, 231, 255));
        pnlCatalog.setLayout(new BorderLayout());
        pnlRoot.add(pnlCatalog);
        this.repaint();
        this.pack();
        this.revalidate();
    }


    private void initComponents() throws IOException {

        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 colonnes, espacement de 10px
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Exemple de véhicules
        String[] vehicleNames = {"Tesla Model S", "Ducati Panigale", "BMW X5"};
        String[] vehiclePrices = {"75,000 €", "20,000 €", "50,000 €"};
        String[] vehicleImages = {"car.jpg", "car.jpg", "car.jpg"};

        for (int i = 0; i < vehicleNames.length; i++) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png" ))));
            JLabel nameLabel = new JLabel(vehicleNames[i], SwingConstants.CENTER);
            JLabel priceLabel = new JLabel(vehiclePrices[i], SwingConstants.CENTER);
            JButton buyButton = new JButton("Acheter");

            card.add(imageLabel, BorderLayout.CENTER);
            card.add(nameLabel, BorderLayout.NORTH);
            card.add(priceLabel, BorderLayout.SOUTH);
            card.add(buyButton, BorderLayout.PAGE_END);

            panel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(panel); // Permet le défilement si trop de véhicules
        pnlRoot.add(scrollPane);
        pnlRoot.revalidate();
        pnlRoot.repaint();
        this.pack();
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {

    }

    @Override
    void homeActionPerformed(ActionEvent evt) {

    }
}
