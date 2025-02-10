package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class AccountForm extends AbstractFrame {
    private JPanel pnlAccount;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField creditCardField;
    private JPasswordField cveField;
    private JButton updateButton;
    private JLabel lblError;
    private JLabel lblSuccess;

    private ClientService clientService;

    public AccountForm(ClientEntity client) {
        super(client);
        this.clientService = new ClientService();
        initComponents();
    }

    private void initComponents() {
        // Création du panneau principal avec GridBagLayout
        pnlAccount = new JPanel(new GridBagLayout());
        pnlAccount.setBackground(Color.WHITE);
        pnlAccount.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Titre
        JLabel lblTitle = new JLabel("📝 Mon Compte");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlAccount.add(lblTitle, gbc);

        // Réinitialiser gridwidth pour les champs
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Nom
        pnlAccount.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setText(getClient().getName());
        pnlAccount.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setText(getClient().getEmail());
        pnlAccount.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setText(getClient().getPassword());
        pnlAccount.add(passwordField, gbc);

        // Numéro de carte
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Numéro de carte :"), gbc);
        gbc.gridx = 1;
        creditCardField = new JPasswordField(20);
        creditCardField.setText(getClient().getCreditCardNumber());
        pnlAccount.add(creditCardField, gbc);

        // CVV
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("CVV :"), gbc);
        gbc.gridx = 1;
        cveField = new JPasswordField(5);
        cveField.setText(getClient().getCveNumber());
        pnlAccount.add(cveField, gbc);

        // Zone d'erreur
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        pnlAccount.add(lblError, gbc);

        // Zone de succès
        gbc.gridy++;
        lblSuccess = new JLabel("");
        lblSuccess.setForeground(new Color(0, 150, 0));
        pnlAccount.add(lblSuccess, gbc);

        // Bouton de mise à jour
        gbc.gridy++;
        updateButton = new JButton("Mettre à jour");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateButton.setBackground(new Color(76, 175, 80));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlAccount.add(updateButton, gbc);

        // Action du bouton
        updateButton.addActionListener((ActionEvent e) -> updateClientInfo());

        // Ajout du panneau principal dans pnlRoot (hérité d'AbstractFrame)
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlAccount, BorderLayout.CENTER);
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {

    }

    /**
     * Vérifie les champs et met à jour les informations du client en base.
     */
    private void updateClientInfo() {
        try {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                throw new Exception("Tous les champs doivent être remplis !");
            }

            // Vérifier si l'email a changé et s'il existe déjà
            if (!newEmail.equals(getClient().getEmail()) && clientService.isEmailAlreadyExists(newEmail)) {
                throw new Exception("L'email est déjà utilisé par un autre utilisateur !");
            }

            // Mise à jour des informations
            getClient().setName(newName);
            getClient().setEmail(newEmail);
            getClient().setPassword(newPassword);
            if (creditCardField.getPassword().length > 0) {
                getClient().setCreditCardNumber(new String(creditCardField.getPassword()));
            }
            if (cveField.getPassword().length > 0) {
                getClient().setCveNumber(new String(cveField.getPassword()));
            }

            clientService.updateClient(getClient());

            lblError.setText("");
            lblSuccess.setText("Mise à jour réussie !");
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
            lblSuccess.setText("");
        }
    }
}
