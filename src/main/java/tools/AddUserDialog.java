package tools;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddUserDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnAdd;
    private ClientService clientService;

    public AddUserDialog(Frame owner) {
        super(owner, "Ajouter un utilisateur", true);
        clientService = new ClientService();
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Champ Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        panel.add(txtName, gbc);

        // Champ Email
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Champ Mot de passe
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);

        // Champ Rôle (ADMIN ou CLIENT)
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Rôle:"), gbc);
        gbc.gridx = 1;
        cbRole = new JComboBox<>(new String[]{"ADMIN", "CLIENT"});
        panel.add(cbRole, gbc);

        add(panel, BorderLayout.CENTER);

        btnAdd = new JButton("Ajouter");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(this::addUser);

        add(btnAdd, BorderLayout.SOUTH);
    }

    private void addUser(ActionEvent evt) {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String roleStr = (String) cbRole.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "⚠ Email invalide!\n",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification du mot de passe selon les mêmes conditions que dans RegisterForm
        if (!password.matches("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*+\\-]).{8,}$")) {
            JOptionPane.showMessageDialog(this, "⚠ Mot de passe invalide !\n" +
                            "Le mot de passe doit contenir au moins 8 caractères, une lettre minuscule, un chiffre et un caractère spécial.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClientEntity newUser = new ClientEntity();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(ClientEntity.Role.valueOf(roleStr));

        if (clientService.addClient(newUser)) {
            JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
