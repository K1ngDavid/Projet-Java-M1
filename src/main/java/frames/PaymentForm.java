package frames;

import entity.ClientEntity;
import entity.CommandEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaymentForm extends AbstractFrame {

    private JPanel pnlPayment;
    private JTable tableCommands;
    private DefaultTableModel tableModel;
    private JButton btnPay;

    public PaymentForm(ClientEntity client) {
        super(client);
        initComponents();
        loadPendingCommands(); // Charge les commandes en attente
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
    }

    private void initComponents() {
        pnlPayment = new JPanel(new BorderLayout());

        // 🔥 Définition des colonnes du tableau
        String[] columnNames = {"ID Commande", "Date", "Montant Total (€)", "Status"};

        // 🔥 Création du modèle de tableau (modifiable pour MAJ dynamique)
        tableModel = new DefaultTableModel(columnNames, 0);
        tableCommands = new JTable(tableModel);
        tableCommands.setRowHeight(30); // Augmenter la hauteur des lignes
        tableCommands.setGridColor(Color.LIGHT_GRAY); // Couleur des séparations
        tableCommands.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 🔥 Styliser l'en-tête du tableau
        JTableHeader header = tableCommands.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(50, 115, 220)); // Bleu moderne
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // 🔥 Centrer le texte dans les cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableCommands.getColumnCount(); i++) {
            tableCommands.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 🔥 Ajout du tableau dans un JScrollPane pour le rendre scrollable
        JScrollPane scrollPane = new JScrollPane(tableCommands);
        pnlPayment.add(scrollPane, BorderLayout.CENTER);

        // ✅ Bouton de paiement
        btnPay = new JButton("Payer les commandes sélectionnées");
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPay.setBackground(new Color(76, 175, 80)); // Vert
        btnPay.setForeground(Color.WHITE);
        btnPay.setFocusPainted(false);
        btnPay.setBorderPainted(false);
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🔥 Action de paiement
        btnPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paySelectedCommands();
            }
        });

        // ✅ Ajout du bouton dans un panneau en bas
        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelSouth.add(btnPay);

        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlPayment, BorderLayout.CENTER);
        pnlRoot.add(panelSouth, BorderLayout.SOUTH);
    }

    /**
     * 🔥 Charge les commandes en attente de paiement et les affiche dans la JTable.
     */
    private void loadPendingCommands() {
        ClientEntity client = this.getClient();
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Erreur : Client non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupération des commandes du client
        List<CommandEntity> commandes = client.getCommands();

        // Nettoyage du tableau avant d'ajouter de nouvelles données
        tableModel.setRowCount(0);

        for (CommandEntity commande : commandes) {
            if ("En attente".equals(commande.getCommandStatus())) { // Filtrer les commandes en attente
                Object[] rowData = {
                        commande.getIdCommand(),
                        commande.getCommandDate(),
                        commande.getTotalAmount(), // Méthode à implémenter dans CommandEntity
                        commande.getCommandStatus()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    /**
     * 🔥 Permet de marquer les commandes sélectionnées comme "Payées".
     */
    private void paySelectedCommands() {
        int[] selectedRows = tableCommands.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins une commande.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClientEntity client = this.getClient();
        List<CommandEntity> commandes = client.getCommands();

        for (int row : selectedRows) {
            int commandId = (int) tableModel.getValueAt(row, 0);

            // Recherche de la commande correspondante
            for (CommandEntity commande : commandes) {
                if (commande.getIdCommand() == commandId) {
                    commande.markAsPaid(); // Marquer la commande comme payée
                }
            }
        }

        // ✅ Affichage d'un message de confirmation
        JOptionPane.showMessageDialog(this, "Paiement effectué avec succès !", "Paiement", JOptionPane.INFORMATION_MESSAGE);

        // 🔄 Rafraîchissement de l'affichage
        loadPendingCommands();
    }
}
