package frames;

import entity.ClientEntity;
import entity.VehicleEntity;
import service.VehicleService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class CatalogForm extends AbstractFrame {
    private JPanel pnlCatalog;
    private ClientEntity client;

    private JPanel searchPanel;
    private JTextField searchBar;
    private JButton searchButton;
    private VehicleService vehicleService;


    public CatalogForm(ClientEntity client) throws IOException {
        super(client);

        vehicleService = new VehicleService();
        // Initialize pnlCatalog with a proper layout
        pnlCatalog = new JPanel(new BorderLayout());

        initComponents();

        pnlCatalog.setBackground(new Color(5, 231, 255));
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlCatalog, BorderLayout.CENTER);

        this.repaint();
        this.pack();
        this.revalidate();
    }

    private void initComponents() throws IOException {
        // Initialize search panel and its components
        System.out.println(this.getClient());
        searchPanel = new JPanel(new BorderLayout());
        searchBar = new JTextField();
        searchButton = new JButton("Search");
        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Create the panel for vehicle cards with 5 columns and dynamic rows
        JPanel panel = new JPanel(new GridLayout(0, 5, 10, 10)); // 5 columns, dynamic rows, 10px spacing

        // Retrieve the list of vehicles from the service
        for (VehicleEntity vehicle : vehicleService.getUniqueVehicles()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            // Create components for the vehicle card
            JLabel imageLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/images/car.png"))));
            JLabel nameLabel = new JLabel(vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName(), SwingConstants.CENTER);
            JLabel priceLabel = new JLabel(vehicle.getPrice().toString(), SwingConstants.CENTER);
            JButton buyButton = new JButton("Buy");

            // Add ActionListener to the Buy button
            buyButton.addActionListener(e -> {
                ProductForm productForm = null;
                try {
                    productForm = new ProductForm(this.getClient(),vehicle);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
                productForm.setVisible(true);
            });

            // Add components to the card
            card.add(imageLabel, BorderLayout.CENTER);
            card.add(nameLabel, BorderLayout.NORTH);
            card.add(priceLabel, BorderLayout.SOUTH);
            card.add(buyButton, BorderLayout.PAGE_END);

            // Add the card to the grid panel
            panel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(panel); // Enable scrolling if too many vehicles

        // Add components to pnlCatalog
        pnlCatalog.add(scrollPane, BorderLayout.CENTER);
        pnlCatalog.add(searchPanel, BorderLayout.NORTH);
    }


    @Override
    void accountActionPerformed(ActionEvent evt) {
        // Handle account action
    }

    @Override
    void homeActionPerformed(ActionEvent evt) {
        // Handle home action
    }
}
