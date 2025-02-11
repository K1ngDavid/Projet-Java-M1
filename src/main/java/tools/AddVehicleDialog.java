package tools;

import entity.*;
import enumerations.PowerSource;
import enumerations.TransmissionType;
import enumerations.VehicleType;
import service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

public class AddVehicleDialog extends JDialog {
    private JTextField txtModelName;
    private JTextField txtBrandName;
    private JTextField txtPrice;
    private JTextField txtCountry;
    private JTextField txtImageUrl;
    private JTextField txtStatus;
    private JTextField txtNumberOfDoors;
    private JTextField txtHorsePower;
    private JComboBox<PowerSource> cbPowerSource;
    private JComboBox<TransmissionType> cbTransmissionType;
    // Nouveau champ pour le type de véhicule
    private JComboBox<VehicleType> cbVehicleType;

    private JButton btnSave;
    private JButton btnCancel;

    private VehicleService vehicleService;

    public AddVehicleDialog(Frame owner) {
        super(owner, "Ajouter un nouveau véhicule", true);
        vehicleService = new VehicleService();
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        pnlContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlContent.setBackground(Color.WHITE);

        // Panel des champs répartis en deux colonnes
        JPanel pnlFields = new JPanel(new GridLayout(0, 2, 10, 10));
        pnlFields.setBackground(Color.WHITE);

        // Colonne 1
        pnlFields.add(new JLabel("Modèle:"));
        txtModelName = new JTextField(20);
        pnlFields.add(txtModelName);

        pnlFields.add(new JLabel("Marque:"));
        txtBrandName = new JTextField(20);
        pnlFields.add(txtBrandName);

        pnlFields.add(new JLabel("Prix (€):"));
        txtPrice = new JTextField(10);
        pnlFields.add(txtPrice);

        pnlFields.add(new JLabel("Pays d'origine:"));
        txtCountry = new JTextField(20);
        pnlFields.add(txtCountry);

        pnlFields.add(new JLabel("Image URL:"));
        txtImageUrl = new JTextField(30);
        pnlFields.add(txtImageUrl);

        // Colonne 2
        pnlFields.add(new JLabel("Status:"));
        txtStatus = new JTextField(15);
        pnlFields.add(txtStatus);

        pnlFields.add(new JLabel("Nombre de portes:"));
        txtNumberOfDoors = new JTextField(5);
        pnlFields.add(txtNumberOfDoors);

        pnlFields.add(new JLabel("Puissance (HP):"));
        txtHorsePower = new JTextField(5);
        pnlFields.add(txtHorsePower);

        pnlFields.add(new JLabel("Source d'énergie:"));
        cbPowerSource = new JComboBox<>(PowerSource.values());
        pnlFields.add(cbPowerSource);

        pnlFields.add(new JLabel("Transmission:"));
        cbTransmissionType = new JComboBox<>(TransmissionType.values());
        pnlFields.add(cbTransmissionType);

        // Nouvelle ligne pour le type de véhicule
        pnlFields.add(new JLabel("Type de véhicule:"));
        cbVehicleType = new JComboBox<>(VehicleType.values());
        pnlFields.add(cbVehicleType);

        pnlContent.add(pnlFields, BorderLayout.CENTER);

        // Panel des boutons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnSave = new JButton("Enregistrer");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> saveVehicle());

        btnCancel = new JButton("Annuler");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnSave);

        pnlContent.add(pnlButtons, BorderLayout.SOUTH);

        setContentPane(pnlContent);
    }

    private void saveVehicle() {
        try {
            // Récupération et validation des champs
            String modelName = txtModelName.getText().trim();
            String brandName = txtBrandName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String country = txtCountry.getText().trim();
            String imageUrl = txtImageUrl.getText().trim();
            String status = txtStatus.getText().trim();
            String doorsStr = txtNumberOfDoors.getText().trim();
            String horsePowerStr = txtHorsePower.getText().trim();
            PowerSource powerSource = (PowerSource) cbPowerSource.getSelectedItem();
            TransmissionType transmissionType = (TransmissionType) cbTransmissionType.getSelectedItem();
            VehicleType vehicleType = (VehicleType) cbVehicleType.getSelectedItem();

            if (modelName.isEmpty() || brandName.isEmpty() || priceStr.isEmpty() || country.isEmpty() ||
                    imageUrl.isEmpty() || status.isEmpty() || doorsStr.isEmpty() || horsePowerStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal price = new BigDecimal(priceStr);
            int numberOfDoors = Integer.parseInt(doorsStr);
            int horsePower = Integer.parseInt(horsePowerStr);

            // Créer le modèle du véhicule
            ModelEntity model = new ModelEntity();
            model.setModelName(modelName);
            model.setBrandName(brandName);

            // Instancier le véhicule selon le type sélectionné
            VehicleEntity newVehicle;
            switch(vehicleType) {
                case CAR:
                    newVehicle = new CarEntity(); // Assurez-vous que CarEntity étend VehicleEntity et est annotée correctement
                    break;
                case MOTORCYCLE:
                    newVehicle = new MotorcycleEntity();
                    break;
                case VAN:
                    newVehicle = new VanEntity();
                    break;
                case OTHER:
                default:
                    // Vous pouvez choisir une implémentation par défaut
                    newVehicle = new CarEntity();
                    break;
            }

            // Affecter les valeurs au véhicule
            newVehicle.setModel(model);
            newVehicle.setPrice(price);
            newVehicle.setCountryOfOrigin(country);
            newVehicle.setImageUrl(imageUrl);
            newVehicle.setStatus(status);
            newVehicle.setNumberOfDoors(numberOfDoors);
            newVehicle.setHorsePower(horsePower);
            newVehicle.setVehiclePowerSource(powerSource);
            newVehicle.setTransmissionType(transmissionType);

            // Appeler le service pour persister le véhicule
            boolean success = vehicleService.addVehicle(newVehicle);
            if (success) {
                JOptionPane.showMessageDialog(this, "Véhicule ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du véhicule.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
