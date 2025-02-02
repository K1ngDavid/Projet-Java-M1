package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountForm extends AbstractFrame {
    private JPanel pnlAccount;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton validerButton;
    private JLabel lblErrors;
    private JLabel lblSuccess;
    private JPanel bankPanel;
    private JPasswordField creditCardField;
    private JPasswordField cveField;
    private ClientEntity client;
    private ClientService clientService;

    public AccountForm(ClientEntity client) {
        super(client);
        this.client = client;
        this.clientService = new ClientService();
        initComponents();
    }

    private void initComponents() {
        pnlAccount = new JPanel();
        pnlAccount.setLayout(new GridBagLayout());
        pnlAccount.setBackground(new Color(220, 230, 240)); // Couleur plus douce

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ✅ Label du titre
        JLabel lblTitle = new JLabel("📝 Mon Compte");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        pnlAccount.add(lblTitle, gbc);

        // ✅ Champ Nom
        gbc.gridy++;
        gbc.gridwidth = 1;
        pnlAccount.add(new JLabel("Nom :"), gbc);
        nameField = new JTextField(20);
        nameField.setText(client.getName());
        gbc.gridx = 1;
        pnlAccount.add(nameField, gbc);

        // ✅ Champ Email
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Email :"), gbc);
        emailField = new JTextField(20);
        emailField.setText(client.getEmail());
        gbc.gridx = 1;
        pnlAccount.add(emailField, gbc);

        // ✅ Champ Mot de passe
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Mot de passe :"), gbc);
        passwordField = new JPasswordField(20);
        passwordField.setText(client.getPassword());
        gbc.gridx = 1;
        pnlAccount.add(passwordField, gbc);

        // ✅ Champ Carte bancaire
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Numéro de carte :"), gbc);
        creditCardField = new JPasswordField(20);
        creditCardField.setText(client.getCreditCardNumber());
        gbc.gridx = 1;
        pnlAccount.add(creditCardField, gbc);

        // ✅ Champ CVE
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("CVV :"), gbc);
        cveField = new JPasswordField(5);
        cveField.setText(client.getCveNumber());
        gbc.gridx = 1;
        pnlAccount.add(cveField, gbc);

        // ✅ Label des erreurs
        lblErrors = new JLabel("");
        lblErrors.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        pnlAccount.add(lblErrors, gbc);

        // ✅ Label de succès
        lblSuccess = new JLabel("");
        lblSuccess.setForeground(new Color(0, 150, 0));
        gbc.gridy++;
        pnlAccount.add(lblSuccess, gbc);

        // ✅ Bouton de validation
        validerButton = new JButton("Mettre à jour");
        validerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        validerButton.setBackground(new Color(76, 175, 80)); // Vert
        validerButton.setForeground(Color.WHITE);
        validerButton.setFocusPainted(false);
        validerButton.setBorderPainted(false);
        validerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy++;
        pnlAccount.add(validerButton, gbc);

        // ✅ Ajout du panel principal à la fenêtre
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlAccount, BorderLayout.CENTER);

        // 🔥 Action du bouton "Mettre à jour"
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClientInfo();
            }
        });
    }

    /**
     * 🔥 Vérifie les champs et met à jour l'utilisateur dans la BDD.
     */
    private void updateClientInfo() {
        try {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = String.valueOf(passwordField.getPassword()).trim();

            // ✅ Vérification des champs obligatoires
            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                throw new Exception("Tous les champs doivent être remplis !");
            }

            // ✅ Vérification si l'email est déjà utilisé
            if (!newEmail.equals(client.getEmail()) && clientService.isEmailAlreadyExists(newEmail)) {
                throw new Exception("L'email est déjà utilisé par un autre utilisateur !");
            }

            // ✅ Mise à jour des informations du client
            client.setName(newName);
            client.setEmail(newEmail);
            client.setPassword(newPassword);

            if (creditCardField.getPassword().length > 0) {
                client.setCreditCardNumber(String.valueOf(creditCardField.getPassword()));
            }

            if (cveField.getPassword().length > 0) {
                client.setCveNumber(String.valueOf(cveField.getPassword()));
            }

            // ✅ Mise à jour en BDD
            clientService.updateClient(client);

            // ✅ Affichage du succès
            lblErrors.setText("");
            lblSuccess.setText("Mise à jour réussie !");
        } catch (Exception ex) {
            lblErrors.setText(ex.getMessage());
            lblSuccess.setText("");
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        System.out.println("DEJA SUR ACCOUNT");
    }
}
