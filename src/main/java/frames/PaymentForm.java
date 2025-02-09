package frames;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import service.CommandService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public class PaymentForm extends JFrame {
    private ClientEntity client;
    private JPanel pnlPayment;
    private List<CommandEntity> commandsToPay;
    private CommandService commandService;

    private JTextField txtCardNumber;
    private JPasswordField txtCVV;
    private JButton btnConfirmPayment;
    private JButton btnDownloadPDF;

    public PaymentForm(ClientEntity client, List<CommandEntity> commandsToPay) {
        this.client = client;
        this.commandsToPay = commandsToPay;
        this.commandService = new CommandService();

        setTitle("Paiement des Commandes");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        pnlPayment = new JPanel(new GridLayout(6, 1, 10, 10));
        pnlPayment.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 📌 Titre
        JLabel lblTitle = new JLabel("💳 Paiement Sécurisé", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // 📌 Numéro de carte
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

        // 📌 Ajout de l'événement de paiement
        btnConfirmPayment.addActionListener(e -> processPayment());

        // 📌 Calculer le total des commandes à payer
        BigDecimal totalAmount = commandsToPay.stream()
                .map(CommandEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // ✅ Somme des montants

        // 📌 Label affichant le montant total
        JLabel lblTotalAmount = new JLabel("💰 Montant total à payer : " + totalAmount.intValueExact() + " €", SwingConstants.CENTER);
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 📌 Bouton pour télécharger le PDF (initialement caché)
        btnDownloadPDF = new JButton("📄 Télécharger la Facture");
        btnDownloadPDF.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDownloadPDF.setBackground(new Color(0, 123, 255));
        btnDownloadPDF.setForeground(Color.WHITE);
        btnDownloadPDF.setSize(200,400);
        btnDownloadPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDownloadPDF.setVisible(false); // Caché au départ

        btnDownloadPDF.addActionListener(e -> generateInvoicePDF());

        // 📌 Ajout des composants au panel
        pnlPayment.add(lblTitle);
        pnlPayment.add(new JLabel("Numéro de Carte :"));
        pnlPayment.add(txtCardNumber);
        pnlPayment.add(new JLabel("CVV :"));
        pnlPayment.add(txtCVV);
        pnlPayment.add(lblTotalAmount);

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

        // 📌 Afficher le bouton de téléchargement du PDF
        btnDownloadPDF.setVisible(true);
        add(btnDownloadPDF, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * 📄 Génération de la facture PDF.
     */
    private void generateInvoicePDF() {
        String fileName = "facture_" + LocalDate.now() + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(new File(fileName));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("📄 Facture d'Achat").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Client : " + client.getName() + " (ID: " + client.getIdClient() + ")"));
            if (client.getCreditCardNumber() != null) {
                document.add(new Paragraph("Carte Bancaire : **** **** **** " + client.getCreditCardNumber().substring(client.getCreditCardNumber().length() - 4)));
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
}
