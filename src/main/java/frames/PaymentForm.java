package frames;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import entity.ClientEntity;
import entity.CommandEntity;
import entity.CommandLineEntity;
import entity.VehicleEntity;
import service.CommandService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public class PaymentForm extends AbstractFrame {
    private JPanel pnlPayment;
    private List<CommandEntity> commandsToPay;
    private CommandService commandService;

    private JTextField txtCardNumber;
    private JPasswordField txtCVV;
    private JButton btnConfirmPayment;
    private JButton btnDownloadPDF;
    private JTable tableCommands;

    public PaymentForm(ClientEntity client, List<CommandEntity> commandsToPay) {
        super(client);
        pnlRoot.setLayout(new BorderLayout());
        this.commandsToPay = commandsToPay;
        this.commandService = new CommandService();

        setTitle("💳 Paiement Sécurisé");

        initUI();

        pnlRoot.add(pnlPayment,BorderLayout.CENTER);
        pnlRoot.setBorder(BorderFactory.createLineBorder(Color.red));
        this.pack();
        this.revalidate();
    }

    private void initUI() {

        pnlPayment.setLayout(new BorderLayout());
        // 📌 Titre en haut
        JLabel lblTitle = new JLabel("💳 Paiement Sécurisé", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlPayment.add(lblTitle, BorderLayout.NORTH);

        // 📌 TABLE DES COMMANDES (OCCUPE TOUTE LA LARGEUR)
        JPanel pnlCommandes = new JPanel(new BorderLayout());
        JLabel lblCommands = new JLabel("📜 Commandes à Payer", SwingConstants.CENTER);
        lblCommands.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tableCommands = createCommandTable();
        JScrollPane scrollPane = new JScrollPane(tableCommands);

        // 🔥 FORCE LA TABLE À PRENDRE TOUTE LA LARGEUR
        tableCommands.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        pnlCommandes.add(lblCommands, BorderLayout.NORTH);
        pnlCommandes.add(scrollPane, BorderLayout.CENTER);

        pnlPayment.add(pnlCommandes, BorderLayout.CENTER);

        BigDecimal totalAmount = commandsToPay.stream()
                .map(CommandEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 📌 SECTION PAIEMENT RESPONSIVE avec BorderLayout
        JPanel pnlPaymentDetails = new JPanel(new BorderLayout());
        pnlPaymentDetails.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true),
                "💰 Détails du Paiement",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(50, 50, 50)));

// 📌 Panel intermédiaire pour aligner les champs horizontalement
        JPanel pnlFields = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 lignes, 2 colonnes

// 🔹 Numéro de carte (Label + Champ)
        JLabel lblCardNumber = new JLabel("Numéro de Carte :");
        lblCardNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCardNumber = new JTextField(20);
        if (getClient().getCreditCardNumber() != null) {
            txtCardNumber.setText(getClient().getCreditCardNumber());
            txtCardNumber.setEditable(false);
        }

// 🔹 CVV (Label + Champ)
        JLabel lblCVV = new JLabel("CVV :");
        lblCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCVV = new JPasswordField(5);

// 🔥 Ajout des éléments horizontalement
        pnlFields.add(lblCardNumber);
        pnlFields.add(txtCardNumber);
        pnlFields.add(lblCVV);
        pnlFields.add(txtCVV);

// 📌 Ajout de pnlFields dans pnlPaymentDetails (CENTER)
        pnlPaymentDetails.add(pnlFields, BorderLayout.CENTER);

// 🔹 Montant total (Ajouté en bas)
        JLabel lblTotalAmount = new JLabel("💰 Montant total : " + totalAmount.intValueExact() + " €", SwingConstants.CENTER);
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlPaymentDetails.add(lblTotalAmount, BorderLayout.SOUTH);

// 📌 Boutons sous le montant total
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnConfirmPayment = new JButton("💳 Confirmer le Paiement");
        styleButton(btnConfirmPayment, new Color(76, 175, 80));
        btnConfirmPayment.addActionListener(e -> processPayment());

        btnDownloadPDF = new JButton("📄 Télécharger la Facture");
        styleButton(btnDownloadPDF, new Color(0, 123, 255));
        btnDownloadPDF.setVisible(false);
        btnDownloadPDF.addActionListener(e -> generateInvoicePDF());

        pnlButtons.add(btnConfirmPayment);
        pnlButtons.add(btnDownloadPDF);

// 🔥 Ajout des boutons en bas
        pnlPaymentDetails.add(pnlButtons, BorderLayout.SOUTH);

// 📌 Ajout final dans pnlPayment
        pnlPayment.add(pnlPaymentDetails, BorderLayout.SOUTH);

    }

    // 🔥 Style des boutons pour un meilleur rendu UI
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    // 📌 Création de la JTable responsive
    private JTable createCommandTable() {
        String[] columnNames = {"ID Commande", "ID CommandLine", "Statut", "Prix (€)", "Véhicule"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (CommandEntity commande : commandsToPay) {
            for (CommandLineEntity commandLine : commande.getCommandLines()) {
                // 📌 Récupérer l'ID de la ligne de commande
                int commandLineId = commandLine.getId().hashCode(); // Utilisation du hash si pas d'ID unique

                // 📌 Récupérer le véhicule associé
                String vehicleName = commandLine.getVehicle() != null
                        ? commandLine.getVehicle().getModel().getModelName()
                        : "Inconnu";

                // 📌 Récupérer le prix du véhicule (correctement maintenant)
                int vehiclePrice = commandLine.getVehicle() != null
                        ? commandLine.getVehicle().getPrice().intValueExact()
                        : 0;

                // 📌 Ajouter chaque ligne de commande séparément avec le bon prix
                tableModel.addRow(new Object[]{
                        commande.getIdCommand(),
                        commandLineId,  // ID de la ligne de commande
                        commande.getCommandStatus(),
                        vehiclePrice, // ✅ Prix du véhicule, et non le total de la commande
                        vehicleName
                });
            }
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(200, 200, 200));

        // 🔥 Active le redimensionnement automatique des colonnes
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        return table;
    }

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
        if (getClient().getCreditCardNumber() == null) {
            getClient().setCreditCardNumber(cardNumber);
        }

        // 📌 Marquer toutes les commandes sélectionnées comme "Payées"
        for (CommandEntity commande : commandsToPay) {
            commande.markAsPaid();
            commandService.updateCommand(commande);
        }

        JOptionPane.showMessageDialog(this, "Paiement effectué avec succès ! ✅", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);

        // 📌 Afficher le bouton de téléchargement du PDF
        btnDownloadPDF.setVisible(true);


        revalidate();
        repaint();
    }

    private void generateInvoicePDF() {
        String fileName = "facture_" + LocalDate.now() + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(new File(fileName));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("📄 Facture d'Achat").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Client : " + getClient().getName() + " (ID: " + getClient().getIdClient() + ")"));
            if (getClient().getCreditCardNumber() != null) {
                document.add(new Paragraph("Carte Bancaire : **** **** **** " + getClient().getCreditCardNumber().substring(getClient().getCreditCardNumber().length() - 4)));
            }
            document.add(new Paragraph("Date : " + LocalDate.now()).setFontSize(12));

            document.add(new Paragraph("\n🔹 Commandes Payées :"));
            for (CommandEntity commande : commandsToPay) {
                document.add(new Paragraph("Commande #" + commande.getIdCommand() + " - Montant : " + commande.getTotalAmount() + " €").setBold());
                for (VehicleEntity vehicle : commande.getVehicles()) {
                    document.add(new Paragraph("  • " + vehicle.getModel().getBrandName() + " " + vehicle.getModel().getModelName() + " - " + vehicle.getPrice() + " €"));
                }
            }

            document.add(new Paragraph("\n✅ Merci pour votre achat !").setItalic().setTextAlignment(TextAlignment.CENTER));
            document.close();

            JOptionPane.showMessageDialog(this, "Facture téléchargée avec succès : " + fileName, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du PDF.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
    }
}
