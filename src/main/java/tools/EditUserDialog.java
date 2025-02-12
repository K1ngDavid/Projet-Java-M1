package tools;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import java.awt.*;

public class EditUserDialog extends JDialog {

    private ClientEntity client;
    private ClientService clientService;

    private JTextField tfName;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfPostalAddress;
    private JTextField tfCreditCard;
    private JTextField tfCve;
    private JPasswordField pfPassword;
    private JTextField tfRole;

    public EditUserDialog(Frame owner, ClientEntity client) {
        super(owner, "Modifier le compte", true);
        this.client = client;
        this.clientService = new ClientService(); // Assurez-vous que ClientService possède updateClient()
        initComponents();
        populateFields();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Nom
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Nom :"), gbc);
        tfName = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfName, gbc);
        row++;

        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Téléphone :"), gbc);
        tfPhone = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfPhone, gbc);
        row++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Email :"), gbc);
        tfEmail = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfEmail, gbc);
        row++;

        // Adresse postale
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Adresse postale :"), gbc);
        tfPostalAddress = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfPostalAddress, gbc);
        row++;

        // Numéro de carte bancaire
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Carte bancaire :"), gbc);
        tfCreditCard = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfCreditCard, gbc);
        row++;

        // CVE
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("CVE :"), gbc);
        tfCve = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfCve, gbc);
        row++;

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Mot de passe :"), gbc);
        pfPassword = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(pfPassword, gbc);
        row++;

        // Rôle
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Rôle :"), gbc);
        tfRole = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfRole, gbc);
        row++;

        add(formPanel, BorderLayout.CENTER);

        // Boutons d'action
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // Actions des boutons
        btnSave.addActionListener(e -> saveChanges());
        btnCancel.addActionListener(e -> dispose());
    }

    private void populateFields() {
        tfName.setText(client.getName());
        tfPhone.setText(client.getPhoneNumber());
        tfEmail.setText(client.getEmail());
        tfPostalAddress.setText(client.getPostalAddress());
        tfCreditCard.setText(client.getCreditCardNumber());
        tfCve.setText(client.getCveNumber());
        pfPassword.setText(client.getPassword());
        tfRole.setText(client.getRole().toString());
    }

    private void saveChanges() {
        try {
            // Récupération des valeurs des champs
            String name = tfName.getText().trim();
            String phone = tfPhone.getText().trim();
            String email = tfEmail.getText().trim();
            String postal = tfPostalAddress.getText().trim();
            String creditCard = tfCreditCard.getText().trim();
            String cve = tfCve.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();
            String roleStr = tfRole.getText().trim();

            // Vérification de l'email
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "⚠ Email invalide!\n",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification du mot de passe
            if (!password.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$")) {
                JOptionPane.showMessageDialog(this, "⚠ Mot de passe invalide !\n" +
                                "Le mot de passe doit contenir au moins 8 caractères, une lettre minuscule, un chiffre et un caractère spécial.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mise à jour de l'objet client
            client.setName(name);
            client.setPhoneNumber(phone);
            client.setEmail(email);
            client.setPostalAddress(postal);
            client.setCreditCardNumber(creditCard);
            client.setCveNumber(cve);
            client.setPassword(password);

            // Vérification et affectation du rôle
            try {
                client.setRole(ClientEntity.Role.valueOf(roleStr.toUpperCase()));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "⚠ Rôle invalide !\nLes rôles possibles sont : CLIENT, ADMIN.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Appel du service pour mettre à jour le client
            clientService.updateClient(client);

            JOptionPane.showMessageDialog(this, "Compte modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du compte : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
