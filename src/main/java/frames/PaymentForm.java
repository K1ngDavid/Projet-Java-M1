package frames;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

        pnlRoot.add(pnlPayment, BorderLayout.CENTER);
        this.pack();
        this.revalidate();
    }

    private void initUI() {
        // Panel principal de paiement
        pnlPayment = new JPanel(new BorderLayout(20, 20));
        pnlPayment.setBackground(Color.WHITE);
        pnlPayment.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---- Header ----
        JLabel lblTitle = new JLabel("💳 Paiement Sécurisé", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlPayment.add(lblTitle, BorderLayout.NORTH);

        // ---- Table des commandes ----
        JPanel pnlCommandes = new JPanel(new BorderLayout(10, 10));
        pnlCommandes.setBackground(Color.WHITE);
        JLabel lblCommands = new JLabel("📜 Commandes à Payer", SwingConstants.CENTER);
        lblCommands.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCommands.setForeground(new Color(33, 33, 33));
        pnlCommandes.add(lblCommands, BorderLayout.NORTH);
        tableCommands = createCommandTable();
        JScrollPane scrollPane = new JScrollPane(tableCommands);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlCommandes.add(scrollPane, BorderLayout.CENTER);
        pnlPayment.add(pnlCommandes, BorderLayout.CENTER);

        // ---- Section Paiement ----
        JPanel pnlPaymentDetails = new JPanel(new BorderLayout(10, 10));
        pnlPaymentDetails.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true),
                "💰 Détails du Paiement",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(50, 50, 50));
        pnlPaymentDetails.setBorder(titledBorder);

        // Panel pour les champs de saisie (numéro de carte et CVV)
        JPanel pnlFields = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlFields.setBackground(Color.WHITE);
        JLabel lblCardNumber = new JLabel("Numéro de Carte :");
        lblCardNumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCardNumber = new JTextField(20);
        if (getClient().getCreditCardNumber() != null) {
            txtCardNumber.setText(getClient().getCreditCardNumber());
            txtCardNumber.setEditable(false);
        }
        JLabel lblCVV = new JLabel("CVV :");
        lblCVV.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCVV = new JPasswordField(5);
        pnlFields.add(lblCardNumber);
        pnlFields.add(txtCardNumber);
        pnlFields.add(lblCVV);
        pnlFields.add(txtCVV);
        pnlPaymentDetails.add(pnlFields, BorderLayout.CENTER);

        // Affichage du montant total
        BigDecimal totalAmount = commandsToPay.stream()
                .map(CommandEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        JLabel lblTotalAmount = new JLabel("💰 Montant total : " + totalAmount.intValueExact() + " €", SwingConstants.CENTER);
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalAmount.setForeground(new Color(76, 175, 80));
        pnlPaymentDetails.add(lblTotalAmount, BorderLayout.SOUTH);

        // Panel des boutons de la section paiement
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlButtons.setBackground(Color.WHITE);
        btnConfirmPayment = new JButton("💳 Confirmer le Paiement");
        styleButton(btnConfirmPayment, new Color(76, 175, 80));
        btnConfirmPayment.addActionListener(e -> processPayment());

        btnDownloadPDF = new JButton("📄 Télécharger la Facture");
        styleButton(btnDownloadPDF, new Color(0, 123, 255));
        btnDownloadPDF.setVisible(false);
        btnDownloadPDF.addActionListener(e -> generateInvoicePDF());

        pnlButtons.add(btnConfirmPayment);
        pnlButtons.add(btnDownloadPDF);
        // Pour aligner ces boutons, on les place dans un panel au-dessus des champs
        JPanel pnlPaymentDetailWrapper = new JPanel(new BorderLayout(10, 10));
        pnlPaymentDetailWrapper.setBackground(Color.WHITE);
        pnlPaymentDetailWrapper.add(pnlFields, BorderLayout.NORTH);
        pnlPaymentDetailWrapper.add(lblTotalAmount, BorderLayout.CENTER);
        pnlPaymentDetailWrapper.add(pnlButtons, BorderLayout.SOUTH);

        // On remplace le centre du panneau de paiement détaillé par notre wrapper
        pnlPaymentDetails.removeAll();
        pnlPaymentDetails.add(pnlPaymentDetailWrapper, BorderLayout.CENTER);

        pnlPayment.add(pnlPaymentDetails, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private JTable createCommandTable() {
        String[] columnNames = {"ID Commande", "ID CommandLine", "Statut", "Prix (€)", "Véhicule"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (CommandEntity commande : commandsToPay) {
            for (CommandLineEntity commandLine : commande.getCommandLines()) {
                int commandLineId = commandLine.getId().hashCode();
                String vehicleName = commandLine.getVehicle() != null
                        ? commandLine.getVehicle().getModel().getModelName()
                        : "Inconnu";
                int vehiclePrice = commandLine.getVehicle() != null
                        ? commandLine.getVehicle().getPrice().intValueExact()
                        : 0;
                tableModel.addRow(new Object[]{
                        commande.getIdCommand(),
                        commandLineId,
                        commande.getCommandStatus(),
                        vehiclePrice,
                        vehicleName
                });
            }
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
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
        // Sauvegarder le numéro de carte si non enregistré
        if (getClient().getCreditCardNumber() == null) {
            getClient().setCreditCardNumber(cardNumber);
        }

        // Pour chaque commande à payer, on met à jour le statut et on persiste la modification
        for (CommandEntity commande : commandsToPay) {
            commande.markAsPaid();  // Change le statut en "Payée"
            commandService.updateCommand(commande);  // Met à jour en base
        }

        // Afficher le succès et afficher le bouton de téléchargement PDF
        JOptionPane.showMessageDialog(this, "Paiement effectué avec succès ! ✅", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);
        btnDownloadPDF.setVisible(true);

        // Rafraîchir l'interface (vous pouvez notifier d'autres formulaires ou recharger le modèle)
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
        // Implémentez selon vos besoins
    }
}
