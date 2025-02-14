package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

public class AccountForm extends AbstractFrame {
    private JPanel pnlAccount;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField creditCardField; // Utilisation d'un JTextField pour le numéro de carte
    private JTextField cveField;         // et pour le CVV
    private JTextField phoneField;       // Champ pour le numéro de téléphone
    private JTextField addressField;     // Champ pour l'adresse postale
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
        creditCardField = new JTextField(20);
        creditCardField.setText(getClient().getCreditCardNumber());
        pnlAccount.add(creditCardField, gbc);

        // CVV
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("CVV :"), gbc);
        gbc.gridx = 1;
        cveField = new JTextField(5);
        cveField.setText(getClient().getCveNumber());
        pnlAccount.add(cveField, gbc);

        // Numéro de téléphone
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setText(getClient().getPhoneNumber());
        pnlAccount.add(phoneField, gbc);

        // Adresse
        gbc.gridx = 0;
        gbc.gridy++;
        pnlAccount.add(new JLabel("Adresse :"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        addressField.setText(getClient().getPostalAddress());
        pnlAccount.add(addressField, gbc);

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
        // Méthode à implémenter si nécessaire.
    }

    /**
     * Vérifie les champs avec des regex et met à jour les informations du client.
     */
    private void updateClientInfo() {
        try {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            String newCreditCard = creditCardField.getText().trim();
            String newCve = cveField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String newAddress = addressField.getText().trim();

            // Vérification des champs obligatoires
            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                throw new Exception("Tous les champs obligatoires doivent être remplis !");
            }

            // Validation du nom : lettres, espaces, tirets et apostrophes (2 à 50 caractères)
            if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s\\-']{2,50}$", newName)) {
                throw new Exception("Nom invalide !");
            }

            // Validation de l'email
            if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", newEmail)) {
                throw new Exception("Email invalide !");
            }

            // Validation du mot de passe : au moins 8 caractères, une lettre minuscule, un chiffre et un caractère spécial parmi #?!@$%^&*+-
            if (!Pattern.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$", newPassword)) {
                throw new Exception("Mot de passe invalide !");
            }

            // Optionnel : Validation du numéro de carte (si non vide, uniquement chiffres, 13 à 19 chiffres)
            if (!newCreditCard.isEmpty() && !Pattern.matches("^\\d{13,19}$", newCreditCard)) {
                throw new Exception("Numéro de carte invalide !");
            }

            // Optionnel : Validation du CVV (si non vide, 3 ou 4 chiffres)
            if (!newCve.isEmpty() && !Pattern.matches("^\\d{3,4}$", newCve)) {
                throw new Exception("CVV invalide !");
            }

            // Optionnel : Validation du numéro de téléphone (lettres interdites, autorise chiffres, espaces, tirets, et +)
            if (!newPhone.isEmpty() && !Pattern.matches("^[\\d\\s\\-+()]{7,20}$", newPhone)) {
                throw new Exception("Numéro de téléphone invalide !");
            }

            // Optionnel : Validation de l'adresse (3 à 100 caractères, permet lettres, chiffres, espaces et quelques ponctuations)
            if (!newAddress.isEmpty() && !Pattern.matches("^[\\w\\s,.'-]{3,100}$", newAddress)) {
                throw new Exception("Adresse invalide !");
            }

            // Vérifier si l'email a changé et s'il existe déjà
            if (!newEmail.equals(getClient().getEmail()) && clientService.isEmailAlreadyExists(newEmail)) {
                throw new Exception("L'email est déjà utilisé par un autre utilisateur !");
            }

            // Mise à jour des informations du client
            getClient().setName(newName);
            getClient().setEmail(newEmail);
            getClient().setPassword(newPassword);
            getClient().setCreditCardNumber(newCreditCard);
            getClient().setCveNumber(newCve);
            getClient().setPhoneNumber(newPhone);
            getClient().setPostalAddress(newAddress);

            clientService.updateClient(getClient());

            lblError.setText("");
            lblSuccess.setText("Mise à jour réussie !");
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
            lblSuccess.setText("");
        }
    }
}
