package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import service.CommandService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaymentForm extends JFrame {
    private ClientEntity client;
    private JPanel pnlPayment;
    private List<CommandEntity> commandsToPay;
    private CommandService commandService;

    private JTextField txtCardNumber;
    private JPasswordField txtCVV;
    private JButton btnConfirmPayment;

    public PaymentForm(ClientEntity client, List<CommandEntity> commandsToPay) {
        this.client = client;
        this.commandsToPay = commandsToPay;
        this.commandService = new CommandService();

        setTitle("Paiement des Commandes");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        pnlPayment.setLayout(new GridLayout(5, 1, 10, 10));
        pnlPayment.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 📌 Titre
        JLabel lblTitle = new JLabel("💳 Paiement Sécurisé", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // 📌 Récupérer ou Demander le Numéro de Carte
        txtCardNumber = new JTextField();
        if (client.getCreditCardNumber() != null) {
            txtCardNumber.setText(client.getCreditCardNumber());
            txtCardNumber.setEditable(false); // 🔒 Ne pas modifier si déjà enregistré
        } else {
            txtCardNumber.setToolTipText("Entrez votre numéro de carte bancaire");
        }

        // 📌 CVV (toujours requis)
        txtCVV = new JPasswordField();
        txtCVV.setToolTipText("Entrez le code CVV");

        // 📌 Bouton de confirmation
        btnConfirmPayment = new JButton("💳 Confirmer le Paiement");
        btnConfirmPayment.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmPayment.setBackground(new Color(76, 175, 80));
        btnConfirmPayment.setForeground(Color.WHITE);
        btnConfirmPayment.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 📌 Événement de paiement
        btnConfirmPayment.addActionListener(e -> processPayment());

        // 📌 Ajout des composants au panel
        pnlPayment.add(lblTitle);
        pnlPayment.add(new JLabel("Numéro de Carte :"));
        pnlPayment.add(txtCardNumber);
        pnlPayment.add(new JLabel("CVV :"));
        pnlPayment.add(txtCVV);

        // 📌 Ajout du panel et bouton au frame
        add(pnlPayment, BorderLayout.CENTER);
        add(btnConfirmPayment, BorderLayout.SOUTH);
    }

    /**
     * 🔥 Vérifie les informations de paiement et marque les commandes comme payées.
     */
    private void processPayment() {
        String cardNumber = txtCardNumber.getText().trim();
        String cvv = new String(txtCVV.getPassword()).trim();

        if (cardNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro de carte valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cvv.length() != 3) {
            JOptionPane.showMessageDialog(this, "Le CVV doit être composé de 3 chiffres.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 📌 Si la carte n'était pas enregistrée, la sauvegarder
        if (client.getCreditCardNumber() == null) {
            client.setCreditCardNumber(cardNumber);
        }

        // 📌 Marquer toutes les commandes sélectionnées comme "Payées"
        for (CommandEntity commande : commandsToPay) {
            commande.markAsPaid();
            commandService.updateCommand(commande);
        }

        JOptionPane.showMessageDialog(this, "Paiement effectué avec succès ! ✅", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // 📌 Fermer la fenêtre après paiement
    }
}
